package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.PigDisambiguator;
import pl.edu.icm.coansys.disambiguation.clustering.strategies.SingleLinkageHACStrategy_OnlyMax;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.disambiguation.idgenerators.IdGenerator;
import pl.edu.icm.coansys.disambiguation.idgenerators.UuIdGenerator;

public class ExhaustiveAND extends EvalFunc<DataBag> {

	private double threshold;
	private final double NOT_CALCULATED = Double.NEGATIVE_INFINITY;	
	private PigDisambiguator[] features;
	private List<FeatureInfo> featureInfos;
	private double sim[][];

	public ExhaustiveAND(String threshold, String featureDescription){
		this.threshold = Double.parseDouble(threshold);
        this.featureInfos = FeatureInfo.parseFeatureInfoString(featureDescription);
        DisambiguatorFactory ff = new DisambiguatorFactory();

        int featureNum = 0;
        for ( FeatureInfo fi : featureInfos ){
        	if(!fi.getDisambiguatorName().equals("")) featureNum++;
        }

        this.features = new PigDisambiguator[featureNum];

        int index = -1;
        for ( FeatureInfo fi : featureInfos ){
        	if ( fi.getDisambiguatorName().equals("") ) continue;
        	index++;
        	Disambiguator d = ff.create(fi);
        	features[index] = new PigDisambiguator(d);
        }
	}

	/*
	 * Tuple: sname,{(contribId:chararray,contribPos:int,sname:chararray, metadata:map[{(chararray)}])},count
	 */

	@SuppressWarnings("unchecked")
	@Override
	public DataBag exec( Tuple input ) throws IOException {
		
		if ( input == null || input.size() == 0 ) return null;
		try{
			//bag: contribId,pozycja - po co?,sname,mapa: <extractor,bag: tuple ze stringiem>,
			//opcjonalnie: bag: tuple ( int index w sim contrib X, int ..contrib Y, double sim value) 
			//TODO w razie nudy:
			//wystarczyloby wrzucac do udf'a tylko (contrib id i mape z metadanymi) z datagroup
			//co daje odpornosc na zmiany struktury calej tabeli
			//ale to bym musial zmienic na poziomie generowania tabel (nie generowac z niepotrzebnymi danymi)

			DataBag contribs = (DataBag) input.get(0);  //biore bag'a z kontrybutorami
			Iterator<Tuple> it = contribs.iterator();	//iterator po bag'u

			//TODO: zamienic ponizej liste map na liste list, albo najlepiej tablice tablic..
			//przy wyciaganiu mapy z tupla rzucic na odpowiednie
			List< Map<String,Object> > contribsT = new LinkedList< Map<String,Object> > ();
			List< String > contribsId = new LinkedList<String>();


			while ( it.hasNext() ) { //iteruje sie po bag'u, zrzucam bag'a do tablicy Tupli
				Tuple t = it.next();
				contribsId.add( (String) t.get(0) ); //biore contrId z Tupla
				contribsT.add( (Map<String, Object>) t.get(3) );
			}
			
			//inicjuje sim[][]
			sim = new double[ contribsT.size() ][];
			for ( int i = 1; i < contribsT.size(); i++ ) {
				sim[i] = new double[i];
				for ( int j = 0; j < i; j++ ) 
					sim[i][j] = NOT_CALCULATED;
			}
				
			//jesli podano sim do inicjacji:
			if ( input.size() == 2 ) {			
				DataBag similarities = (DataBag) input.get(1);  //biore bag'a z wyliczonymi podobienstwami
				it = similarities.iterator();	//iterator po bag'u
				while ( it.hasNext() ) { //iteruje sie po bag'u, zrzucam bag'a do tablicy Tupli
					Tuple t = it.next();
					
					int idX = (Integer) t.get(0);
					int idY = (Integer) t.get(1);						
					double simValue = (Double) t.get(2);
					
					try {	
						sim[ idX ][ idY ] = simValue;
						
					} catch ( java.lang.ArrayIndexOutOfBoundsException e ) {
						
						String m = "Out of bounds during sim init by values from input: " + "idX: " + idX + ", idY: " + idY + ", sim.length: " + sim.length + 
								", contrib number: " + contribsT.size();
						
						if ( sim.length > idX )
							m += ", sim[idX].length: " + sim[idX].length;

						m+= "\n" + "Tuple debug: " + t.toString();

						throw new Exception(m, e);
					}
				}			
			}
			
			//obliczam sim[][]
			calculateAffinity ( contribsT );

			// clusterAssociations[ index_kontrybutora ] = klaster, do ktorego go przyporzadkowano
	        int[] clusterAssociations = new SingleLinkageHACStrategy_OnlyMax().clusterize( sim );

	        Map<Integer,List<String>> clusterMap = splitIntoMap( clusterAssociations, contribsId );

	        return createResultingTuples( clusterMap );
	        //zwraca bag: Tuple z (Obiektem z (String (UUID) i bag: { Tuple z ( String (contrib ID) ) } ) )
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
		
		//return new DefaultDataBag();
	}

	//po co contribsId przekazywane jako argument?
	//po 1. - nie uzywane ponizej
	//po 3. - id kontrybutorow siedzą w contribsT - get(0)
	//ad po 3. - w ogole te Id nie sa ponizszej potrzebne, mozna by zmienic koncepcje
	//i wrzucac gotową listę map a nie tablice tupli
	//powyzsze zrobione

	private void calculateAffinity( List< Map<String,Object> > contribsT/*, List<String> contribsId */) throws Exception {

		for ( int i = 1; i < contribsT.size(); i++ ) {
			for ( int j = 0; j < i; j++ ) {
				
				//jesli wartosc jest obliczona, to nie obliczam ponownie
				if( sim[i][j] != NOT_CALCULATED ) continue;
				sim[i][j] = threshold;
				
				for ( int d = 0; d < features.length; d++ ){
					
					FeatureInfo featureInfo = featureInfos.get(d);

					//ponizsze implikuje nie stworzenie obiektu disambiguatora
					//pod features[d]
					if ( featureInfo.getDisambiguatorName().equals("") ) continue;

					//pobieranie wartosci (cech) spod danego klucza (nazwy ekstraktora = nazwa cechy)
					//w contribsT.get(i) siedzi interesujaca nas mapa
					//de facto Object = DataBag.
					//Biore z i'tej mapy (zbioru cech i'tego kontrybutora) Bag wartości danej cechy:
					Object oA = contribsT.get(i).get( featureInfo.getFeatureExtractorName() );
					Object oB = contribsT.get(j).get( featureInfo.getFeatureExtractorName() );

					double partial = features[d].calculateAffinity( oA, oB );
					partial = partial / featureInfo.getMaxValue() * featureInfo.getWeight();
					sim[i][j] += partial;
					
					//na pewno ci sami, wiec przerywam
        			if ( sim[i][j] >= 0 ) break;
				}
			}
		}
	}

	protected Map<Integer, List<String>> splitIntoMap(int[] clusterAssociation, List<String> authorIds) {

		//pod dany klaster id (clusterAssociation) wrzucamy id kontrybutorow
		Map<Integer, List<String>> clusterMap = new HashMap<Integer, List<String>>();
		//TODO: mapa na np liste jak w aproximate
		
        for (int i = 0; i < clusterAssociation.length; i++) {
            addToMap(clusterMap, clusterAssociation[i], authorIds.get(i));
        }
		return clusterMap;
	}

	protected <K, V> void addToMap(Map<Integer, List<String>> clusters, int clusterAssociation, String string) {

		//patrze czy klucz (id klastra) jest juz w mapie
		List<String> values = clusters.get(clusterAssociation);
        if (values == null) {
            values = new ArrayList<String>();
            values.add(string);
            clusters.put(clusterAssociation, values);
        }else{
        	//jak nie, to dodaje do danego klastra (value) id kontrybutora
        	values.add(string);
        }
    }

	protected DataBag createResultingTuples( Map<Integer, List<String>> clusterMap /*,
			 List<String> authorIds2 */ ) {
    	IdGenerator idgenerator = new UuIdGenerator();

    	DataBag ret = new DefaultDataBag();
        for (Map.Entry<Integer, List<String>> o : clusterMap.entrySet()) {
        	String clusterId = idgenerator.genetareId(o.getValue());

        	DataBag contribs = new DefaultDataBag();
        	for(String s : o.getValue()){
        		contribs.add(TupleFactory.getInstance().newTuple(s));
        	}

        	Object[] to = new Object[]{clusterId,contribs};
	        ret.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
        }
		return ret;
	}
}

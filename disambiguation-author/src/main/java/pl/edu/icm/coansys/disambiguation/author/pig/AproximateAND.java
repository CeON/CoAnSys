package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.pig.PigDisambiguator;
import pl.edu.icm.coansys.disambiguation.clustering.strategies.SingleLinkageHACStrategy_OnlyMax;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

// zmiany w stosunku do pierwotnego Exhaustive:
// - poslugiwanie sie nadanymi identyfikatorami int zamiast string dla kazdego kontrybutora
// zamiana mapy na tablice - zbicie log'a + porownywanie stringow / ich hashy, ktore trzeba generowac jest dluzsze


public class AproximateAND extends EvalFunc<DataBag> {

	private double threshold;
	private final double NOT_CALCULATED = Double.NEGATIVE_INFINITY;	
	private PigDisambiguator[] features;
	private List<FeatureInfo> featureInfos;
	private double sim[][];
	private Tuple datain[];
	//TODO: możliwie pozamieniac listy na tablice
	
	
	public AproximateAND(String threshold, String featureDescription){
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
        	System.out.println( fi.getDisambiguatorName() );
        	index++;
        	Disambiguator d = ff.create(fi);
        	features[index] = new PigDisambiguator(d);
        }
	}

	/*
	 * Tuple: sname,{(contribId:chararray,contribPos:int,sname:chararray, metadata:map[{(chararray)}])},count
	 */


	@Override
	public DataBag exec( Tuple input ) throws IOException {
		
		/*System.out.println( input.size() );
		
		for( int i = 0; i < input.size(); i++ )
			System.out.println( input.get(i) );

		System.out.println( "------------------" );*/
		
		
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
			datain = new DefaultTuple[ (int) contribs.size() ];
			
			List< Map<String,Object> > contribsT = new LinkedList< Map<String,Object> > ();
			//List< String > contribsId = new LinkedList<String>(); //nie potrzebuje, bo w wyniku posluguje sie nadanymi etykietami a nie sname

			//Ziana koncepcji: do calculateAffinity powedruje lista map, a nie tablica tupli z bag'a
			//(cale tuple sa tam niepotrzebne)
			//+ jesli zmieni sie struktura tabeli, to musze wprowadzic zmiany tylko w exec
			
			int k = 0;
			while ( it.hasNext() ) { //iteruje sie po bag'u, zrzucam bag'a do tablicy Tupli
				Tuple t = it.next();
				datain[ k++ ] = t;
				//contribsId.add( (String) t.get(0) ); //biore contrId z Tupla
				contribsT.add( (Map<String, Object>) t.get(3) );
				
			}
			
			//inicjuje sim[][]
			//TODO: przeanalizowac reszte kodu, bo indeksowanie od 1 nie od 0 - moglem sie pomylic
			sim = new double[contribsT.size()][];
			for ( int i = 1; i < contribsT.size(); i++ ) {
				sim[i] = new double[i];
				for ( int j = 0; j < i; j++ ) 
					sim[i][j] = NOT_CALCULATED;
			}
			
			/*
			//jesli podano sim do inicjacji:
			if ( input.size() == 2 ) {			
				DataBag similarities = (DataBag) input.get(1);  //biore bag'a z wyliczonymi podobienstwami
				it = similarities.iterator();	//iterator po bag'u
				while ( it.hasNext() ) { //iteruje sie po bag'u, zrzucam bag'a do tablicy Tupli
					Tuple t = it.next();
					int idX = t.getType(0);
					int idY = t.getType(1);
					double simValue = t.getType(2);
					
					sim[ idX ][ idY ] = simValue;
				}			
			}
			*/
			
			//obliczam sim[][]
			calculateAffinity ( contribsT );

			// clusterAssociations[ index_kontrybutora ] = klaster, do ktorego go przyporzadkowano
	        int[] clusterAssociations = new SingleLinkageHACStrategy_OnlyMax().clusterize( sim );
	        //TODO: gdybym chcial wyodrebnic z exhaustive i aproximate do klasy nadrzednej, to strategia poza exec i inicjowana w overridowanym czyms (konstruktorze)

	        List < Vector<Integer> >  clusterMap = splitIntoClusters( clusterAssociations );

	        return createResultingTuples( clusterMap );
	        //zwraca bag: Tuple z (Obiektem z (String (UUID) i bag: { Tuple z ( String (contrib ID) ) } ) )
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
		
		//return new DefaultDataBag();
	}

	// TODO: ponizsza metode wydzielic do nadklasy (jest w takiej samej postaci w Exhaustive i Aproximate)
	// to samo konstruktor i pare innych
	private void calculateAffinity( List< Map<String,Object> > contribsT ) throws Exception {

		for ( int i = 1; i < contribsT.size(); i++ ) {
			for ( int j = 0; j < i; j++ ) {
				
				//jesli wartosc jest obliczona, to nie obliczam ponownie
				if ( sim[i][j] != NOT_CALCULATED ) continue;
				sim[i][j] = threshold;
				
				for ( int d = 0; d < features.length; d++ ) {
					
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
        			if ( sim[i][j] > 0 ) break;
				}
			}
		}
	}

	protected List < Vector<Integer> > splitIntoClusters( int[] clusterAssociation ) {
		
		int clusterNumber = Collections.max( Arrays.asList( ArrayUtils.toObject( clusterAssociation ) ) );
		List < Vector<Integer> > clusters = new ArrayList < Vector< Integer > > ( clusterNumber );
		// cluster[ id klastra ] = vector  simId kontrybutorow
		
        for (int i = 0; i < clusterAssociation.length; i++) {
            clusters.get( clusterAssociation[i] ).add( i );
        }
		return clusters;
	}

	protected DataBag createResultingTuples( List < Vector<Integer> > clusters ) {
    	
		//IdGenerator idgenerator = new UuIdGenerator();
    	DataBag ret = new DefaultDataBag();
    	int simIdToClusterId[] = new int[ sim.length ];
    	
    	//iteruje po klastrach
    	for ( Vector<Integer> cluster: clusters ) {
        	
        	DataBag contribDatas = new DefaultDataBag();
        	DataBag similarities = new DefaultDataBag();

        	// iteruje po kontrybutorach w klastrze (znajdują sie tam simId)
        	for ( int i = 0; i < cluster.size(); i++ ) {
        		
        		int sidX = cluster.get( i );
        		
        		simIdToClusterId[ sidX ] = i;
        		contribDatas.add( datain[ sidX ] );
        		
        		//dodaje do wyniku wartosci podobienst dla faktycznie obliczonych
        		for ( int j = 0; j < i; j++ ) {
        			int sidY = cluster.get( j );
        			
        			assert( sidX > sidY );
        			assert( simIdToClusterId[ sidX ] > simIdToClusterId[ sidY ] );
        			
        			if ( sim[ sidX ][ sidY ] != Double.NEGATIVE_INFINITY && sim[ sidX ][ sidY ] != Double.POSITIVE_INFINITY ) {
        				Object[] clusterTriple = new Object[]{ simIdToClusterId[ sidX ], simIdToClusterId[ sidY ], sim[ sidX ][ sidY ] };
        				similarities.add( TupleFactory.getInstance().newTuple( Arrays.asList( clusterTriple ) ) );
        			}
        		}
        	}

        	Object[] to = new Object[]{ contribDatas, similarities };
	        ret.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
        }
		return ret;
		//bag z: { bag z dane jak na wejsciu aproximate odpowiadające kontrybutorom z poszczegolnych klastrow, bag z triple podobienstwami }
	}
}

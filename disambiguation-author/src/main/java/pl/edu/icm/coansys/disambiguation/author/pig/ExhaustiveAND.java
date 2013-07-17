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
	private PigDisambiguator[] features;
	private List<FeatureInfo> featureInfos; 
	
	public ExhaustiveAND(String threshold, String featureDescription){
		this.threshold = Double.parseDouble(threshold);
        this.featureInfos = FeatureInfo.parseFeatureInfoString(featureDescription);	
        DisambiguatorFactory ff = new DisambiguatorFactory();
        
        int featureNum = 0;
        for(FeatureInfo fi : featureInfos){
        	if(!fi.getDisambiguatorName().equals("")) featureNum++;
        }
        
        this.features = new PigDisambiguator[featureNum];
        
        int index = -1;
        System.out.println("======exhaustiveAnd constructor===========");
        for(FeatureInfo fi : featureInfos){
        	if(fi.getDisambiguatorName().equals("")) continue;
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
				
		if (input == null || input.size() == 0) return null;
		try{
			System.out.println("========ExhaustiveEND.exec========");
			//sname - po co?
			//bag: contribId,pozycja - po co?,sname,mapa: <extractor,bag: tuple ze stringiem>,
			//count - po co?
			DataBag contribs = (DataBag) input.get(1);  //biore bag'a z kontrybutorami
			Iterator<Tuple> it = contribs.iterator();	//iterator po bag'u
			//TODO zmien na liste (np. LinkedList)
			Tuple[] contribsT = new Tuple[(int) contribs.size()]; // tu bedzie zrzucony bag
			List<String> contribsId = new LinkedList<String>(); // UWAGA: tego tu nie wypelniam? sprawdzic!
			int i = 0;
						
			while ( it.hasNext() ) { //iteruje sie po bag'u, zrzucam bag'a do tablicy Tupli
				Tuple t = it.next();
				System.out.println(t);
		        System.out.println("-----------------------------------");				
				contribsT[i] = t;
				i++;
			}
			
			double sim[][] = calculateAffinity ( contribsT, contribsId );
			System.out.println( "sim[][] obliczone ");
			
	        int[] clusterAssociations = new SingleLinkageHACStrategy_OnlyMax().clusterize(sim);
	        Map<Integer,List<String>> clusterMap = splitIntoMap(clusterAssociations, contribsId);
	        
	        System.out.println("==================================");
	        
	        return createResultingTuples(clusterMap, contribsId);
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}
	
	//po co contribsId przekazywane jako argument?
	//po 1. - nie uzywane ponizej
	//po 2. - nie wypelniane w exec
	//po 3. - id kontrybutorow siedzą w contribsT - get(0)
	
	private double[][] calculateAffinity(Tuple[] contribsT, List<String> contribsId) throws Exception {
		
		double[][] sim = new double[contribsT.length][];
		
		//zmienic kolejnosc petli?
		for ( int i = 1; i < contribsT.length; i++ ) {
			sim[i] = new double[i];
			for ( int j = 0; j < i; j++ ) {
				sim[i][j]=threshold;
				for ( int d = 0; d < features.length; d++ ){					
        
					FeatureInfo featureInfo = featureInfos.get(d);
					
					//bo to oznacza, ze nie zostal stworzony obiekt disambiguatora
					//pod features[d]
					if ( featureInfo.getDisambiguatorName().equals("") ) continue;
					
					//pobieranie wartosci (cech) spod danego klucza (nazwy ekstraktora = nazwa cechy)
					//w contribsT[i].get(3) siedzi interesujaca nas mapa
					//de facto Object = DataBag
					Map<String,Object> mA = (Map<String, Object>) contribsT[i].get(3); //biore mape z tupla
					Object oA = mA.get( featureInfo.getFeatureExtractorName() ); //biore Bag wartości danej cechy
					Map<String,Object> mB = (Map<String, Object>) contribsT[j].get(3); //j.w.			
					Object oB = mB.get( featureInfo.getFeatureExtractorName() );
									
					double partial = features[d].calculateAffinity( oA, oB );
					partial = partial / featureInfo.getMaxValue() * featureInfo.getWeight();
					sim[i][j]+=partial;
        			if(sim[i][j]>0) break;
				}
			}
		}
		return sim;
	}
	
	protected Map<Integer, List<String>> splitIntoMap(int[] clusterAssociation, List<String> authorIds) {
		
		Map<Integer, List<String>> clusterMap = new HashMap<Integer, List<String>>();
		
        for (int i = 0; i < clusterAssociation.length; i++) {
            addToMap(clusterMap, clusterAssociation[i], authorIds.get(i));
        }
		return clusterMap;
	}
	
	protected <K, V> void addToMap(Map<Integer, List<String>> clusters, int clusterAssociation, String string) {
        List<String> values = clusters.get(clusterAssociation);
        if (values == null) {
            values = new ArrayList<String>();
            values.add(string);
            clusters.put(clusterAssociation, values);
        }else{
        	values.add(string);
        }
    }
	
	protected DataBag createResultingTuples(Map<Integer, List<String>> clusterMap,
			List<String> authorIds2) {
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

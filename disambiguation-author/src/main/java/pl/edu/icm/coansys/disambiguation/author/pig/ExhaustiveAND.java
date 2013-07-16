package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Bag;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
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
        for(FeatureInfo fi : featureInfos){
        	if(fi.getDisambiguatorName().equals("")) continue;
        	index++;
        	Disambiguator d = ff.create(fi);
        	features[index] = new PigDisambiguator(d);
        	//TODO posortuj dysambiguatory malejaco wg. in wag, np. Email#1.0,ClasifCode#0.7
        }
	}

	/*
	 * Tuple: sname,{(contribId:chararray,contribPos:int,sname:chararray, metadata:map[])} 
	 */
	@Override
	public DataBag exec( Tuple input ) throws IOException {
				
		if (input == null || input.size() == 0) return null;
		try{
			DataBag contribs = (DataBag) input.get(1);
			Iterator<Tuple> it = contribs.iterator();
			Tuple[] contribsT = new Tuple[(int) contribs.size()];//TODO zmien na liste (np. LinkedList)
			List<String> contribsId = new LinkedList<String>();
			int i = 0;
			while(it.hasNext()){
				Tuple t = it.next();;
				System.out.println("====================");
				System.out.println(t);
				System.out.println("====================");
				contribsT[i]=t;
			}
			
			double sim[][] = calculateAffinity(contribsT,contribsId);
			
	        int[] clusterAssociations = new SingleLinkageHACStrategy_OnlyMax().clusterize(sim);
	        Map<Integer,List<String>> clusterMap = splitIntoMap(clusterAssociations, contribsId);
	        return createResultingTuples(clusterMap, contribsId);
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}

	private double[][] calculateAffinity(Tuple[] contribsT, List<String> contribsId) throws Exception {
		
		double[][] sim = new double[contribsT.length][];;
		
		for(int i = 1; i < contribsT.length;i++){
			sim[i] = new double[i];
			for(int j = 0; j<i; j++){
				sim[i][j]=threshold;
				for(int d=0; d<features.length;d++){					
        			FeatureInfo featureInfo = featureInfos.get(d);
					@SuppressWarnings("unchecked")
					Object oA =  ((Map<String,DataBag>) contribsT[i].get(3)).get(featureInfo.getFeatureExtractorName());
					@SuppressWarnings("unchecked")
					Object oB = (DataBag) ((Map<String,DataBag>) contribsT[j].get(3)).get(featureInfo.getFeatureExtractorName());	
					double partial = features[d].calculateAffinity(oA, oB);
					partial = partial/featureInfo.getMaxValue()*featureInfo.getWeight();
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
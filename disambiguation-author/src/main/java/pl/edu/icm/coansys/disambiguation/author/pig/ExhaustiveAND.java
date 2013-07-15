package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	private Disambiguator[] features;
	private List<FeatureInfo> featureInfos;

	public ExhaustiveAND(String threshold, String featureDescription){
		this.threshold = Double.parseDouble(threshold);
        this.featureInfos = FeatureInfo.parseFeatureInfoString(featureDescription);	
        this.features = new Disambiguator[featureInfos.size()];  
        
        DisambiguatorFactory ff = new DisambiguatorFactory();
        int index = -1;
        for(FeatureInfo fi : featureInfos){
        	index++;
        	features[index] = ff.create(fi);
        }
	}

	/*
	 * Tuple: sname,{(sname, contribId,metadata)} 
	 */
	@Override
	public DataBag exec( Tuple input ) throws IOException {
				
		if (input == null || input.size() == 0) return null;
		try{
			Bag contribsB = (Bag) input.get(1);
			Tuple[] contribsT = (Tuple[]) contribsB.toArray();
			List<String> contribsId = new LinkedList<String>();
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

	@SuppressWarnings("null")
	private double[][] calculateAffinity(Tuple[] contribsT, List<String> contribsId) {
		/**
		 * Object[1][4] -- the 5th feature of the 2nd contributor
		 */
		Object[][] contribFeature = new Object[contribsT.length][features.length];
		double[][] sim = null;
		
		
		for(int i = 1; i < contribsT.length;i++){
			sim[i] = new double[i];
			getContribId(contribsT,contribsId);
			for(int j = 0; j<i; j++){
				sim[i][j]=threshold;
				for(int d=0; d<features.length;d++){
					List<String> f1 = getFeature(contribFeature[i][d],contribsT[i]);
					List<String> f2 = getFeature(contribFeature[i][d],contribsT[i]);
					
        			FeatureInfo featureInfo = featureInfos.get(d);
					double partial = features[d].calculateAffinity(f1, f2);
					partial = partial/featureInfo.getMaxValue()*featureInfo.getWeight();
					sim[i][j]+=partial;
        			if(sim[i][j]>0) break;
				}
			}
		}
		return sim;
	}

	private void getContribId(Tuple[] contribsT, List<String> authId) {
		// TODO Auto-generated method stub
		
	}

	private List<String> getFeature(Object object, Tuple tuple) {
		// TODO Auto-generated method stub
		return null;
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
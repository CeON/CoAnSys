/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.author.model.clustering.strategy.CompleteLinkageHACStrategy_OnlyMax;
import pl.edu.icm.coansys.disambiguation.author.model.feature.Disambiguator;
import pl.edu.icm.coansys.disambiguation.author.model.feature.FeatureInfo;
import pl.edu.icm.coansys.disambiguation.author.model.feature.disambiguator.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.author.model.idgenerator.IdGenerator;
import pl.edu.icm.coansys.disambiguation.author.model.idgenerator.UuIdGenerator;
import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.disambiguation.auxil.constants.HBaseConstants;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class ClusterDisambiguationReducer_Toy extends Reducer<Text, TextTextArrayMapWritable, ImmutableBytesWritable, Put> {

	private static Logger logger = Logger.getLogger(LoggingInDisambiguation.class);
	protected String reducerId = new Date().getTime() + "_" + new Random().nextFloat();
	
	protected double threshold;
	protected List<FeatureInfo> featureInfos;
	protected Disambiguator[] features;
    
	protected List<TextTextArrayMapWritable> featuresMapsList = new ArrayList<TextTextArrayMapWritable>();
	protected List<String> authorIds = new ArrayList<String>();
	
	protected Map<Integer, Integer> setSizes = new HashMap<Integer, Integer>();
	protected int sizeCountLimit;

    
    @Override
    public void setup(Context context) throws IOException, InterruptedException {

        logger.setLevel(Level.DEBUG);
        
        Configuration conf = context.getConfiguration();
        
        threshold = Double.parseDouble(conf.getStrings("THRESHOLD")[0]);
        featureInfos = FeatureInfo.parseFeatureInfoString
        	(conf.get("FEATURE_DESCRIPTION"));
        	
        features = new Disambiguator[featureInfos.size()];  
        
        DisambiguatorFactory ff = new DisambiguatorFactory();
        int index = -1;
        for(FeatureInfo fi : featureInfos){
        	index++;
        	features[index] = ff.create(fi);
        }
    }

    @Override
    public void reduce(Text key, Iterable<TextTextArrayMapWritable> values, 
    		Context context) throws IOException, InterruptedException {
    	
    	if(initialPreparations(key, values, context)) return;
        double[][] sim = calculateAffinity();
        int[] clusterAssociations = new CompleteLinkageHACStrategy_OnlyMax().clusterize(sim);
        Map<Integer,List<String>> clusterMap = splitIntoMap(clusterAssociations, authorIds);
        persistReslutsInHBase(clusterMap, authorIds, context);
        finalRoutine();
    }

    
	private boolean initialPreparations(Text key,
			Iterable<TextTextArrayMapWritable> values, Context context) {
		if("".equals(key.toString())){
    		logger.error("Authors with the empty surname field detected! Please investigate this issue!");
    		logger.error("Further calculations are terminated...");
    		return true;
    	}
        logShardProceeding(key);
        prepairFeatureMapsList(key, values);
        gatherAuxiliarData();
        
        if(featuresMapsList.size()==1){
        	String contribId = authorIds.get(0);
        	
        	ArrayList<String> one = new ArrayList<String>(); 
        	one.add(contribId);
        	String clusterId = new UuIdGenerator().genetareId(one);
        	String clusterRowId = HBaseConstants.T_CONTRIBUTIONS_CLUSTER + HBaseConstants.INTRASEPARATOR + clusterId;
            
        	Put cluster_put = new Put(Bytes.toBytes(clusterRowId));
        	cluster_put.add(Bytes.toBytes(HBaseConstants.F_RESULT), Bytes.toBytes(HBaseConstants.Q_CONTRIBS), Bytes.toBytes(contribId));
        	writePutAndLogExceptions(context, clusterId, cluster_put);
        	
        	String contributorRowId = HBaseConstants.T_CONTRIBUTOR + HBaseConstants.INTRASEPARATOR + contribId;
        	Put contributor_put = new Put(Bytes.toBytes(contributorRowId));
            contributor_put.add(Bytes.toBytes(HBaseConstants.F_RESULT),Bytes.toBytes(HBaseConstants.Q_CLUSTER_ID),Bytes.toBytes(clusterId));
        	writePutAndLogExceptions(context, contribId, contributor_put);

        	return true;
        }
        return false;
	}
    
	private void logShardProceeding(Text key) {
		logger.debug("-------------------------------------------");
        logger.debug("Shard: " + key.toString());
	}

	protected void prepairFeatureMapsList(Text key,
			Iterable<TextTextArrayMapWritable> values) {
		featuresMapsList.clear();
		authorIds .clear();
        for (TextTextArrayMapWritable value : values) {
            logger.debug("key=" + key + "\tvalue=" + value);
            featuresMapsList.add(value.copy());
            authorIds.add(value.getString("authId"));
        }
	}
	
	protected void gatherAuxiliarData() {
		int clusterGroupSize = featuresMapsList.size();
        int sizeCount = ((setSizes.get(clusterGroupSize) == null) ? 0 : setSizes.get(clusterGroupSize)) + 1;
        setSizes.put(clusterGroupSize, sizeCount);
	}

	protected double[][] calculateAffinity() {
		double[][] sim = new double[featuresMapsList.size()][];
        for(int i=0;i<featuresMapsList.size();i++){
        	sim[i] = new double[i];
        	for(int j=0;i<j;j++){
        		sim[i][j]=threshold;
        		for(int findex = 0; findex < features.length; findex++){
        			TextTextArrayMapWritable a = featuresMapsList.get(i);
        			TextTextArrayMapWritable b = featuresMapsList.get(j);
        			
        			Disambiguator feature = features[findex];
        			FeatureInfo featureInfo = featureInfos.get(findex);
        			
        			double partial = feature.calculateAffinity(
        					a.getStringList(feature.getName()),
        					b.getStringList(feature.getName()));
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
    
	protected void persistReslutsInHBase(Map<Integer, List<String>> clusterMap,
			List<String> authorIds2, Context context) {
    	IdGenerator idgenerator = new UuIdGenerator();
        for (Map.Entry<Integer, List<String>> o : clusterMap.entrySet()) {
        	String clusterId = idgenerator.genetareId(o.getValue());
        	String rowId = HBaseConstants.T_CONTRIBUTIONS_CLUSTER + HBaseConstants.INTRASEPARATOR + clusterId;
            Put cluster_put = new Put(Bytes.toBytes(rowId));
        	
            for (String contribId : o.getValue()) {
            	cluster_put.add(Bytes.toBytes(HBaseConstants.F_RESULT), Bytes.toBytes(HBaseConstants.Q_CONTRIBS), 
            			Bytes.toBytes(HBaseConstants.T_CONTRIBUTIONS_CLUSTER + HBaseConstants.INTRASEPARATOR + contribId));
            	
            	Put contributor_put = new Put(Bytes.toBytes(HBaseConstants.T_CONTRIBUTOR + HBaseConstants.INTRASEPARATOR + contribId));
            	contributor_put.add(Bytes.toBytes(HBaseConstants.F_RESULT),Bytes.toBytes(HBaseConstants.Q_CLUSTER_ID),
            			Bytes.toBytes(HBaseConstants.T_CONTRIBUTIONS_CLUSTER + HBaseConstants.INTRASEPARATOR + clusterId));
            	writePutAndLogExceptions(context, contribId, contributor_put);
            }
            writePutAndLogExceptions(context, clusterId, cluster_put);
        }
		
	}

	protected void writePutAndLogExceptions(Context context, String rowId, Put put) {
		try {
			context.write(null, put);
		} catch (IOException e) {
			logger.debug("During persisting the cluster "+rowId+" following IOException occured:");
			logger.debug(e);
		} catch (InterruptedException e) {
			logger.debug("During persisting the cluster "+rowId+" following InterruptedException occured:");
			logger.debug(e);
		}
	}

	protected void finalRoutine(Object... objects) {
//      long clustering_start = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//      long clustering_end = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//      if (clusterMap.size() < featuresMapsList.size()) {
//          logger.debug("===  Disambiguation found in " + key + " from " + featuresMapsList.size() + " to " + clusterMap.size());
//      }
//      logger.debug("=== Persisting group " + key + " of size " + featuresMapsList.size() + " took " + (clustering_end - clustering_start));
//      logger.debug("-------------------------------------------");
	}

	@Override
    public void cleanup(Context context) throws IOException, InterruptedException {
        logger.debug("=== Set sizes stats ===");
        logger.debug("-------------------------------------------");
        for (Entry<Integer, Integer> kv : setSizes.entrySet()) {
            logger.debug("=== Reducer: " + reducerId + " setSize=" + kv.getKey() + " count=" + kv.getValue());
        }
    }
}

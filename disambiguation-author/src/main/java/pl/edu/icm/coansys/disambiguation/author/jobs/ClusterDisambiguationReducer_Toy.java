/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.author.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.author.constants.HBaseConstants;
import pl.edu.icm.coansys.disambiguation.author.features.disambiguators.DisambiguatorFactory;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.disambiguation.clustering.strategies.SingleLinkageHACStrategy_OnlyMax;
import pl.edu.icm.coansys.disambiguation.features.Disambiguator;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;
import pl.edu.icm.coansys.disambiguation.idgenerators.IdGenerator;
import pl.edu.icm.coansys.disambiguation.idgenerators.UuIdGenerator;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class ClusterDisambiguationReducer_Toy extends Reducer<Text, TextTextArrayMapWritable, ImmutableBytesWritable, Put> {

	private static Logger logger = LoggerFactory.getLogger(ClusterDisambiguationReducer_Toy.class);
	private String reducerId = new Date().getTime() + "_" + Math.random();
	
	private float threshold;
	private List<FeatureInfo> featureInfos;
	private Disambiguator[] features;
    
	private List<TextTextArrayMapWritable> featuresMapsList = new ArrayList<TextTextArrayMapWritable>();
	private List<String> authorIds = new ArrayList<String>();
	
	private Map<Integer, Integer> setSizes = new HashMap<Integer, Integer>();
    
    @Override
    public void setup(Context context) throws IOException, InterruptedException {

        Configuration conf = context.getConfiguration();
        
        threshold = Float.parseFloat(conf.getStrings("THRESHOLD")[0]);
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
        float[][] sim = calculateAffinity();
        int[] clusterAssociations = new SingleLinkageHACStrategy_OnlyMax().clusterize(sim);
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
        
        logger.debug("Reduced size: "+featuresMapsList.size());
        
        if(featuresMapsList.size()==1){
        	String contribId = authorIds.get(0);
        	
        	List<String> one = new ArrayList<String>(); 
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

	protected float[][] calculateAffinity() {
		float[][] sim = new float[featuresMapsList.size()][];
        for(int i=1;i<featuresMapsList.size();i++){
        	sim[i] = new float[i];
        	for(int j=0;i<j;j++){
        		sim[i][j]=threshold;
        		for(int findex = 0; findex < features.length; findex++){
        			TextTextArrayMapWritable a = featuresMapsList.get(i);
        			TextTextArrayMapWritable b = featuresMapsList.get(j);
        			
        			Disambiguator feature = features[findex];
        			FeatureInfo featureInfo = featureInfos.get(findex);
        			
        			List <Object> f1 = new LinkedList <Object>();
        			List <Object> f2 = new LinkedList <Object>();

        			for ( String str : a.getStringList(feature.getName()) ) 
        				f1.add(str);

        			for ( String str : b.getStringList(feature.getName()) ) 
        				f2.add(str);

        			double partial = feature.calculateAffinity( f1, f2 );
        			
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
			logger.debug("During persisting the cluster "+rowId+" following IOException occured: " + e);
		} catch (InterruptedException e) {
			logger.debug("During persisting the cluster "+rowId+" following InterruptedException occured: " + e);
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

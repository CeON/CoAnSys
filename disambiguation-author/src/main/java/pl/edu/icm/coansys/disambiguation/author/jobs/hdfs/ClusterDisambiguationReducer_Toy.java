/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.author.jobs.hdfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
public class ClusterDisambiguationReducer_Toy extends Reducer<Text, TextTextArrayMapWritable, Text, Text> {

    private static Logger logger = Logger.getLogger(ClusterDisambiguationReducer_Toy.class);
    private String reducerId = new Date().getTime() + "_" + Math.random();
    private float threshold;
    private List<FeatureInfo> featureInfos;
    private Disambiguator[] features;
    private List<TextTextArrayMapWritable> featuresMapsList = new ArrayList<TextTextArrayMapWritable>();
    private List<String> authorIds = new ArrayList<String>();
    private Map<Integer, Integer> setSizes = new HashMap<Integer, Integer>();
    private Text contribIdText = new Text();
    private Text clusterIdText = new Text();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {

        logger.setLevel(Level.DEBUG);

        Configuration conf = context.getConfiguration();

        threshold = Float.parseFloat(conf.getStrings("THRESHOLD")[0]);
        featureInfos = FeatureInfo.parseFeatureInfoString(conf.get("FEATURE_DESCRIPTION"));

        features = new Disambiguator[featureInfos.size()];

        DisambiguatorFactory ff = new DisambiguatorFactory();
        int index = -1;
        for (FeatureInfo fi : featureInfos) {
            index++;
            features[index] = ff.create(fi);
        }
    }

    @Override
    public void reduce(Text key, Iterable<TextTextArrayMapWritable> values,
            Context context) throws IOException, InterruptedException {

        if (initialPreparations(key, values, context)) {
            return;
        }
        float[][] sim = calculateAffinity();
        int[] clusterAssociations = new SingleLinkageHACStrategy_OnlyMax().clusterize(sim);
        Map<Integer, List<String>> clusterMap = splitIntoMap(clusterAssociations, authorIds);
        persistReslutsInHBase(clusterMap, authorIds, context);
        finalRoutine();
    }
    
    // usage in reduce
    private boolean initialPreparations(Text key,
            Iterable<TextTextArrayMapWritable> values, Context context) {
        if ("".equals(key.toString())) {
            logger.error("Authors with the empty surname field detected! Please investigate this issue!");
            logger.error("Further calculations are terminated...");
            return true;
        }
        logShardProceeding(key);
        prepairFeatureMapsList(key, values);
        gatherAuxiliarData();

        logger.debug("Reduced size: " + featuresMapsList.size());

        if (featuresMapsList.size() == 1) {
            String contribId = authorIds.get(0);

            List<String> one = new ArrayList<String>();
            one.add(contribId);
            String clusterId = new UuIdGenerator().genetareId(one);

            clusterIdText.set(clusterId);
            contribIdText.set(contribId);
            writeAndLogExceptions(context, contribIdText, clusterIdText);

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
        authorIds.clear();
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
    
    //usage in reduce
    protected float[][] calculateAffinity() {
        float[][] sim = new float[featuresMapsList.size()][];
        for (int i = 1; i < featuresMapsList.size(); i++) {
            sim[i] = new float[i];
            for (int j = 0; i < j; j++) {
                sim[i][j] = threshold;
                for (int findex = 0; findex < features.length; findex++) {
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

                    partial = partial / featureInfo.getMaxValue() * featureInfo.getWeight();
                    sim[i][j] += partial;
                    if (sim[i][j] > 0) {
                        break;
                    }
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
        } else {
            values.add(string);
        }
    }

    protected void persistReslutsInHBase(Map<Integer, List<String>> clusterMap,
            List<String> authorIds2, Context context) {
        IdGenerator idgenerator = new UuIdGenerator();
        for (Map.Entry<Integer, List<String>> o : clusterMap.entrySet()) {
            String clusterId = idgenerator.genetareId(o.getValue());

            for (String contribId : o.getValue()) {
                clusterIdText.set(clusterId);
                contribIdText.set(contribId);
                writeAndLogExceptions(context, contribIdText, clusterIdText);
            }
        }

    }

    protected void writeAndLogExceptions(Context context, Text contribId, Text clusterId) {
        try {
            context.write(contribId, clusterId);
        } catch (IOException e) {
            logger.debug("During persisting the cluster " + contribId.toString() + "  - " + clusterId.toString() + " following IOException occured:");
            logger.debug(e);
        } catch (InterruptedException e) {
            logger.debug("During persisting the cluster " + contribId.toString() + "  - " + clusterId.toString() + " following InterruptedException occured:");
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

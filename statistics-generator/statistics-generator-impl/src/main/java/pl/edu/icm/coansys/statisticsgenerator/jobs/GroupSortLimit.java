/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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
package pl.edu.icm.coansys.statisticsgenerator.jobs;

import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.coansys.statisticsgenerator.conf.ConfigurationConstants;
import pl.edu.icm.coansys.statisticsgenerator.conf.StatGeneratorConfiguration;
import pl.edu.icm.coansys.statisticsgenerator.mrtypes.SortedMapWritableComparable;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class GroupSortLimit implements Tool {

    private static Logger logger = LoggerFactory.getLogger(GroupSortLimit.class);
    private Configuration conf;

    public static class GroupSortLimitMap extends Mapper<Text, BytesWritable, SortedMapWritableComparable, BytesWritable> {

        private StatGeneratorConfiguration statGenConfiguration;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            statGenConfiguration = new StatGeneratorConfiguration(conf);
        }

        @Override
        protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
            StatisticsProtos.Statistics statistic = StatisticsProtos.Statistics.parseFrom(value.copyBytes());
            SortedMapWritableComparable outputKeyMap = new SortedMapWritableComparable();
            List<String> groupKeys = Arrays.asList(statGenConfiguration.getGroupKeys());

            for (StatisticsProtos.KeyValue kv : statistic.getPartitionsList()) {
                if (groupKeys.contains(kv.getKey())) {
                    outputKeyMap.put(new Text(kv.getKey()), new Text(kv.getValue()));
                }
            }

            context.write(outputKeyMap, value);
        }
    }

    public static class GroupSortLimitCombine extends Reducer<SortedMapWritableComparable, BytesWritable, SortedMapWritableComparable, BytesWritable> {

        private StatGeneratorConfiguration statGenConfiguration;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            statGenConfiguration = new StatGeneratorConfiguration(conf);
        }

        @Override
        protected void reduce(SortedMapWritableComparable key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
            List<StatisticsProtos.Statistics> sortedLimited = sortLimit(values, statGenConfiguration);
            for (StatisticsProtos.Statistics st : sortedLimited) {
                context.write(key, new BytesWritable(st.toByteArray()));
            }
        }
    }

    public static class GroupSortLimitReduce extends Reducer<SortedMapWritableComparable, BytesWritable, Text, BytesWritable> {

        private StatGeneratorConfiguration statGenConfiguration;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            statGenConfiguration = new StatGeneratorConfiguration(conf);
        }

        @Override
        protected void reduce(SortedMapWritableComparable key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
            List<StatisticsProtos.Statistics> sortedLimited = sortLimit(values, statGenConfiguration);

            StatisticsProtos.SelectedStatistics.Builder valueBuilder = StatisticsProtos.SelectedStatistics.newBuilder();
            valueBuilder.setSelectedStat(statGenConfiguration.getSortStat());
            valueBuilder.setSortOrder(statGenConfiguration.getSortOrder());
            valueBuilder.setLimit(statGenConfiguration.getLimit());
            
            StatisticsProtos.KeyValue.Builder kvBuilder = StatisticsProtos.KeyValue.newBuilder();
            for (WritableComparable fixedPartition : key.keySet()) {
                kvBuilder.clear();
                String partName = ((Text) fixedPartition).toString();
                String partValue = ((Text) key.get(fixedPartition)).toString();
                kvBuilder.setKey(partName);
                kvBuilder.setValue(partValue);
                valueBuilder.addFixedPartitions(kvBuilder);
            }
            valueBuilder.addAllStats(sortedLimited);
            
            
            context.write(new Text(genStringFromMap(key)), new BytesWritable(valueBuilder.build().toByteArray()));
        }
    }

    private static List<StatisticsProtos.Statistics> sortLimit(Iterable<BytesWritable> values, StatGeneratorConfiguration statGeneratorConfiguration) throws InvalidProtocolBufferException {
        String sortStat = statGeneratorConfiguration.getSortStat();
        String sortOrder = statGeneratorConfiguration.getSortOrder();
        int limit = statGeneratorConfiguration.getLimit();

        List<StatisticsProtos.Statistics> result = new ArrayList<StatisticsProtos.Statistics>();

        SortedMap<Double, List<StatisticsProtos.Statistics>> buffer = new TreeMap<Double, List<StatisticsProtos.Statistics>>();
        int counter = 0;

        for (BytesWritable value : values) {

            StatisticsProtos.Statistics st = StatisticsProtos.Statistics.parseFrom(value.copyBytes());
            Double statValue = null;

            for (StatisticsProtos.KeyValue kv : st.getStatisticsList()) {
                if (kv.getKey().equals(sortStat)) {
                    statValue = Double.parseDouble(kv.getValue());
                    break;
                }
            }

            if (statValue == null) {
                break;
            }

            List<StatisticsProtos.Statistics> listToAdd;
            if (!buffer.containsKey(statValue)) {
                listToAdd = new ArrayList<StatisticsProtos.Statistics>();
                buffer.put(statValue, listToAdd);
            } else {
                listToAdd = buffer.get(statValue);
            }
            listToAdd.add(st);
            counter++;


            if (counter > limit) {
                Double keyToRemove;
                if (sortOrder.equals(ConfigurationConstants.SORT_ASC)) {
                    keyToRemove = buffer.lastKey();
                } else {
                    keyToRemove = buffer.firstKey();
                }
                
                List<StatisticsProtos.Statistics> firstList = buffer.get(keyToRemove);
                if (firstList.size() == 1) {
                    buffer.remove(keyToRemove);
                } else {
                    firstList.remove(0);
                }
                counter--;
            }

        }
        if (counter > 0) {
            List<Double> keyList = new ArrayList<Double>(buffer.keySet());
            
            for (int i = 0; i < keyList.size(); i++) {
                Double key;
                if (sortOrder.equals(ConfigurationConstants.SORT_ASC)) {
                    key = keyList.get(i);
                } else {
                    key = keyList.get(keyList.size() - i - 1);
                }
                
                for (StatisticsProtos.Statistics st : buffer.get(key)) {
                    result.add(st);
                }
                
            }
        }
        return result;
    }
    
    private static String genStringFromMap(Map map) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object key : map.keySet()) {
            if (!first) {
                sb.append("#");
                first = false;
            }
            sb.append(key).append("=").append(map.get(key));
        }
        return sb.toString();
    }

    @Override
    public int run(String[] args) throws Exception {
        Job job = new Job(conf);
        job.setJarByClass(StatisticsGenerator.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setMapperClass(GroupSortLimitMap.class);
        job.setCombinerClass(GroupSortLimitCombine.class);
        job.setReducerClass(GroupSortLimitReduce.class);
        job.setMapOutputKeyClass(SortedMapWritableComparable.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setNumReduceTasks(1);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);

        long startTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        boolean success = job.waitForCompletion(true);
        long endTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        double duration = (endTime - startTime) / Math.pow(10, 9);
        logger.info("=== Job Finished in " + duration + " seconds " + (success ? "(success)" : "(failure)"));
        return success ? 0 : 1;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("syntax: GroupSortLimit <input_path> <output_path>");
        }
        ToolRunner.run(new StatisticsGenerator(), args);
    }
}

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

package pl.edu.icm.coansys.statisticsgenerator.jobs;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
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
import pl.edu.icm.coansys.statisticsgenerator.mrtypes.SortedMapWritableComparable;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.Partitioner;
import pl.edu.icm.coansys.statisticsgenerator.conf.StatGeneratorConfiguration;
import pl.edu.icm.coansys.statisticsgenerator.operationcomponents.StatisticCalculator;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class StatisticsGenerator implements Tool {

    private static Logger logger = LoggerFactory.getLogger(StatisticsGenerator.class);
    private Configuration conf;
        
    public static class StatisticsMap extends Mapper<Text, BytesWritable, SortedMapWritableComparable, BytesWritable> {
        
        private StatGeneratorConfiguration statGenConfiguration;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            statGenConfiguration = new StatGeneratorConfiguration(conf);
        }

        @Override
        protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {

            StatisticsProtos.InputEntry inputEntry = StatisticsProtos.InputEntry.parseFrom(value.copyBytes());
            SortedMapWritableComparable outputKeyMap = new SortedMapWritableComparable();
            Map<String, Partitioner> partitioners = statGenConfiguration.getPartitioners();
            
            for (StatisticsProtos.KeyValue field : inputEntry.getFieldList()) {
                String fieldKey = field.getKey();
                if (partitioners.containsKey(fieldKey)) {
                    String partition = partitioners.get(fieldKey).partition(field.getValue());
                    outputKeyMap.put(new Text(fieldKey), new Text(partition));
                }
            }

            context.write(outputKeyMap, value);
        }
    }

    
    public static class StatisticsReduce extends Reducer<SortedMapWritableComparable, BytesWritable, Text, BytesWritable> {
        
        private StatGeneratorConfiguration statGenConfiguration;
        
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            statGenConfiguration = new StatGeneratorConfiguration(conf);
        }
        
        @Override
        protected void reduce(SortedMapWritableComparable key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
            StatisticsProtos.Statistics.Builder statisticsBuilder = StatisticsProtos.Statistics.newBuilder();
            StatisticsProtos.KeyValue.Builder kvBuilder = StatisticsProtos.KeyValue.newBuilder();
            
            for (SortedMapWritableComparable.Entry<WritableComparable, Writable> partEntry : key.entrySet()) {
                kvBuilder.clear();
                Text kText = (Text) partEntry.getKey();
                Text vText = (Text) partEntry.getValue();
                kvBuilder.setKey(kText.toString());
                kvBuilder.setValue(vText.toString());
                statisticsBuilder.addPartitions(kvBuilder);
            }
            for (Map.Entry<String, StatisticCalculator> statisticEntry : this.statGenConfiguration.getStatisticCalculators().entrySet()) {
                kvBuilder.clear();
                double result = statisticEntry.getValue().calculate(values);
                kvBuilder.setKey(statisticEntry.getKey());
                kvBuilder.setValue(String.valueOf(result));
                statisticsBuilder.addStatistics(kvBuilder);
            }
            statisticsBuilder.setTimestamp(new Date().getTime());
            context.write(new Text("abc"), new BytesWritable(statisticsBuilder.build().toByteArray()));
        }
    }
    
    

    @Override
    public int run(String[] args) throws Exception {
        Job job = new Job(conf);
        job.setJarByClass(StatisticsGenerator.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setMapperClass(StatisticsMap.class);
        job.setReducerClass(StatisticsReduce.class);
        job.setMapOutputKeyClass(SortedMapWritableComparable.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setNumReduceTasks(16);
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
        return this.conf;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("syntax: StatisticsGenerator <input_path> <output_path>");
        }
        ToolRunner.run(new StatisticsGenerator(), args);
    }
}

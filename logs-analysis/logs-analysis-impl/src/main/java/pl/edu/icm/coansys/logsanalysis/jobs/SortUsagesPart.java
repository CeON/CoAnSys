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

package pl.edu.icm.coansys.logsanalysis.jobs;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.MostPopularProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class SortUsagesPart implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(SortUsagesPart.class);
    private Configuration conf;

    public static class SorterMap extends Mapper<Text, LongWritable, NullWritable, Text> {

        @Override
        protected void map(Text key, LongWritable value, Context context) throws IOException, InterruptedException {
            String valueStr = "" + value.get() + ":" + key.toString();
            context.write(NullWritable.get(), new Text(valueStr));
        }
    }
    
    public static class SorterCombine extends Reducer<NullWritable, Text, NullWritable, Text> {

        @Override
        protected void reduce(NullWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            int nbOfRecords = Integer.parseInt(conf.get("NB_OF_RECORDS"));
            for (AbstractMap.SimpleEntry<Long, String> entry: findMostPopular(values, nbOfRecords)) {
                String valueStr = "" + entry.getKey() + ":" + entry.getValue();
                context.write(NullWritable.get(), new Text(valueStr));
            }
        }
        
    }

    public static class SorterReduce extends Reducer<NullWritable, Text, NullWritable, BytesWritable> {

        @Override
        protected void reduce(NullWritable nullKey, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            Long timeInMilis;
            try {
                timeInMilis = new SimpleDateFormat("yyyy-MM-dd").parse(conf.get("RESULT_DATE")).getTime();
            } catch (Exception ex) {
                timeInMilis = System.currentTimeMillis();
            }

            int nbOfRecords = Integer.parseInt(conf.get("NB_OF_RECORDS"));
            
            List<AbstractMap.SimpleEntry<Long, String>> mostPopular = findMostPopular(values, nbOfRecords);
            
            if (mostPopular.size() > 0) {
                MostPopularProtos.MostPopularStats.Builder statsBuilder = MostPopularProtos.MostPopularStats.newBuilder();
                statsBuilder.setTimestamp(timeInMilis);
                
                
                for (AbstractMap.SimpleEntry<Long, String> entry: mostPopular) {
                    long n = entry.getKey();
                    String resource = entry.getValue();
                    MostPopularProtos.ResourceStat.Builder resStatBuilder = MostPopularProtos.ResourceStat.newBuilder();
                    resStatBuilder.setResourceId(resource);
                    resStatBuilder.setCounter(n);
                    statsBuilder.addStat(resStatBuilder);
                }
                
                BytesWritable bw = new BytesWritable(statsBuilder.build().toByteArray());
                context.write(NullWritable.get(), bw);
            }
        }
    }

    private static List<AbstractMap.SimpleEntry<Long, String>> findMostPopular(Iterable<Text> values, int nbOfRecords) {
        
        List<AbstractMap.SimpleEntry<Long, String>> result = new ArrayList<AbstractMap.SimpleEntry<Long, String>>();
        
        SortedMap<Long, List<String>> buffer = new TreeMap<Long, List<String>>();
        int counter = 0;
        long minValue = Long.MAX_VALUE;

        for (Text value : values) {
            //value contains counter and resource_id
            Pattern pattern = Pattern.compile("^(\\d+):(.*)$");
            Matcher matcher = pattern.matcher(value.toString());
            if (matcher.find()) {
                Long nb = Long.parseLong(matcher.group(1));
                String id = matcher.group(2);

                if (counter < nbOfRecords || nb > minValue) {

                    if (nb < minValue) {
                        minValue = nb;
                    }

                    List<String> listToAdd;
                    if (!buffer.containsKey(nb)) {
                        listToAdd = new ArrayList<String>();
                        buffer.put(nb, listToAdd);
                    } else {
                        listToAdd = buffer.get(nb);
                    }
                    listToAdd.add(id);
                    counter++;
                    if (counter > nbOfRecords) {
                        Long firstKey = buffer.firstKey();
                        List<String> firstList = buffer.get(firstKey);
                        if (firstList.size() == 1) {
                            buffer.remove(firstKey);
                            minValue = buffer.firstKey();
                        } else {
                            firstList.remove(0);
                            minValue = firstKey;
                        }
                        counter--;
                    }
                }

            }
        }
        if (counter > 0) {
            //reverse order of usage counters using a stack
            Stack<Long> countersStack = new Stack<Long>();
            for (Long n : buffer.keySet()) {
                countersStack.push(n);
            }
            while (!countersStack.empty()) {
                long n = countersStack.pop();
                for (String resource : buffer.get(n)) {
                    result.add(new AbstractMap.SimpleEntry<Long, String>(Long.valueOf(n), resource));
                }
            }
        }
        return result;
    }

    @Override
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        if (args.length < 3) {
            logger.error("Usage: SortUsagePart <input_dir> <output_dir> <nb_of_records>");
            return 1;
        }

        conf.set("NB_OF_RECORDS", args[2]);

        Job job = new Job(conf);
        job.setJarByClass(SortUsagesPart.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(BytesWritable.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setMapperClass(SorterMap.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setCombinerClass(SorterCombine.class);
        job.setReducerClass(SorterReduce.class);

        /*
         * Launch job
         */
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
        System.exit(ToolRunner.run(new SortUsagesPart(), args));
    }
}

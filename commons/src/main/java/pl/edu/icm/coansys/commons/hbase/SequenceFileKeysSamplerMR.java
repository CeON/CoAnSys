/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.hbase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class SequenceFileKeysSamplerMR implements Tool {

    private Configuration conf;
    private static final String SAMPLE_SAMPLES_TOTAL_COUNT = "sampler.samples.region.count";
    private static final int SAMPLE_SAMPLES_TOTAL_COUNT_DV = 20;
    private static final String SAMPLE_SAMPLES_PER_SPLIT = "sampler.samples.per.split";
    private static final int SAMPLE_SAMPLES_PER_SPLIT_DV = 100;
    private static final String[] DEFAULT_ARGS = {"/home/akawa/bwndata/sf/", "output-keys"};

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    public static class Map extends Mapper<BytesWritable, BytesWritable, BooleanWritable, BytesWritable> {

        private int count = 0;
        private int limit = SAMPLE_SAMPLES_PER_SPLIT_DV;
        private static final BooleanWritable TRUE = new BooleanWritable(true);

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration config = context.getConfiguration();
            limit = config.getInt(SAMPLE_SAMPLES_PER_SPLIT, SAMPLE_SAMPLES_PER_SPLIT_DV);
        }

        @Override
        public void run(Context context) throws IOException, InterruptedException {
            setup(context);
            while ((count < limit) && context.nextKeyValue()) {
                map(context.getCurrentKey(), context.getCurrentValue(), context);
                count++;
            }
            cleanup(context);
        }

        @Override
        public void map(BytesWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
            context.write(TRUE, key);
        }
    }

    public static class Reduce extends Reducer<BooleanWritable, BytesWritable, Text, NullWritable> {

        private int samplesLimit = SAMPLE_SAMPLES_TOTAL_COUNT_DV;
        private Text rangeKey = new Text();
        private static final NullWritable NULL = NullWritable.get();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration config = context.getConfiguration();
            samplesLimit = config.getInt(SAMPLE_SAMPLES_TOTAL_COUNT, SAMPLE_SAMPLES_TOTAL_COUNT_DV);
        }

        @Override
        public void reduce(BooleanWritable key, Iterable<BytesWritable> samples, Context context) throws IOException, InterruptedException {

            List<String> samplesString = getStringList(samples);
            Collections.sort(samplesString);

            float stepSize = samplesString.size() / (float) samplesLimit;
            List<String> intervaleSamples = getIntervalSamples(samplesString, stepSize, samplesLimit);

            for (String regionKey : intervaleSamples) {
                rangeKey.set(regionKey);
                context.write(rangeKey, NULL);
            }
        }
        
        public List<String> getStringList(Iterable<BytesWritable> samples) {
             List<String> samplesString = new ArrayList<String>();
            for (BytesWritable val : samples) {
                samplesString.add(Bytes.toString(val.copyBytes()));
            }
            return samplesString;
        }

        public List<String> getIntervalSamples(List<String> longerList, float stepSize, int sampleLimit) {
            List<String> sampledList = new ArrayList<String>();
            int last = -1;
            for (int i = 1; i < Math.min(sampleLimit, longerList.size()); ++i) {
                int k = Math.round(stepSize * i);
                while (last >= k && longerList.get(last).equals(longerList.get(k))) {
                    ++k;
                }
                sampledList.add(longerList.get(k));
                last = k;
            }

            return sampledList;
        }
    }

    @Override
    public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        if (args.length < 2) {
            usage("Wrong number of arguments: " + args.length);
            ToolRunner.printGenericCommandUsage(System.err);
            return(-1);
        }

        return createParitionFile(args[0], args[1]);
    }

    private int createParitionFile(String sequenceFileInput, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Path input = new Path(sequenceFileInput);
        Job sampler = new Job(getConf());

        sampler.setNumReduceTasks(1);
        sampler.setInputFormatClass(SequenceFileInputFormat.class);
        sampler.setOutputFormatClass(TextOutputFormat.class);
        
        sampler.setMapOutputKeyClass(BooleanWritable.class);
        sampler.setMapOutputValueClass(BytesWritable.class);
        sampler.setOutputKeyClass(BytesWritable.class);
        sampler.setOutputValueClass(NullWritable.class);

        sampler.setMapperClass(Map.class);
        sampler.setReducerClass(Reduce.class);
        SequenceFileInputFormat.addInputPath(sampler, input);
        
        FileOutputFormat.setOutputPath(sampler, new Path(outputPath));

        sampler.waitForCompletion(true);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        String[] arguments = new String[2];
        if (args.length == 0) {
            arguments[0] = DEFAULT_ARGS[0];
            arguments[1] = DEFAULT_ARGS[1];
            FileUtils.deleteDirectory(new File(arguments[1]));
        }
        else if (args.length >= 2) {
            arguments[0] = args[0];
            arguments[1] = args[1];            
        }
        int result = ToolRunner.run(new SequenceFileKeysSamplerMR(), arguments);
        System.exit(result);
    }

    private static void usage(String info) {
        System.err.println(info);
        System.err.println("Two parameters needed: <sequence-file-input> <output-path>");
        System.err.println("Example: hadoop jar target/commons-1.0-SNAPSHOT.jar " + SequenceFileKeysSamplerMR.class.getName() + " bazekon-20120228.sf sf-split");
    }
}
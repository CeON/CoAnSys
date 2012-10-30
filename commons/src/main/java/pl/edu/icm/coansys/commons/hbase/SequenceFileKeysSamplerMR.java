package pl.edu.icm.coansys.commons.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class SequenceFileKeysSamplerMR implements Tool {

    private Configuration conf;
    private static final String SAMPLE_SAMPLES_TOTAL_COUNT = "sampler.samples.total.count";
    private static final int SAMPLE_SAMPLES_TOTAL_COUNT_DV = 20;
    private static final String SAMPLE_SAMPLES_PER_SPLIT = "sampler.samples.per.split";
    private static final int SAMPLE_SAMPLES_PER_SPLIT_DV = 3;

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

            List<String> stringSamples = new ArrayList<String>();
            for (BytesWritable val : samples) {
                stringSamples.add(Bytes.toString(val.copyBytes()));
            }
            Collections.sort(stringSamples);
            
            float stepSize = stringSamples.size() / (float) samplesLimit;
            int last = -1;
            for (int i = 1; i < samplesLimit; ++i) {
                int k = Math.round(stepSize * i);
                while (last >= k && stringSamples.get(last).equals(stringSamples.get(k))) {
                    ++k;
                }

                rangeKey.set(stringSamples.get(i));
                context.write(rangeKey, NULL);
                last = k;
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        int result = createParitionFile(args[0], args[1]);
        return result;
    }

    private int createParitionFile(String sequenceFileInput, String outputPath) throws IOException, ClassNotFoundException, InterruptedException {
        Path input = new Path(sequenceFileInput);
        Job sampler = new Job(getConf());

        sampler.setNumReduceTasks(1);
        sampler.setInputFormatClass(SequenceFileInputFormat.class);
        sampler.setOutputFormatClass(TextOutputFormat.class);
        sampler.setMapOutputKeyClass(BooleanWritable.class);
        sampler.setMapOutputValueClass(BytesWritable.class);
        sampler.setOutputKeyClass(Text.class);
        sampler.setOutputValueClass(NullWritable.class);

        sampler.setMapperClass(Map.class);
        sampler.setReducerClass(Reduce.class);
        SequenceFileInputFormat.addInputPath(sampler, input);
        FileOutputFormat.setOutputPath(sampler, new Path(outputPath));

        sampler.waitForCompletion(true);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            usage("Wrong number of arguments: " + otherArgs.length);
            System.exit(-1);
        }

        int result = ToolRunner.run(conf, new SequenceFileKeysSamplerMR(), otherArgs);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Two parameters needed: <sequence-file-input> <output-path>");
        System.out.println("Example: hadoop jar target/commons-1.0-SNAPSHOT.jar " + SequenceFileKeysSamplerMR.class.getName() + " bazekon-20120228.sf 20 sf-split");
    }
}
// hadoop jar target/commons-1.0-SNAPSHOT.jar pl.edu.icm.coansys.commons.hbase.SequenceFileKeysSamplerMR bazekon-20120228.sf 20 sf-split
// export HBASE_CLASSPATH=target/commons-1.0-SNAPSHOT.jar
// hbase org.apache.hadoop.hbase.util.RegionSplitter -D split.algorithm=pl.edu.icm.coansys.commons.hbase.SequenceFileSplitAlgorithm -c 2 -f c:m test_table1
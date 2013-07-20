package pl.edu.icm.coansys.commons.hadoop;

import java.io.IOException;
import java.util.Random;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class SamplerByInputSplit implements Tool {

    private Configuration conf;
    private static final String SAMPLE_FREQUENCY = "sampler.frequency";
    private static final float SAMPLE_FREQUENCY_DV = 0.01f;

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    public static class Map extends Mapper<LongWritable, Text, Text, NullWritable> {

        private int count = 0;
        private float fequency = SAMPLE_FREQUENCY_DV;
        private static final NullWritable NULL = NullWritable.get();
        private Random random = new Random();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration config = context.getConfiguration();
            fequency = config.getFloat(SAMPLE_FREQUENCY, SAMPLE_FREQUENCY_DV);
        }

        @Override
        public void run(Context context) throws IOException, InterruptedException {
            setup(context);
            while ((count < fequency) && context.nextKeyValue()) {
                float diff = fequency - count;
                if (random.nextFloat() <= diff) {
                    map(context.getCurrentKey(), context.getCurrentValue(), context);
                }
                count++;
            }
            cleanup(context);
        }

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value, NULL);
        }
    }

    @Override
    public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        return createParitionFile(args[0], args[1], Float.parseFloat(args[2]));
    }

    private int createParitionFile(String sequenceFileInput, String outputPath, float frequency) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration config = getConf();
        config.setFloat(SAMPLE_FREQUENCY, frequency);
        Job sampler = new Job(config);

        sampler.setInputFormatClass(TextInputFormat.class);
        sampler.setOutputFormatClass(TextOutputFormat.class);
        sampler.setOutputKeyClass(Text.class);
        sampler.setOutputValueClass(NullWritable.class);
        sampler.setNumReduceTasks(0);

        sampler.setMapperClass(Map.class);
        TextInputFormat.addInputPath(sampler, new Path(sequenceFileInput));
        TextOutputFormat.setOutputPath(sampler, new Path(outputPath));

        sampler.waitForCompletion(true);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 3) {
            usage("Wrong number of arguments: " + otherArgs.length);
            System.exit(-1);
        }

        int result = ToolRunner.run(conf, new SamplerByInputSplit(), otherArgs);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Three parameters needed: <input-path, output-file, frequency>");
        System.out.println("Example: hadoop jar target/commons-1.0-SNAPSHOT.jar " + SamplerByInputSplit.class.getName() + "...");
    }
}
/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.hbase;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class SequenceFileKeysSampler implements Tool {

    private Configuration conf;
    private static final String OUTPUT_KEYS_FILE_NAME_KEY = "output.keys.file.name";
    private static final String OUTPUT_KEYS_FILE_NAME_DEFAULT_VALUE = "keys";
    private static final String SAMPLER_FREQUENCY_KEY = "sampler.frequency";
    private static final float SAMPLER_FREQUENCY_DEFAULT_VALUE = 0.1f;
    private static final String SAMPLER_NUM_SAMPLES_KEY = "sampler.num.samples";
    private static final int SAMPLER_NUM_SAMPLES_DEFAULT_VALUE = 1000;

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    @Override
    public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException  {

        if (args.length < 2) {
            usage("Wrong number of arguments: " + args.length);
            return(-1);
        }
        
        String sequenceFileInput = args[0];
        int regionCnt = Integer.parseInt(args[1]);
        return createParitionFile(sequenceFileInput, regionCnt);
    }

    private int createParitionFile(String sequenceFileInput, int regionCnt) throws IOException, ClassNotFoundException, InterruptedException {
        Path input = new Path(sequenceFileInput);
        Job sampler = new Job(getConf());

        sampler.setNumReduceTasks(regionCnt);
        sampler.setInputFormatClass(SequenceFileInputFormat.class);
        sampler.setOutputFormatClass(SequenceFileOutputFormat.class);
        sampler.setOutputKeyClass(BytesWritable.class);
        SequenceFileInputFormat.addInputPath(sampler, input);

        Configuration config = sampler.getConfiguration();

        InputSampler.Sampler<BytesWritable, NullWritable> inputSampler =
                new InputSampler.RandomSampler<BytesWritable, NullWritable>(
                config.getFloat(SAMPLER_FREQUENCY_KEY, SAMPLER_FREQUENCY_DEFAULT_VALUE),
                config.getInt(SAMPLER_NUM_SAMPLES_KEY, SAMPLER_NUM_SAMPLES_DEFAULT_VALUE));

        Path partitionFile = new Path(config.get(OUTPUT_KEYS_FILE_NAME_KEY, OUTPUT_KEYS_FILE_NAME_DEFAULT_VALUE));
        TotalOrderPartitioner.setPartitionFile(config, partitionFile);
        InputSampler.<BytesWritable, NullWritable>writePartitionFile(sampler, inputSampler);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new SequenceFileKeysSampler(), args);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Two parameters needed: <sequence-file-input, number-of-regions>");
        System.out.println("Example: hadoop jar target/commons-1.0-SNAPSHOT.jar " + SequenceFileKeysSampler.class.getName() + " bazekon-20120228.sf 20");
    }
}
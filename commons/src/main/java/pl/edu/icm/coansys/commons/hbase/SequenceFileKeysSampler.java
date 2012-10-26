package pl.edu.icm.coansys.commons.hbase;

import java.io.IOException;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class SequenceFileKeysSampler implements Tool {

    private Configuration conf;
    private String OUTPUT_KEYS_FILE_NAME_KEY = "output.keys.file.name";
    private String OUTPUT_KEYS_FILE_NAME_DEFAULT_VALUE = "keys";
    private String SAMPLER_FREQUENCY_KEY = "sampler.frequency";
    private float SAMPLER_FREQUENCY_DEFAULT_VALUE = 0.1f;
    private String SAMPLER_NUM_SAMPLES_KEY = "sampler.num.samples";
    private int SAMPLER_NUM_SAMPLES_DEFAULT_VALUE = 100;

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    @Override
    public int run(String[] args) throws Exception {
        String sequenceFileInput = args[0];
        int regionCnt = Integer.parseInt(args[1]);
        String keysDirOutput = args[2];
        int result = createParitionFile(sequenceFileInput, regionCnt, keysDirOutput);
        return result;
    }

    private int createParitionFile(String sequenceFileInput, int regionCnt, String keysDirOutput) throws IOException, ClassNotFoundException, InterruptedException {
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

        Path partitionDir = new Path(keysDirOutput);
        partitionDir = partitionDir.makeQualified(partitionDir.getFileSystem(config));
        Path partitionFile = new Path(partitionDir, config.get(OUTPUT_KEYS_FILE_NAME_KEY, OUTPUT_KEYS_FILE_NAME_DEFAULT_VALUE));
        TotalOrderPartitioner.setPartitionFile(config, partitionFile);
        InputSampler.<BytesWritable, NullWritable>writePartitionFile(sampler, inputSampler);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 3) {
            usage("Wrong number of arguments: " + otherArgs.length);
            System.exit(-1);
        }

        int result = ToolRunner.run(conf, new SequenceFileKeysSampler(), otherArgs);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Three parameters needed: <sequence-file-input, number-of-regions, keys-dir-output>");
        System.out.println("Example: hadoop jar target/commons-1.0-SNAPSHOT.jar " + SequenceFileKeysSampler.class.getName() + " bazekon-20120228.sf 20 sf-split");
    }
}



// hadoop jar target/commons-1.0-SNAPSHOT.jar pl.edu.icm.coansys.commons.hbase.SequenceFileKeysSampler bazekon-20120228.sf 20 sf-split
// export HBASE_CLASSPATH=target/commons-1.0-SNAPSHOT.jar
// hbase org.apache.hadoop.hbase.util.RegionSplitter -D split.algorithm=pl.edu.icm.coansys.commons.hbase.SequenceFileSplitAlgorithm -c 2 -f c:m test_table1
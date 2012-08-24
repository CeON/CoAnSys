/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.jobs;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class MostPopular implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(MostPopular.class);
    private Configuration conf;
    
    public static class CounterMap extends Mapper<Text, BytesWritable, Text, LongWritable> {
        private final static LongWritable one = new LongWritable(1);
        
        @Override
        protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
            AuditEntryProtos.LogMessage logMessage = AuditEntryProtos.LogMessage.parseFrom(value.copyBytes());
            String eventType = logMessage.getEventType();
            if ("SAVE_TO_DISK".equals(eventType)) {
                String resourceId = logMessage.getArg(5);
                context.write(new Text(resourceId), one);
            }
        }
    }
    
    public static class CounterReduce extends Reducer<Text, LongWritable, Text, LongWritable> {

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable value : values) {
                sum += value.get();
            }
            context.write(key, new LongWritable(sum));
        }  
    }

    @Override
    public int run(String[] args) throws Exception {

        Job job = new Job(conf);
        job.setJarByClass(MostPopular.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path("/tmp/testlogs.seqfile"));
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path("/tmp/output"));
        job.setMapperClass(MostPopular.CounterMap.class);
        job.setCombinerClass(CounterReduce.class);
        job.setReducerClass(CounterReduce.class);

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
        System.exit(ToolRunner.run(new MostPopular(), args));
    }
}

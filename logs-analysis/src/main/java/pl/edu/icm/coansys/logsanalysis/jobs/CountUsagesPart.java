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
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.logsanalysis.metrics.UsageWeight;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryFactory;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos;
import pl.edu.icm.coansys.logsanalysis.transformers.AuditEntry2Protos;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class CountUsagesPart implements Tool {
    
    private static final String DEFAULT_USAGE_WEIGHT_CLASS = "pl.edu.icm.coansys.logsanalysis.metrics.SimpleUsageWeight";

    private static final Logger logger = LoggerFactory.getLogger(CountUsagesPart.class);
    private Configuration conf;

    public static class CounterMap extends Mapper<Writable, BytesWritable, Text, LongWritable> {

        private UsageWeight weight;

        @Override
        protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {
            AuditEntryProtos.LogMessage logMessage = AuditEntryProtos.LogMessage.parseFrom(value.copyBytes());
            AuditEntry entry = AuditEntry2Protos.deserialize(logMessage);

            long usageWeight = weight.getWeight(entry);
            if (usageWeight > 0) {
                String resourceId = AuditEntryFactory.getResourceId(entry);
                context.write(new Text(resourceId), new LongWritable(usageWeight));
            }
        }

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            String weightClassName = conf.get("USAGE_WEIGHT_CLASS");
            if (weightClassName == null || "".equals(weightClassName)) {
                weightClassName = DEFAULT_USAGE_WEIGHT_CLASS;
            }
            try {
                Class weightClass = Class.forName(weightClassName);
                weight = (UsageWeight) weightClass.newInstance();
            } catch (Exception ex) {
                logger.error(ex.toString());
                throw new IOException(ex);
            }
            super.setup(context);
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

        if (args.length < 2) {
            logger.error("Usage: CountUsagePart <input_seqfile> <output_dir> [<usage_weight_class>]");
            return 1;
        }

        if (args.length > 2) {
            conf.set("USAGE_WEIGHT_CLASS", args[2]);
        }

        Job job = new Job(conf);
        job.setJarByClass(CountUsagesPart.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.setMapperClass(CounterMap.class);
        //job.setMapOutputKeyClass(Text.class);
        //job.setMapOutputValueClass(LongWritable.class);
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
        System.exit(ToolRunner.run(new CountUsagesPart(), args));
    }
}

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
import pl.edu.icm.coansys.logsanalysis.constants.ParamNames;
import pl.edu.icm.coansys.logsanalysis.metrics.UsageWeight;
import pl.edu.icm.coansys.logsanalysis.models.LogsMessageHelper;
import pl.edu.icm.coansys.models.LogsProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class CountUsagesPart implements Tool {
    
    private static final String DEFAULT_USAGE_WEIGHT_CLASS = "pl.edu.icm.coansys.logsanalysis.metrics.ComplexUsageWeight";

    private static final Logger logger = LoggerFactory.getLogger(CountUsagesPart.class);
    private Configuration conf;

    public static class CounterMap extends Mapper<Writable, BytesWritable, Text, LongWritable> {

        private UsageWeight weight;

        @Override
        protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

            LogsProtos.LogsMessage logMessage = LogsProtos.LogsMessage.parseFrom(value.copyBytes());

            long usageWeight = weight.getWeight(logMessage);
            if (usageWeight > 0) {
                String resourceId = LogsMessageHelper.getParam(logMessage, ParamNames.RESOURCE_ID_PARAM);
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
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

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

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.jobs;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class HTableToSequenceFile implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(HTableToSequenceFile.class);
    
    private Configuration conf;
    
    public static class ConvertMap extends TableMapper<NullWritable, BytesWritable> {

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            byte[] columnFamily = Bytes.toBytes(conf.get("COLUMN_FAMILY"));
            byte[] columnName = Bytes.toBytes(conf.get("COLUMN_NAME"));
            
            if (value.containsColumn(columnFamily, columnName)) {
                KeyValue keyValue = value.getColumnLatest(columnFamily, columnName);
                context.write(NullWritable.get(), new BytesWritable(keyValue.getValue()));
            }
        }
    }

    @Override
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length < 4) {
            System.err.println("Usage: HTableToSequenceFile <table_name> <column_family> <column_name> <sequence_file_path>");
            return -1;
        }
        conf.set("COLUMN_FAMILY", args[1]);
        conf.set("COLUMN_NAME", args[2]);
        Job job = new Job(conf);
        job.setJarByClass(HTableToSequenceFile.class);
        job.setNumReduceTasks(0);
        job.setMapperClass(ConvertMap.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(BytesWritable.class);
        job.setInputFormatClass(TableInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        Scan scan = new Scan();
        TableMapReduceUtil.initTableMapperJob(args[0], scan, ConvertMap.class, NullWritable.class, BytesWritable.class, job);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[3]));
        
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
        System.exit(ToolRunner.run(new HTableToSequenceFile(), args));
    }
}

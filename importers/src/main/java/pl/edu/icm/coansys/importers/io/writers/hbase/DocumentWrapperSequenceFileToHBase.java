/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.io.writers.hbase;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class DocumentWrapperSequenceFileToHBase implements Tool {

    private Configuration conf;
    final static String BULK_OUTPUT_CONF_KEY = "bulk.output";
  
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

        String tableName = args[1];
        
        setOptimizedConfiguration(conf);

        Job job = new Job(conf, DocumentWrapperSequenceFileToHBase.class.getSimpleName());
        job.setJarByClass(DocumentWrapperSequenceFileToHBase.class);
        
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));

        String hfileOutPath = conf.get(BULK_OUTPUT_CONF_KEY);
        if (hfileOutPath != null) {
            HTable table = new HTable(conf, tableName);
            Path outputDir = new Path(hfileOutPath);
            FileOutputFormat.setOutputPath(job, outputDir);
            job.setMapOutputKeyClass(ImmutableBytesWritable.class);
            job.setMapOutputValueClass(Put.class);
            HFileOutputFormat.configureIncrementalLoad(job, table);
        } else {
            // No reducers.  Just write straight to table.  Call initTableReducerJob
            // to set up the TableOutputFormat.
            TableMapReduceUtil.initTableReducerJob(tableName, null, job);
            job.setNumReduceTasks(0);
        }

        boolean success = job.waitForCompletion(true);
        if (!success) {
            throw new IOException("Error with job!");
        }

        return 0;
    }

    private void setOptimizedConfiguration(Configuration conf) {
        conf.set("mapred.child.java.opts", "-Xmx2000m");
        conf.set("io.sort.mb", "1000");
        conf.set("io.sort.spill.percent", "0.90");
        conf.set("io.sort.record.percent", "0.15");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
             usage("Wrong number of arguments: " + args.length);
             System.exit(-1);
        }

        int result = ToolRunner.run(new DocumentWrapperSequenceFileToHBase(), args);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Exemplary command: ");
        String command = "hadoop jar target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar"
                + " " + DocumentWrapperSequenceFileToHBase.class.getName()
                + " -D mapreduce.map.class=<mapper.class>"
                + " -D " + BULK_OUTPUT_CONF_KEY + "=<bulkoutputfile>"
                + " <directory> <table>";

        System.out.println(command);
    }
}
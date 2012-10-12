/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.io.writers.hbase;

import static pl.edu.icm.coansys.importers.constants.HBaseConstant.*;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper.DocumentWrapper;

/**
 *
 * @author akawa
 */
public class DocumentWrapperSequenceFileToHBase implements Tool {

    private static Logger logger = LoggerFactory.getLogger(DocumentWrapperSequenceFileToHBase.class);
    private Configuration conf;
    private static final String LOCAL_JOBTRACKER = "local";
    final static String BULK_OUTPUT_CONF_KEY = "bulk.output";

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    public static class DocumentWrapperToHBasePutMapper extends Mapper<BytesWritable, BytesWritable, ImmutableBytesWritable, Put> {

        private ImmutableBytesWritable docWrapRowKey = new ImmutableBytesWritable();

        @Override
        protected void map(BytesWritable rowKey, BytesWritable documentWrapper, Context context)
                throws IOException, InterruptedException {

            DocumentWrapper docWrap = DocumentWrapper.parseFrom(documentWrapper.copyBytes());

            docWrapRowKey.set(docWrap.getRowId().toByteArray());
            
            Put put = new Put(docWrap.getRowId().toByteArray());
            put.add(FAMILY_METADATA_BYTES, FAMILY_METADATA_QUALIFIER_PROTO_BYTES, docWrap.getMproto().toByteArray());
            put.add(FAMILY_CONTENT_BYTES, FAMILY_CONTENT_QUALIFIER_PROTO_BYTES, docWrap.getCproto().toByteArray());

            context.write(docWrapRowKey, put);
        }
    }

    @Override
    public int run(String[] args) throws Exception {

        getOptimizedConfiguration(conf);

        Job job = new Job(conf, DocumentWrapperSequenceFileToHBase.class.getSimpleName());
        job.setJarByClass(DocumentWrapperSequenceFileToHBase.class);

        job.setMapperClass(DocumentWrapperToHBasePutMapper.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));

        String tableName = args[1];

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

    private void getOptimizedConfiguration(Configuration conf) {
        conf.set("mapred.child.java.opts", "-Xmx2000m");
        conf.set("io.sort.mb", "500");
        conf.set("io.sort.spill.percent", "0.90");
        conf.set("io.sort.record.percent", "0.15");
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            usage("Wrong number of arguments: " + otherArgs.length);
            System.exit(-1);
        }

        int result = ToolRunner.run(conf, new DocumentWrapperSequenceFileToHBase(), args);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Exemplary command: ");
        String command = "hadoop jar target/importers-1.0-SNAPSHOT-jar-with-dependencies.jar"
                + " " + DocumentWrapperSequenceFileToHBase.class.getName()
                + " -D" + BULK_OUTPUT_CONF_KEY + "=bulkoutputfile"
                + " directory table";
        
        System.out.println(command);
    }
}
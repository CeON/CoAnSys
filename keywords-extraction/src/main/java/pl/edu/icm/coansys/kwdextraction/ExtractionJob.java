/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.importers.models.KeywordExtractionProtos.ExtractedKeywords;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class ExtractionJob implements Tool {

    private static final Logger logger = LoggerFactory.getLogger(ExtractionJob.class);
    Configuration conf;

    public static class ExtractMap extends Mapper<Writable, BytesWritable, Text, BytesWritable> {

        @Override
        protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {
            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            ExtractedKeywords.Builder kwdBuilder = ExtractedKeywords.newBuilder();
            kwdBuilder.setAlgorithm("RAKE");
            kwdBuilder.setDocId(docWrapper.getRowId());
            for (String keyword : new RakeExtractor(docWrapper).getKeywords()) {
                kwdBuilder.addKeyword(keyword);
            }
            if (kwdBuilder.getKeywordCount() > 0) {
                context.write(new Text(docWrapper.getRowId()), new BytesWritable(kwdBuilder.build().toByteArray()));
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            logger.error("Usage: ExtractionJob <input_seqfile> <output_dir>");
            return 1;
        }

        Job job = new Job(conf);
        job.setJarByClass(ExtractionJob.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(ExtractMap.class);
        
        job.setNumReduceTasks(0);

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
        conf.set("dfs.client.socket-timeout", "70000");
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new ExtractionJob(), args));
    }
}

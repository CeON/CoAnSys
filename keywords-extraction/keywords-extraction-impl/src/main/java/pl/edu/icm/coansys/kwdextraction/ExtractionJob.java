/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.IOException;
import java.util.Date;
import java.util.List;
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
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
//import pl.edu.icm.coansys.models.KeywordExtractionProtos.ExtractedKeywords;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class ExtractionJob implements Tool {

    /*
     * EXTRACTION_OPTION - i.e. CONTENT, ABSTRACT, CONTENT_AND_ABSTRACT EXTRACTION_LANGUAGE - "en", "pl", "fr". Only
     * texts in this language will be processed.
     */
    private static final String EXTRACTION_OPTION = "EXTRACTION_OPTION";
    private static final String EXTRACTION_LANGUAGE = "EXTRACTION_LANGUAGE";
    private static final String ALGORITHM_NAME = "RAKE";
    private static final Logger logger = LoggerFactory.getLogger(ExtractionJob.class);
    private Configuration conf;

    public static class ExtractMap extends Mapper<Writable, BytesWritable, Text, BytesWritable> {

        @Override
        protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

            Configuration conf = context.getConfiguration();
            String extractionOption = conf.get(EXTRACTION_OPTION);
            String lang = conf.get(EXTRACTION_LANGUAGE);

            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            RakeExtractor rakeExtractor = new RakeExtractor(docWrapper, extractionOption, lang);
            List<String> keywords = rakeExtractor.getKeywords();

            if (keywords.size() > 0) {
                String docId = docWrapper.getDocumentMetadata().getKey();

                DocumentProtos.KeywordsList.Builder kwdBuilder = DocumentProtos.KeywordsList.newBuilder();

                DocumentProtos.ProvenanceInfo.Builder provenanceBuilder = DocumentProtos.ProvenanceInfo.newBuilder();
                DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder singleProvenanceBuilder = DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.newBuilder();
                singleProvenanceBuilder.setLastModificationDate(new Date().getTime());
                singleProvenanceBuilder.setLastModificationMarkerId(ALGORITHM_NAME);
                provenanceBuilder.setCurrentProvenance(singleProvenanceBuilder);
                kwdBuilder.setProvenance(provenanceBuilder);

                kwdBuilder.addAllKeywords(keywords);

                context.write(new Text(docId), new BytesWritable(kwdBuilder.build().toByteArray()));
            }
        }
    }

    @Override
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length < 4) {
            logger.error("Usage: ExtractionJob <input_seqfile> <output_dir> <extractionOption> <language>");
            logger.error("  extractionOption must be one of the following:");
            for (String opt : RakeExtractor.getAvailableExtractionOptions()) {
                logger.error("    " + opt);
            }
            logger.error("  supported languages:");
            for (String lang : RakeExtractor.getSupportedLanguages()) {
                logger.error("    " + lang);
            }
            return 1;
        }

        conf.set(EXTRACTION_OPTION, args[2]);
        conf.set(EXTRACTION_LANGUAGE, args[3]);

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
        boolean success = job.waitForCompletion(true);
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

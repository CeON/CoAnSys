/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.nlmextraction;

import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.cermine.PdfBxStructureExtractor;
import pl.edu.icm.cermine.PdfNLMContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.structure.DocstrumSegmenter;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.Media;
import pl.edu.icm.coansys.models.DocumentProtos.MediaContainer;
import pl.edu.icm.coansys.models.constants.ProtoConstants;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class NLMExtractionJob implements Tool {

    private static final String MAX_PDF_SIZE = "MAX_PDF_SIZE";
    private static final Logger logger = LoggerFactory.getLogger(NLMExtractionJob.class);
    private Configuration conf;

    public static class ExtractMap extends Mapper<Writable, BytesWritable, Text, BytesWritable> {
        
        private long maxPdfSize = 0;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);

            Configuration conf = context.getConfiguration();
            String maxPdfSizeString = conf.get(MAX_PDF_SIZE);
            if (maxPdfSizeString != null) {
                try {
                    maxPdfSize = (long) (Float.parseFloat(maxPdfSizeString) * 1024 * 1024);
                } catch (NumberFormatException ex) {
                    throw new IOException(maxPdfSizeString + " is not a valid max PDF size", ex);
                }
            }
        }



        @Override
        protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            MediaContainer mediaContainer = docWrapper.getMediaContainer();
            for (Media media : mediaContainer.getMediaList()) {
                if (ProtoConstants.mediaTypePdf.equals(media.getMediaType())) {
                    long fileSize;
                    if (media.hasSourceFilesize()) {
                        fileSize = media.getSourceFilesize();
                    } else {
                        logger.warn("Source file size is not set in " + media.getKey() + ", using getSerializedSize() method");
                        fileSize = media.getSerializedSize();
                    }

                    if (maxPdfSize > 0 && fileSize <= maxPdfSize) {

                        InputStream pdfIS = media.getContent().newInput();
                        try {
                            PdfNLMContentExtractor nlmExtr = new PdfNLMContentExtractor();
                            PdfBxStructureExtractor strExtractor = new PdfBxStructureExtractor();
                            strExtractor.setPageSegmenter(new DocstrumSegmenter());
                            nlmExtr.setStructureExtractor(strExtractor);

                            Element nlmContent = nlmExtr.extractContent(pdfIS);

                            XMLOutputter outp = new XMLOutputter();
                            String nlmString = outp.outputString(nlmContent);

                            Media.Builder nlmMediaBuilder = Media.newBuilder();
                            nlmMediaBuilder.setCollection(media.getCollection());
                            nlmMediaBuilder.setKey(media.getKey());
                            nlmMediaBuilder.setSourceFilesize(nlmString.length());
                            nlmMediaBuilder.setContent(ByteString.copyFromUtf8(nlmString));
                            nlmMediaBuilder.setMediaType(ProtoConstants.mediaTypeNlm);

                            DocumentProtos.ProvenanceInfo.Builder provenanceBuilder = DocumentProtos.ProvenanceInfo.newBuilder();
                            DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.Builder signleProvenance =
                                    DocumentProtos.ProvenanceInfo.SingleProvenanceInfo.newBuilder();
                            signleProvenance.setLastModificationDate(new Date().getTime());
                            signleProvenance.setLastModificationMarkerId("Coansys NLM extraction (CERMINE)");
                            provenanceBuilder.setCurrentProvenance(signleProvenance);
                            nlmMediaBuilder.setProvenance(provenanceBuilder);

                            context.write(new Text(media.getKey()), new BytesWritable(nlmMediaBuilder.build().toByteArray()));
                        } catch (AnalysisException ex) {
                            throw new IOException(ex);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length < 3) {
            logger.error("Usage: NLMExtractionJob <input_seqfile> <output_dir> <max_PDF_size>");
            logger.error("  (max_PDF_size -- size in MB; greater files will be ignored)");
            return 1;
        }

        conf.set(MAX_PDF_SIZE, args[2]);

        Job job = new Job(conf);
        job.setJarByClass(NLMExtractionJob.class);
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
        System.exit(ToolRunner.run(new NLMExtractionJob(), args));
    }
}

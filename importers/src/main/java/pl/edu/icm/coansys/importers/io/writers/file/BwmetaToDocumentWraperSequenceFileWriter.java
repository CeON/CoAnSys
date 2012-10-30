/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.io.writers.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.log4j.Logger;
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.transformers.RowComposer;

public class BwmetaToDocumentWraperSequenceFileWriter {

    private static final Logger LOGGER = Logger.getLogger(BwmetaToDocumentWraperSequenceFileWriter.class);
    private static String[] DEFAULT_ARGS = {
        "/home/akawa/bwndata/zips/",
        "cedram",
        "/home/akawa/bwndata/cedram.sf",};
    
    private static long metadataCount = 0;
    private static long mediaCount = 0;
    private static long mediaConteinerCount = 0;

    public static void main(String[] args) throws IOException {

        args = ((args == null || args.length == 0) ? DEFAULT_ARGS : args);

        if (args.length != 3) {
            usage();
            System.exit(1);
        }

        String inputDir = args[0];
        String collection = args[1];
        String outputSequenceFile = args[2];

        checkPaths(inputDir, collection, outputSequenceFile);
        generateSequenceFile(inputDir, collection, outputSequenceFile);
        printStats();

    }
    
    private static void printStats() {
        LOGGER.info(metadataCount + " metadata records");
        LOGGER.info(mediaConteinerCount + " mediaContainer records");
        LOGGER.info(mediaCount + " media records");
    }

    private static void checkPaths(String inputDir, String collection, String outputSequenceFile) throws IOException {
        File input = new File(inputDir);
        if (!input.exists()) {
            System.err.println("<Input dir> does not exist: " + inputDir);
            System.exit(2);
        }
        if (!input.isDirectory()) {
            System.err.println("<Input dir> is not a directory:" + inputDir);
            System.exit(3);
        }
        if (collection.length() != collection.replaceAll("[^a-zA-Z0-9]", "").length()) {
            System.err.println("Only alphanumeric signs (a space sign is also excluded) are allowed for a collection name: " + collection);
            System.exit(4);
        }
        File outf = new File(outputSequenceFile);
        if (!outf.getParentFile().exists()) {
            outf.getParentFile().mkdirs();
        }

    }

    private static void generateSequenceFile(String inputDir, String collection, String outputSequenceFile) throws IOException {
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(inputDir, collection);
        SequenceFile.Writer writer = null;
        try {
            BytesWritable rowKeyBytesWritable = new BytesWritable();
            BytesWritable documentWrapperBytesWritable = new BytesWritable();
            DocumentWrapper.Builder dw = DocumentWrapper.newBuilder();

            writer = createSequenceFileWriter(outputSequenceFile, rowKeyBytesWritable, documentWrapperBytesWritable);
            for (DocumentDTO doc : zdtp) {
                DocumentWrapper docWrap = buildFrom(dw, doc);

                // specify key
                byte[] rowKey = docWrap.getRowId().getBytes();
                rowKeyBytesWritable.set(rowKey, 0, rowKey.length);

                // specify value
                byte[] dwBytes = docWrap.toByteArray();
                documentWrapperBytesWritable.set(dwBytes, 0, dwBytes.length);

                // append to the sequence file
                writer.append(rowKeyBytesWritable, documentWrapperBytesWritable);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    private static DocumentWrapper buildFrom(DocumentWrapper.Builder dw, DocumentDTO doc) {
        String rowId = RowComposer.composeRow(doc);
        dw.setRowId(rowId);

        LOGGER.trace("Building: ");
        LOGGER.trace("\tKey = " + doc.getKey());
        LOGGER.trace("\tCollection = " + doc.getCollection());

        DocumentMetadata documentMetadata = doc.getDocumentMetadata();
        if (documentMetadata.getSerializedSize() > 0) {
            dw.setDocumentMetadata(documentMetadata);
            LOGGER.trace("\tArchiveZip = " + documentMetadata.getSourceArchive());
            LOGGER.trace("\tSourcePath = " + documentMetadata.getSourcePath());
            LOGGER.trace("\tDocumentMetadata size: " + documentMetadata.toByteArray().length);
            metadataCount++;
        }

        MediaContainer mediaConteiner = doc.getMediaConteiner();
        if (mediaConteiner.getSerializedSize() > 0) {
            dw.setMediaContainer(mediaConteiner);
            LOGGER.info("\tMediaConteiner size: " + (mediaConteiner.toByteArray().length / 1024 / 1024) + " MB");
            for (Media media : mediaConteiner.getMediaList()) {
                LOGGER.info("\tArchiveZip = " + media.getSourceArchive());
                LOGGER.info("\tSourcePath = " + media.getSourcePath());
                LOGGER.info("\tSourcePathFilesize = " + (media.getSourcePathFilesize() / 1024 / 1024) + " MB");
                mediaCount++;
            }
            mediaConteinerCount++;
        }

        return dw.build();
    }

    private static SequenceFile.Writer createSequenceFileWriter(String uri, Writable key, Object value) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(uri);
        SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());
        return writer;
    }

    private static void usage() {
        String usage = "Usage: \n"
                + "java -cp importers-*-with-deps.jar "
                + BwmetaToDocumentWraperSequenceFileWriter.class.getName()
                + " <in_dir> <collectionName> <out_file>";
        System.out.println(usage);
    }
}

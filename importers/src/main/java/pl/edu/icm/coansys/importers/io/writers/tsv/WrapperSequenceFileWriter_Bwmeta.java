/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.io.writers.tsv;

import com.google.protobuf.ByteString;
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
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper;
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper.DocumentWrapper;
import pl.edu.icm.coansys.importers.transformers.RowComposer;

public class WrapperSequenceFileWriter_Bwmeta {

    private WrapperSequenceFileWriter_Bwmeta() {
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            usage();
            System.exit(1);
        }

        String inputDir = args[0];
        String collection = args[1];
        String outputSequenceFile = args[2];

        checkPaths(inputDir, collection, outputSequenceFile);
        generateSequenceFile(inputDir, collection, outputSequenceFile);

    }

    private static void checkPaths(String inputDir, String collection, String outputSequenceFile) throws IOException {
        File input = new File(inputDir);
        if (!input.exists()) {
            System.out.println("<Input dir> does not exist: " + inputDir);
            System.exit(2);
        }
        if (!input.isDirectory()) {
            System.out.println("<Input dir> is not a directory:" + inputDir);
            System.exit(3);
        }
        if (collection.length() != collection.replaceAll("[^a-zA-Z0-9]", "").length()) {
            System.out.println("Only alphanumeric signs (a space sign is also excluded) are allowed for a collection name: " + collection);
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
            DocumentProtosWrapper.DocumentWrapper.Builder dw = DocumentProtosWrapper.DocumentWrapper.newBuilder();

            writer = createSequenceFileWriter(outputSequenceFile, rowKeyBytesWritable, documentWrapperBytesWritable);
            for (DocumentDTO doc : zdtp) {
                DocumentWrapper docWrap = buildFrom(dw, doc);

                // specify key
                byte[] rowKey = docWrap.getRowId().toByteArray();
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

    private static DocumentWrapper buildFrom(DocumentProtosWrapper.DocumentWrapper.Builder dw, DocumentDTO doc) {
        ByteString rowId = ByteString.copyFrom(RowComposer.composeRow(doc).getBytes());
        dw.setRowId(rowId);

        System.out.println("Building: " + rowId.toString());

        DocumentMetadata documentMetadata = doc.getDocumentMetadata();
        if (documentMetadata != null) {
            dw.setMproto(documentMetadata.toByteString());
            System.out.println("\tdocumentMetadata size: " + documentMetadata.toByteString().size());
        }

        MediaContainer mediaConteiner = doc.getMediaConteiner();
        if (mediaConteiner != null) {
            dw.setCproto(mediaConteiner.toByteString());
            System.out.println("\tmediaConteiner size: " + mediaConteiner.toByteString().size());
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
                + WrapperSequenceFileWriter_Bwmeta.class.getName()
                + " <in_dir> <collectionName> <out_file>";
        System.out.println(usage);
    }
}

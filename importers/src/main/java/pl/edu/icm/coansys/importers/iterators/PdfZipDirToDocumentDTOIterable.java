/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.iterators;

import com.google.protobuf.ByteString;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.utils.ZipArchive;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class PdfZipDirToDocumentDTOIterable extends DocumentDTOIterable {

    private static final Logger logger = LoggerFactory.getLogger(PdfZipDirToDocumentDTOIterable.class);
    private Iterator<File> filesIterator = null;
    private Iterator<String> pathsInZipIterator;
    private ZipArchive archive;

    public PdfZipDirToDocumentDTOIterable(String dataPath, String collection) {
        super(dataPath, collection);
    }

    @Override
    protected DocumentDTO prepareNextItem(String dataPath, String collection) {
        DocumentDTO nextItem = null;
        while (nextItem == null) {
            while (pathsInZipIterator == null || !pathsInZipIterator.hasNext()) {
                if (filesIterator == null) {
                    File inputDir = new File(dataPath);
                    if (!inputDir.isDirectory()) {
                        logger.error(dataPath + " is not a directory");
                        return null;
                    }
                    filesIterator = FileUtils.listFiles(inputDir, TrueFileFilter.TRUE, TrueFileFilter.TRUE).iterator();
                }
                if (!filesIterator.hasNext()) {
                    return null;
                }
                File currentZip = filesIterator.next();
                try {
                    archive = new ZipArchive(currentZip);
                } catch (IOException ex) {
                    logger.error("Unable to read zip archive " + currentZip.getPath() + ": " + ex);
                    continue;
                }
                List<String> filter = archive.filter(".*[PpDdFf]");
                pathsInZipIterator = filter.iterator();
            }
            
            String pdfInZip = pathsInZipIterator.next();

            byte[] pdfByteArray;
            try {
                InputStream pdfInputStream = archive.getFileAsInputStream(pdfInZip);
                pdfByteArray = IOUtils.toByteArray(pdfInputStream);
            } catch (IOException ex) {
                logger.error("Unable to retreive file from zip: " + ex);
                continue;
            }

            nextItem = new DocumentDTO();
            Media.Builder mediaBuilder = Media.newBuilder();
            mediaBuilder.setKey(pdfInZip);
            mediaBuilder.setContent(ByteString.copyFrom(pdfByteArray));
            mediaBuilder.setCollection(collection);
            mediaBuilder.setMediaType(ProtoConstants.mediaTypePdf);
            mediaBuilder.setSourcePath(dataPath + "/" + archive.getZipFilePath() + "#" + pdfInZip);
            mediaBuilder.setSourceFilesize(pdfByteArray.length);

            nextItem.addMedia(mediaBuilder.build());
            nextItem.setKey(pdfInZip);
            nextItem.setCollection(collection);
        }
        return nextItem;
    }
}

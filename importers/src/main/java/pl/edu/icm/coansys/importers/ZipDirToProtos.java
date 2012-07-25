/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.edu.icm.coansys.importers.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.DocumentProtos.DocumentMetadata.Builder;
import pl.edu.icm.synat.application.model.bwmeta.YContentEntry;
import pl.edu.icm.synat.application.model.bwmeta.YElement;
import pl.edu.icm.synat.application.model.bwmeta.YExportable;

/**
 *
 * @author acz
 */
public class ZipDirToProtos implements Iterable<DocumentMetadata.Builder> {

    private File[] listZipFiles = null;
    private int zipIndex = 0;
    private ZipArchive actualZipArchive = null;
    private Iterator<String> xmlPathIterator = null;
    private Iterator<YExportable> yExportableIterator = null;
    private DocumentMetadata.Builder nextItem;

    public ZipDirToProtos(String zipDirPath) {
        File zipDir = new File(zipDirPath);
        if (zipDir.isDirectory()) {
            listZipFiles = zipDir.listFiles();
        }
        moveToNextItem();
    }
    
    @Override
    public Iterator<DocumentMetadata.Builder> iterator() {
        return new Iterator() {

            @Override
            public boolean hasNext() {
                return nextItem != null;
            }

            @Override
            public DocumentMetadata.Builder next() {
                moveToNextItem();
                return nextItem;
            }

            @Override
            public void remove() {
                moveToNextItem();
            }
        };
    }

    private void moveToNextItem() {

        nextItem = null;

        while (nextItem == null) {
            while (yExportableIterator == null || !yExportableIterator.hasNext()) {
                while (xmlPathIterator == null || !xmlPathIterator.hasNext()) {
                    if (listZipFiles == null || zipIndex >= listZipFiles.length) {
                        nextItem = null;
                        return;
                    }
                    // here we have a new zip file
                    try {
                        actualZipArchive = new ZipArchive(listZipFiles[zipIndex].getPath());
                        xmlPathIterator = actualZipArchive.filter(".*xml").iterator();
                    } catch (IOException ex) {
                        // TODO: uporządkować logi
                        Logger.getLogger(ZipDirToProtos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    zipIndex++;
                }
                // here we have a new xml path:
                String xmlPath = xmlPathIterator.next();
                try {
                    InputStream xmlIS = actualZipArchive.getFileAsInputStream(xmlPath);
                    yExportableIterator = MetadataPBParser.streamToYExportable(xmlIS, MetadataPBParser.MetadataType.BWMETA).iterator();
                } catch (IOException ex) {
                    // TODO: uporządkować logi
                    Logger.getLogger(ZipDirToProtos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // here we have an yExportable:
            YExportable yExportable = yExportableIterator.next();

            if (yExportable instanceof YElement) {
                YElement yElement = (YElement) yExportable;
                
                nextItem = MetadataPBParser.yelementToDocumentMetadata(yElement);
                
                // try to enrich nextItem to media content
                if (nextItem != null) {
                    List<YContentEntry> contents = yElement.getContents();
                    for (YContentEntry content : contents) {
                        InputStream pdfIS = null;
                        try {
                            //TODO: wyciągnąć z yElement ścieżkę do PDF-a
                            if (content.isFile()) {
                                // content.getAttributes("lol");
                            }
                            //To jest tymczasowo, udaje prawdziwą ścieżkę:
                            String pdfPath = "";
                            pdfIS = actualZipArchive.getFileAsInputStream(pdfPath);
                        } catch (IOException ex) {
                            //TODO: uporządkować logi
                            Logger.getLogger(ZipDirToProtos.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                pdfIS.close();
                            } catch (IOException ex) {
                                //TODO: uporządkować logi
                                Logger.getLogger(ZipDirToProtos.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    public static Iterable<DocumentMetadata.Builder> getYExportables(String zipDirPath) throws IOException {

        List<DocumentMetadata.Builder> result = new ArrayList<DocumentMetadata.Builder>();

        File zipDir = new File(zipDirPath);
        if (!zipDir.isDirectory()) {
            throw new IOException("Not a directory");
        }
        for (File file : zipDir.listFiles(new ZipFilter())) {
            ZipArchive zipArchive = new ZipArchive(file.getPath());

            for (String xmlFilePath : zipArchive.filter(".*xml")) {
                InputStream xmlIS = zipArchive.getFileAsInputStream(xmlFilePath);
                List<YExportable> yExportables = MetadataPBParser.streamToYExportable(xmlIS, MetadataPBParser.MetadataType.BWMETA);

                if (yExportables != null) {
                    for (YExportable yExportable : yExportables) {
                        if (yExportable instanceof YElement) {
                            YElement yElement = (YElement) yExportable;
                            Builder doc = MetadataPBParser.yelementToDocumentMetadata(yElement);
                            if (doc != null) {
                                List<YContentEntry> contents = yElement.getContents();
                                for (YContentEntry content : contents) {
                                    //TODO: wyciągnąć z yElement ścieżkę do PDF-a
                                    if (content.isFile()) {
                                        content.getAttributes("lol");
                                    }
                                    //To jest tymczasowo, udaje prawdziwą ścieżkę:
                                    String pdfPath = "";
                                    InputStream pdfIS = zipArchive.getFileAsInputStream(pdfPath);
                                    //TODO: dodać do doc pdf-a, którego możemy przeczytać z pdfPath
                                }

                                result.add(doc);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private static class ZipFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".zip"));
        }
    }
}

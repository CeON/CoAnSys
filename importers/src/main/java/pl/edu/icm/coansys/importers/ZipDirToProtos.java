/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import pl.edu.icm.coansys.importers.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.DocumentProtos.DocumentMetadata.Builder;
import pl.edu.icm.synat.application.model.bwmeta.YContentEntry;
import pl.edu.icm.synat.application.model.bwmeta.YElement;
import pl.edu.icm.synat.application.model.bwmeta.YExportable;

/**
 *
 * @author acz
 */
public class ZipDirToProtos {

    public static List<DocumentMetadata.Builder> getYExportables(String zipDirPath) throws IOException {

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

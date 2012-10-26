/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.iterators;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.ZipArchive;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.parsers.MetadataToProtoMetadataParser;
import pl.edu.icm.coansys.importers.transformers.YElementFromZip2DocumentDto;
import pl.edu.icm.synat.application.model.bwmeta.YExportable;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 * @author pdendek
 */
public class ZipDirToDocumentDTOIterator implements Iterable<DocumentDTO> {
    /*
     * The directory contains multiple zip files. Every zip file can contain
     * multiple xml files. Every xml file can contain multiple YExportable
     * objects. An iterator of this class walks through every zip file, then
     * every xml file and every YExportable object. Output type
     * DocumentDTO is a class carrying data about an input xml document enhanced 
     * with its media (like pdf, tiff, etc.).  
     */

    private static final Logger logger = LoggerFactory.getLogger(ZipDirToDocumentDTOIterator.class);
    private String collection = "unset";
    //List of zip files to process and actual position in this list
    private File[] listZipFiles;
    private int zipIndex;
    //A zip archive we are processing
    private ZipArchive currentZipArchive = null;
    //Markers of actual position in archive
    private Iterator<String> xmlPathIterator = null;
    private Iterator<YExportable> yExportableIterator = null;
    //An object which will be returned by next call of iterators next() method
    private DocumentDTO nextItem = null;
	private YElementFromZip2DocumentDto yElementFromZip2DocumentDTO;
	List<String> xmls = null;
	int currentXml = 0;

    public ZipDirToDocumentDTOIterator(String zipDirPath, String collection) {
        this.collection = collection;
        File zipDir = new File(zipDirPath);
        
        yElementFromZip2DocumentDTO = new YElementFromZip2DocumentDto(collection);
        
        if (zipDir.isDirectory()) {
            listZipFiles = zipDir.listFiles(new ZipFilter());
            zipIndex = 0;
            moveToNextItem();
        } else {
            logger.error(ZipDirToDocumentDTOIterator.class.getName() + ": " + zipDirPath + " is not a directory");
        }
    }

    @Override
    public Iterator<DocumentDTO> iterator() {
        return new Iterator<DocumentDTO>() {

            @Override
            public boolean hasNext() {
                return nextItem != null;
            }

            @Override
            public DocumentDTO next() {
                DocumentDTO actualItem = nextItem;
                moveToNextItem();
                return actualItem;
            }

            @Override
            public void remove() {
                moveToNextItem();
            }
        };
    }

    private void moveToNextItem() {
        DocumentDTO docDTO = null;
        while (docDTO == null) {
            while (yExportableIterator == null || !yExportableIterator.hasNext()) {
                while (xmlPathIterator == null || !xmlPathIterator.hasNext()) {
                    if (listZipFiles == null || zipIndex >= listZipFiles.length) {
                        nextItem = null;
                        return;
                    }
                    // here we have a new zip file
                    try {
                    	System.out.println("Processing " + (zipIndex+1) + ". zip of " + listZipFiles.length);
                        currentZipArchive = new ZipArchive(listZipFiles[zipIndex].getPath());
                        xmls = currentZipArchive.filter(".*xml");
                        currentXml = 0;
                        xmlPathIterator = xmls.iterator();
                    } catch (IOException ex) {logger.error(ex.toString());}
                    zipIndex++;
                }
                // here we have a new xml path:
                String xmlPath = xmlPathIterator.next();
                try {
                    InputStream xmlIS = currentZipArchive.getFileAsInputStream(xmlPath);
                    yExportableIterator = MetadataToProtoMetadataParser.streamToYExportable(xmlIS, MetadataToProtoMetadataParser.MetadataType.BWMETA).iterator();
                    currentXml++;
                    if(currentXml % 10 == 0)System.out.println("\t"+new Date()+"\tProceeded " + currentXml + ". xml of " + xmls.size());
                } catch (IOException ex) {logger.error(ex.toString());}
            }
            // here we have an yExportable:
            docDTO = yElementFromZip2DocumentDTO.transformYElement(yExportableIterator.next(),currentZipArchive);
            if(docDTO != null) docDTO.setCollection(collection);
        }
        nextItem = docDTO;
    }

    private static class ZipFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".zip"));
        }
    }
}

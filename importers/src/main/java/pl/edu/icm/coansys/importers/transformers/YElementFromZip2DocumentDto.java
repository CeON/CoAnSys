/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.transformers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.importers.ZipArchive;
import pl.edu.icm.coansys.importers.constants.BWMetaConstants;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.parsers.MetadataToProtoMetadataParser;

import com.google.protobuf.ByteString;
import pl.edu.icm.model.bwmeta.YContentEntry;
import pl.edu.icm.model.bwmeta.YContentFile;
import pl.edu.icm.model.bwmeta.YElement;
import pl.edu.icm.model.bwmeta.YExportable;

/**
 *
 * @author pdendek
 *
 */
public class YElementFromZip2DocumentDto {

    private String collection;
    private static final Logger logger = LoggerFactory.getLogger(YElementFromZip2DocumentDto.class);

    public String getCollection() {
        return collection;
    }

    public YElementFromZip2DocumentDto(String collection) {
        this.collection = collection;
    }

    public DocumentDTO transformYElement(
            YExportable yExportable, ZipArchive currentZipArchive) {

        DocumentDTO productObject = null;

        if (yExportable instanceof YElement) {
            YElement yElement = (YElement) yExportable;

            DocumentMetadata docMetadata = MetadataToProtoMetadataParser.yelementToDocumentMetadata(yElement, collection);

            if (docMetadata != null) {
                productObject = new DocumentDTO();
                productObject.setKey(docMetadata.getKey()); //Document and DocumentMetadata should have the same key?
                productObject.setDocumentMetadata(docMetadata);

                List<YContentEntry> contents = yElement.getContents();
                for (YContentEntry content : contents) {
                    //get a pdf path from yElement
                    handleContent(productObject, content, currentZipArchive);
                }
            }
        }
        return productObject;
    }

    private void handleContent(DocumentDTO docDTO, YContentEntry content,
            ZipArchive currentZipArchive) {
        if (content.isFile()) {

            YContentFile yFile = (YContentFile) content;

            if (BWMetaConstants.mimePdfListExtension.contains(yFile.getFormat())) {
                handlePDFContent(docDTO, yFile, currentZipArchive);
            }
        }
    }

    private void handlePDFContent(DocumentDTO docDTO,
            YContentFile yFile, ZipArchive currentZipArchive) {
        for (String location : yFile.getLocations()) {
            //path to pdf in yFile contains prefix yadda.pack:/, check and remove it
            String prefix = "yadda.pack:/";
            if (location.startsWith(prefix)) {
                location = location.substring(prefix.length());
                //path to pdf in zip file contains zip filename, not included in yFile
                List<String> foundPaths = currentZipArchive.filter(".*" + location);
                //foundPaths should contain 1 item
                if (foundPaths.size() > 0) {
                    try {
                        InputStream pdfIS = currentZipArchive.getFileAsInputStream(foundPaths.get(0));
                        // ... do something with pdfIS
                        Media.Builder mediaBuilder = Media.newBuilder();
                        mediaBuilder.setKey(docDTO.getKey()); //Media and Document should have the same key?

                        String type = ProtoConstants.mediaTypePdf;
                        mediaBuilder.setMediaType(type);
                        mediaBuilder.setContent(ByteString.copyFrom(IOUtils.toByteArray(pdfIS)));
                        docDTO.addMedia(mediaBuilder.build());
                        docDTO.addMediaType(type);
                    } catch (IOException ex) {
                        logger.error(ex.toString());
                    }
                } else {
                    logger.error("File path in BWmeta, but not in archive: " + location);
                }
            }
        }
    }
}

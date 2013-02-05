/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.transformers;

import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.importers.constants.BWMetaConstants;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.parsers.MetadataToProtoMetadataParser;
import pl.edu.icm.coansys.importers.utils.ZipArchive;
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

    public DocumentDTO transformYElement(YExportable yExportable, ZipArchive currentZipArchive, String currentXmlPath) {

        DocumentDTO productObject = null;
        
        if (yExportable instanceof YElement) {
            YElement yElement = (YElement) yExportable;

            DocumentMetadata docMetadata = MetadataToProtoMetadataParser.yelementToDocumentMetadata(yElement, currentZipArchive, currentXmlPath, collection);

            if (docMetadata != null) {
                productObject = new DocumentDTO();
                productObject.setKey(docMetadata.getKey()); 
                //Document and DocumentMetadata should have the same key?
                productObject.setDocumentMetadata(docMetadata);

                List<YContentEntry> contents = yElement.getContents();
                for (YContentEntry content : contents) {
                    //get a media path from yElement
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
                fetchMediaFromZip(docDTO, yFile, currentZipArchive, ProtoConstants.mediaTypePdf);
            } else if (BWMetaConstants.mimeTxtListExtension.contains(yFile.getFormat())) {
                fetchMediaFromZip(docDTO, yFile, currentZipArchive, ProtoConstants.mediaTypeTxt);
            }
        }
    }

    private void fetchMediaFromZip(DocumentDTO docDTO, YContentFile yFile, ZipArchive currentZipArchive, String mediaType) {
        for (String location : yFile.getLocations()) {
            //path to media in yFile contains prefix yadda.pack:/, check and remove it
            String prefix = "yadda.pack:/";
            if (location.startsWith(prefix)) {
                location = location.substring(prefix.length());
                //path to media in zip file contains zip filename, not included in yFile
                List<String> foundPaths = currentZipArchive.filter(".*" + location);
                //foundPaths should contain 1 item
                if (foundPaths.size() > 0) {
                    try {
                        String foundPath = foundPaths.get(0);
                        InputStream mediaIS = currentZipArchive.getFileAsInputStream(foundPath);
                        // ... do something with mediaIS
                        Media.Builder mediaBuilder = Media.newBuilder();
                        mediaBuilder.setKey(docDTO.getKey()); 
                        //Media and Document should have the same key?
                        mediaBuilder.setMediaType(mediaType);
                        byte[] content = IOUtils.toByteArray(mediaIS);
                        mediaBuilder.setContent(ByteString.copyFrom(content));
                        mediaBuilder.setSourcePath(currentZipArchive.getZipFilePath() + "#" + foundPath);
                        mediaBuilder.setSourceFilesize(content.length);
                        
                        docDTO.addMedia(mediaBuilder.build());
                        docDTO.addMediaType(mediaType);
                        mediaIS.close();

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

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import pl.edu.icm.coansys.importers.ZipArchive;
import pl.edu.icm.coansys.importers.model.DocumentDTO;
import pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.model.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.parser.MetadataToProtoMetadataParser;
import pl.edu.icm.synat.application.model.bwmeta.YContentEntry;
import pl.edu.icm.synat.application.model.bwmeta.YContentFile;
import pl.edu.icm.synat.application.model.bwmeta.YElement;
import pl.edu.icm.synat.application.model.bwmeta.YExportable;

public class YElementFromZip2DocumentDto{

	protected ZipArchive currentZipArchive;
	protected String collection;
	private static final Logger logger = LoggerFactory.getLogger(YElementFromZip2DocumentDto.class);
	
	public String getCollection() {
		return collection;
	}

	public void setCurrentZipArchive(ZipArchive currentZipArchive) {
		this.currentZipArchive = currentZipArchive;
	}
	
	public ZipArchive getCurrentZipArchive() {
		return currentZipArchive;
	}

	public YElementFromZip2DocumentDto(String collection){
		this.collection = collection;
	}
	
	public DocumentDTO transformYElement(
			YExportable yExportable, ZipArchive currentZipArchive2) {
		
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
		            InputStream pdfIS = null;
		            //get a pdf path from yElement
		            handleContent(productObject, content, pdfIS,currentZipArchive);
		        }
		    }
		}
		return productObject;
	}

	private void handleContent(DocumentDTO docDTO, YContentEntry content,
			InputStream pdfIS, ZipArchive currentZipArchive2) {
		if (content.isFile()) {

		    YContentFile yFile = (YContentFile) content;

		    //supported format: PDF
		    //Here you can add support of other formats (see also setMediaType() below)
		    //TODO: pdf may be represented by other strings, add support
		    if ("application/pdf".equals(yFile.getFormat())) {
		        handlePDFContent(docDTO, pdfIS, yFile,currentZipArchive);
		    }
		}
	}

	private void handlePDFContent(DocumentDTO docDTO, InputStream pdfIS,
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
		                pdfIS = currentZipArchive.getFileAsInputStream(foundPaths.get(0));
		                // ... do something with pdfIS
		                Media.Builder mediaBuilder = Media.newBuilder();
		                mediaBuilder.setKey(docDTO.getKey()); //Media and Document should have the same key?
		                String type = "PDF";
		                mediaBuilder.setMediaType(type); //??
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

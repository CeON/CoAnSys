/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner;
import pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.model.DocumentProtos.Media;

/**
 * @author pdendek
 */
public class DocumentDTO {

	protected String collection;
//	protected String year;
	protected DocumentMetadata docMetadata;
	protected String key;
	protected MediaConteiner.Builder medias = MediaConteiner.newBuilder(); 
	protected ArrayList<String> mediaTypes = new ArrayList<String>();

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

//	public String getYear() {
//		return year;
//	}
//
//	public void setYear(String year) {
//		this.year = year;
//	}
	
	public DocumentDTO setDocumentMetadata(DocumentMetadata docMetadata) {
		this.docMetadata = docMetadata;
		return this;
	}

	public DocumentMetadata getDocumentMetadata() {
		return docMetadata;
	}
	
	public DocumentDTO setKey(String key) {
		this.key=key;
		return this;
	}

	public String getKey() {
		return key;
	}
	
	public DocumentDTO addMedia(Media media){
		this.medias.addMedia(media);
		return this;
	}
	
	public DocumentDTO addMedia(Collection medias){
		this.medias.addAllMedia(medias);
		return this;
	}

	public MediaConteiner getMediaConteiner(){
		return medias.build();
	}
	
	public DocumentDTO addMediaType(String type) {
		mediaTypes.add(type);
		return this;
	}
	
	public List<String> getMediaTypes(){
		return this.mediaTypes;
	}
}

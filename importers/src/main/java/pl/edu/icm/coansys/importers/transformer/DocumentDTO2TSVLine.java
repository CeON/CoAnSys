/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformer;

import pl.edu.icm.coansys.importers.model.DocumentDTO;



/**
 * @author pdendek
 */
public class DocumentDTO2TSVLine {
	
	public static final String separator = "################################";
	public static final String lineEnding = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n";
	
	public static String translate(DocumentDTO docDTO) {
		return composeRow(docDTO) + separator + docDTO.getDocumentMetadata() + separator +docDTO.getMediaConteiner() + lineEnding;
	}

	private static String composeRow(DocumentDTO docDTO) {
		StringBuilder sb = new StringBuilder();
		sb.append(docDTO.getCollection());
//		sb.append(docDTO.getYear());
		if(docDTO.getMediaTypes().size()>0)sb.append(1);
		else sb.append(0);
		sb.append("_");
		sb.append(docDTO.getKey());
		
		return sb.toString();
	}

}

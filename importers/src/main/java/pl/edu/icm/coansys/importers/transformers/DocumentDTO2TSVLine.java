/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformers;

import pl.edu.icm.coansys.importers.models.DocumentDTO;



/**
 * @author pdendek
 */
public class DocumentDTO2TSVLine {
	
	public static final String separator = "########";
	public static final String lineEnding = "@@@@@@@@";
	
	public static String translate(DocumentDTO docDTO) {
		return RowComposer.composeRow(docDTO) + separator + docDTO.getDocumentMetadata().toByteString().toStringUtf8() 
								  + separator + docDTO.getMediaConteiner().toByteString().toStringUtf8() + lineEnding;
	}
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformers;

import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentDTO;



/**
 * @author pdendek
 */
public class DocumentDTO2TSVLine {
	
	public static final String separator = "########";
	public static final String lineEnding = "@@@@@@@@";
	
	public static String translate(DocumentDTO docDTO) {
		return composeRow(docDTO) + separator + docDTO.getDocumentMetadata().toByteString().toStringUtf8() 
								  + separator + docDTO.getMediaConteiner().toByteString().toStringUtf8() + lineEnding;
	}

	private static String composeRow(DocumentDTO docDTO) {
		StringBuilder sb = new StringBuilder();
		sb.append(docDTO.getCollection());
		if(docDTO.getMediaTypes().size()>0) sb.append(HBaseConstant.rowIdSeparator);
		for(String t : docDTO.getMediaTypes()){
			if(ProtoConstants.mediaTypePdf.equals(t)){
				sb.append(HBaseConstant.rowIdMediaTypePdf);
				sb.append(HBaseConstant.rowIdSeparator);
			}
		}
		sb.append(HBaseConstant.rowIdSeparator);
		sb.append(docDTO.getKey());
		
		return sb.toString();
	}
}

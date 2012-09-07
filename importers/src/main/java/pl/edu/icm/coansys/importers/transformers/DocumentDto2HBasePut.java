/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformers;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.models.DocumentDTO;

/**
 * @author pdendek
 */
public class DocumentDto2HBasePut {
	
	public DocumentDto2HBasePut(){
		
	}
	
	public static List<Put> translate(DocumentDTO docDTO) {
		
		ArrayList<Put> puts = new ArrayList<Put>();
		
		byte[] row = Bytes.toBytes(RowComposer.composeRow(docDTO));
		
		puts.add(composeMetadataFamily(row, docDTO));
		puts.add(composeContentFamily(row, docDTO));
		
		return puts;
	}

	private static Put composeContentFamily(byte[] row, DocumentDTO docDTO) {
		
		Put p = new Put(row);
		byte[] family =  Bytes.toBytes(HBaseConstant.FAMILY_CONTENT);
		byte[] qualifier = Bytes.toBytes(HBaseConstant.FAMILY_CONTENT_QUALIFIER_PROTO);
		byte[] value = docDTO.getMediaConteiner().toByteArray();
		p.add(family, qualifier, value);
		return p;
	}

	private static Put composeMetadataFamily(byte[] row, DocumentDTO docDTO) {
		
		Put p = new Put(row);
		byte[] family =  Bytes.toBytes(HBaseConstant.FAMILY_METADATA);
		byte[] qualifier = Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO);
		byte[] value = docDTO.getDocumentMetadata().toByteArray();
		p.add(family, qualifier, value);
		return p;
	}
}

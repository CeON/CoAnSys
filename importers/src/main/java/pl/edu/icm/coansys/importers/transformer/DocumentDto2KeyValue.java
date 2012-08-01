/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.model.DocumentDTO;

public class DocumentDto2KeyValue {
	
	public DocumentDto2KeyValue(){
		
	}
	
	public static List<KeyValue> translate(DocumentDTO docDTO) {
		
		ArrayList<KeyValue> kvs = new ArrayList<KeyValue>();
		
		byte[] row = composeRow(docDTO);
		
		kvs.add(composeMetadataFamily(row, docDTO));
		kvs.add(composeContentFamily(row, docDTO));
		
		return kvs;
	}

	private static KeyValue composeContentFamily(byte[] row, DocumentDTO docDTO) {
		
		byte[] family =  Bytes.toBytes(HBaseConstant.familyContent);
		byte[] qualifier = Bytes.toBytes(HBaseConstant.familyContentQualifierProto);
		byte[] value = Bytes.toBytes(docDTO.getMediaConteiner().toString());
		
		return new KeyValue(row,family,qualifier, System.nanoTime(), value);
	}

	private static KeyValue composeMetadataFamily(byte[] row, DocumentDTO docDTO) {
		
		byte[] family =  Bytes.toBytes(HBaseConstant.familyMetadata);
		byte[] qualifier = Bytes.toBytes(HBaseConstant.familyMetadataQualifierProto);
		byte[] value = Bytes.toBytes(docDTO.getDocumentMetadata().toString());
		
		return new KeyValue(row,family,qualifier, System.nanoTime(), value);
	}

	private static byte[] composeRow(DocumentDTO docDTO) {
		StringBuilder sb = new StringBuilder();
		sb.append(docDTO.getCollection());
//		sb.append(docDTO.getYear());
		if(docDTO.getMediaTypes().size()>0)sb.append(1);
		else sb.append(0);
		sb.append("_");
		sb.append(docDTO.getKey());
		
		return Bytes.toBytes(sb.toString());
	}

}

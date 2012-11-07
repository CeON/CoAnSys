/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers.io.readers.hbaserest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;
import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;

/**
 * 
 * @author pdendek
 *
 */
public class HBaseRestReader_Bwmeta {
    
    private HBaseRestReader_Bwmeta(){}
	
	public static void main(String[] args) throws IOException{
		HashMap<String, List<String>> rowAuthorsMap = readAuthorsFromDocumentMetadataHBase("localhost", 8080, "testProto");
//		HashMap<String, List<String>> rowAuthorsMap = readPdfsFromDocumentMetadataHBase("localhost", 8080, "testProto");
		
		for(Entry<String, List<String>> e : rowAuthorsMap.entrySet()){
			for(String an : e.getValue()){
				System.out.println(e.getKey()+"\t\t"+an);
			}
		}
		
	}

	public static HashMap<String, List<String>> readPdfsFromDocumentMetadataHBase(String remoteHost, int remotePort, String remoteTable) throws IOException{

		RemoteHTable table = new RemoteHTable(
        		new Client(
        				new Cluster().add(remoteHost, remotePort)
        		), remoteTable
        	);
		
        ResultScanner scanner = table.getScanner(Bytes.toBytes(HBaseConstant.FAMILY_CONTENT), Bytes.toBytes(HBaseConstant.FAMILY_CONTENT_QUALIFIER_PROTO));
        
        HashMap<String, List<String>> rowAuthorsMap = new HashMap<String, List<String>>(); 
        
        try {
            for (Result scannerResult : scanner) {
            	String rowId = new String(scannerResult.getRow());
            	ArrayList<String> names = new ArrayList<String>();
            	
            	if(scannerResult.getValue(Bytes.toBytes(HBaseConstant.FAMILY_CONTENT), Bytes.toBytes(HBaseConstant.FAMILY_CONTENT_QUALIFIER_PROTO)) != null) {
            		MediaContainer mc = MediaContainer.parseFrom(scannerResult.value());
            		for(Media media : mc.getMediaList()){
            			names.add(media.getMediaType());
            		}
            		rowAuthorsMap.put(rowId, names);
                }else {
                    System.out.println("Parsing problem occured on row "+rowId);
                }
            }
        } finally {
            scanner.close();
        }
        return rowAuthorsMap;
	}	
	
	public static HashMap<String, List<String>> readAuthorsFromDocumentMetadataHBase(String remoteHost, int remotePort, String remoteTable) throws IOException{

		RemoteHTable table = new RemoteHTable(
        		new Client(
        				new Cluster().add(remoteHost, remotePort)
        		), remoteTable
        	);
		
        ResultScanner scanner = table.getScanner(Bytes.toBytes(HBaseConstant.FAMILY_METADATA), Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO));
        
        HashMap<String, List<String>> rowAuthorsMap = new HashMap<String, List<String>>(); 
        
        try {
            for (Result scannerResult : scanner) {
            	String rowId = new String(scannerResult.getRow());
            	ArrayList<String> names = new ArrayList<String>();
            	
            	if(scannerResult.getValue(Bytes.toBytes(HBaseConstant.FAMILY_METADATA), Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO)) != null) {
            		DocumentMetadata dm = DocumentMetadata.parseFrom(scannerResult.value());
            		for(Author a : dm.getAuthorList()){
            			names.add(a.getForenames() + " " + a.getSurname());
            		}
            		rowAuthorsMap.put(rowId, names);
                }else {
                    System.out.println("Parsing problem occured on row "+rowId);
                }
            }
        } finally {
            scanner.close();
        }
        return rowAuthorsMap;
	}
}

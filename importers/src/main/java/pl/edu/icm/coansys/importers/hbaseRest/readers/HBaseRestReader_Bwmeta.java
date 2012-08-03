/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers.hbaseRest.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;


import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person;
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.model.DocumentDTO;
import pl.edu.icm.coansys.importers.model.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.model.DocumentProtos.Media;
import pl.edu.icm.coansys.importers.model.DocumentProtos.MediaConteiner;
import pl.edu.icm.coansys.importers.transformer.DocumentDto2HBasePut;

/**
 * 
 * @author pdendek
 *
 */
public class HBaseRestReader_Bwmeta {
	
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
		
		Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(Bytes.toBytes(HBaseConstant.familyContent), Bytes.toBytes(HBaseConstant.familyContentQualifierProto));
        
        HashMap<String, List<String>> rowAuthorsMap = new HashMap<String, List<String>>(); 
        
        try {
            for (Result scannerResult : scanner) {
            	String rowId = new String(scannerResult.getRow());
            	ArrayList<String> names = new ArrayList<String>();
            	
            	if(scannerResult.getValue(Bytes.toBytes(HBaseConstant.familyContent), Bytes.toBytes(HBaseConstant.familyContentQualifierProto)) != null) {
            		MediaConteiner mc = MediaConteiner.parseFrom(scannerResult.value());
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
		
		Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(Bytes.toBytes(HBaseConstant.familyMetadata), Bytes.toBytes(HBaseConstant.familyMetadataQualifierProto));
        
        HashMap<String, List<String>> rowAuthorsMap = new HashMap<String, List<String>>(); 
        
        try {
            for (Result scannerResult : scanner) {
            	String rowId = new String(scannerResult.getRow());
            	ArrayList<String> names = new ArrayList<String>();
            	
            	if(scannerResult.getValue(Bytes.toBytes(HBaseConstant.familyMetadata), Bytes.toBytes(HBaseConstant.familyMetadataQualifierProto)) != null) {
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

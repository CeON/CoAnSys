/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers.hbaseRest.writer;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;


import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.hbaseRest.model_toy.AddressBookProtos.Person;
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.model.DocumentDTO;
import pl.edu.icm.coansys.importers.transformer.DocumentDto2HBasePut;

/**
 * 
 * @author pdendek
 *
 */
public class HBaseRestWriter_Bwmeta {
	
	public static void main(String[] args) throws IOException{
		String zipDirPath = new HBaseRestWriter_Bwmeta().getClass().getClassLoader().getResource("zipdir").getPath();
		importBwmetaAndSendToHBase(zipDirPath, "TESTCOLLECTION", "localhost", 8080, "testProto");
	}
	
	public static void importBwmetaAndSendToHBase(String zipDirPath, String collectionName, String remoteHost, int remotePort, String remoteTable) throws IOException{

		RemoteHTable table = new RemoteHTable(
        		new Client(
        				new Cluster().add(remoteHost, remotePort)
        		), remoteTable
        	);
		
		ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(zipDirPath, collectionName);
		for(DocumentDTO doc : zdtp){
			try {
				table.put(DocumentDto2HBasePut.translate(doc));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

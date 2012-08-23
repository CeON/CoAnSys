/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers.io.writers.hbaserest;

import java.io.IOException;

import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;

import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.transformers.DocumentDto2HBasePut;

/**
 * 
 * @author pdendek
 *
 */
public class HBaseRestWriter_Bwmeta {
	
	public static void main(String[] args) throws IOException{
		
		if(args == null || args.length!=5){
			printHelp();
			args = new String[5];
			ClassLoader cl = new HBaseRestWriter_Bwmeta().getClass().getClassLoader(); 
			args[0] = cl.getResource("pl/edu/icm/coansys/importers/hbaseRest/writer/ekonCollectionOneFullBwmeta").getPath();
			args[1] = "TESTCOLLECTION";
			args[2] = "localhost";
			args[3] = "8080";
			args[4] = "testProto";
		}
		importBwmetaAndSendToHBase(args[0], args[1],args[2],Integer.parseInt(args[3]),args[4]);
	}

	private static void printHelp() {
		System.out.println("# of parameters not equal to 5");
		System.out.println("You need to provide:");
		System.out.println("* path to directory containing bwmeta zips");
		System.out.println("* the collection name (without spaces");
		System.out.println("* the remote host addres");
		System.out.println("* the remote host port");
		System.out.println("* the remote host htable name");
		System.out.println("");
		System.out.println("Default values will be used:");
		System.out.println("* src/main/resources/zipdir");
		System.out.println("* TESTCOLLECTION");
		System.out.println("* localhost");
		System.out.println("* 8080");
		System.out.println("* testProto");
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

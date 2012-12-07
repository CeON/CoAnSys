/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.io.writers.hbaserest;

import java.io.IOException;

import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;

import pl.edu.icm.coansys.importers.iterators.OAIPMHDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.transformers.DocumentDTO2HBasePut;

/**
 *
 * @author pdendek
 *
 */
public class HBaseRestWriter_OAIPMH {
    private HBaseRestWriter_OAIPMH() {
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 4) {
            importBwmetaAndSendToHBase(args[0], args[1], Integer.parseInt(args[2]), args[3], null);
        } else if (args.length == 5) {
            importBwmetaAndSendToHBase(args[0], args[1], Integer.parseInt(args[2]), args[3], args[4]);
        } else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("# of parameters not equal to 4 or 5");
        System.out.println("You need to provide:");
        System.out.println("* path to directory containing OAI-PMH harvested data");
        System.out.println("* the remote host addres");
        System.out.println("* the remote host port");
        System.out.println("* the remote host htable name");
        System.out.println("* (optional) the collection name (without spaces");
    }

    public static void importBwmetaAndSendToHBase(String zipDirPath, String remoteHost, int remotePort, String remoteTable, String collectionName) throws IOException {
        RemoteHTable table = new RemoteHTable(
                new Client(
                new Cluster().add(remoteHost, remotePort)), remoteTable);

        Iterable<DocumentDTO> zdtp;
        if (collectionName != null) {
            zdtp = new OAIPMHDirToDocumentDTOIterator(zipDirPath, collectionName);
        } else {
            zdtp = new OAIPMHDirToDocumentDTOIterator(zipDirPath);
        }
        for (DocumentDTO doc : zdtp) {
            try {
                table.put(DocumentDTO2HBasePut.translate(doc));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

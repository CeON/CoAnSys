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
import pl.edu.icm.coansys.importers.transformers.DocumentDto2HBasePut;

/**
 *
 * @author pdendek
 *
 */
public class HBaseRestWriter_OAIPMH {

    private HBaseRestWriter_OAIPMH() {
    }

    public static void main(String[] args) throws IOException {

        if (args == null || args.length != 5) {
            printHelp();
            return;
        }
        importBwmetaAndSendToHBase(args[0], args[1], args[2], Integer.parseInt(args[3]), args[4]);
    }

    private static void printHelp() {
        System.out.println("# of parameters not equal to 5");
        System.out.println("You need to provide:");
        System.out.println("* path to directory containing OAI-PMH harvested data");
        System.out.println("* the collection name (without spaces");
        System.out.println("* the remote host addres");
        System.out.println("* the remote host port");
        System.out.println("* the remote host htable name");
    }

    public static void importBwmetaAndSendToHBase(String zipDirPath, String collectionName, String remoteHost, int remotePort, String remoteTable) throws IOException {

        RemoteHTable table = new RemoteHTable(
                new Client(
                new Cluster().add(remoteHost, remotePort)), remoteTable);

        Iterable<DocumentDTO> zdtp = new OAIPMHDirToDocumentDTOIterator(zipDirPath, collectionName);
        for (DocumentDTO doc : zdtp) {
            try {
                table.put(DocumentDto2HBasePut.translate(doc));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

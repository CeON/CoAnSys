/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.io.writers.hbaserest;

import java.io.IOException;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import pl.edu.icm.coansys.importers.iterators.PdfZipDirToDocumentDTOIterable;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.transformers.DocumentDTO2HBasePut;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class HBaseRestWriter_PDF {

    private HBaseRestWriter_PDF() {
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 5) {
            importBwmetaAndSendToHBase(args[0], args[1], Integer.parseInt(args[2]), args[3], args[4]);
        } else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.err.println("# of parameters not equal to 5");
        System.err.println("You need to provide:");
        System.err.println("* path to directory containing OAI-PMH harvested data");
        System.err.println("* the remote host addres");
        System.err.println("* the remote host port");
        System.err.println("* the remote host htable name");
        System.err.println("* the collection name (without spaces");
    }

    public static void importBwmetaAndSendToHBase(String zipDirPath, String remoteHost, int remotePort, String remoteTable, String collectionName) throws IOException {
        RemoteHTable table = new RemoteHTable(
                new Client(
                new Cluster().add(remoteHost, remotePort)), remoteTable);

        Iterable<DocumentDTO> zdtp = new PdfZipDirToDocumentDTOIterable(zipDirPath, collectionName);

        for (DocumentDTO doc : zdtp) {
            try {
                table.put(DocumentDTO2HBasePut.translate(doc));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

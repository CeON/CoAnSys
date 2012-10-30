/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.io.readers.hbaserest;

import java.io.IOException;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;
import pl.edu.icm.coansys.importers.models.AddressBookProtos.Person;

/**
 *
 * @author pdendek
 *
 */
public class HBaseRestReader_Toy {

    private HBaseRestReader_Toy() {
    }

    public static void main(String[] args) throws IOException {
        readDocumentMetadataHBase("localhost", 8080, "test");
    }

    public static void readDocumentMetadataHBase(String remoteHost, int remotePort, String remoteTable) throws IOException {

        RemoteHTable table = new RemoteHTable(
                new Client(
                new Cluster().add(remoteHost, remotePort)), remoteTable);

        Person john =
                Person.newBuilder().setId(1234).setName("John Doe").setEmail("jdoe@example.com").addPhone(
                Person.PhoneNumber.newBuilder().setNumber("555-4321").setType(Person.PhoneType.HOME)).build();

        System.out.println("============ inside code ============");
        byte[] value = john.toByteArray();
        Person p = Person.parseFrom(value);
        System.out.println(p);
        System.out.println("============ from hbase ============");
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);



        try {
            for (Result scannerResult : scanner) {
                if (scannerResult.getValue(Bytes.toBytes("data"), Bytes.toBytes("3")) != null) {
                    System.out.println("Scan: " + Person.parseFrom(scannerResult.value()));
                    System.out.println("!!!! Is it equals?: " + john.equals(Person.parseFrom(scannerResult.value())));
                } else {
                    System.out.println("Scan: " + Bytes.toString(scannerResult.value()));
                }
            }
        } finally {
            scanner.close();
        }
    }
}

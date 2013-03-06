/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.io.deleters.hbaserest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.hadoop.hbase.rest.client.RemoteHTable;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author pdendek
 *
 */
public class HBaseRestDeleter {

    private HBaseRestDeleter() {
    }

    public static void main(String[] args) throws IOException {
        deleteRowFromHBase("localhost", 8080, "test", "row3");
    }

    public static void deleteRowFromHBase(String remoteHost, int remotePort, String remoteTable, String rowToDelete) throws IOException {

        RemoteHTable table = new RemoteHTable(
                new Client(
                new Cluster().add(remoteHost, remotePort)), remoteTable);

        byte[] row3 = Bytes.toBytes(rowToDelete);
        table.delete(new Delete(row3));
    }

    public static void deleteRowFromHBase(String remoteHost, int remotePort, String remoteTable, List<String> rowsToDelete) throws IOException {

        RemoteHTable table = new RemoteHTable(
                new Client(
                new Cluster().add(remoteHost, remotePort)), remoteTable);

        List<byte[]> rowBytes = new ArrayList<byte[]>();
        for (String s : rowsToDelete) {
            rowBytes.add(Bytes.toBytes(s));
        }

        for (byte[] b : rowBytes) {
            table.delete(new Delete(b));
        }

    }
}

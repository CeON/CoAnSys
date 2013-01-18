
package pl.edu.icm.coansys.importers.utils;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 *
 * @author acz
 */
public class HBaseUtils {

    private HBaseAdmin hbadm;
    
    public HBaseUtils() throws MasterNotRunningException, ZooKeeperConnectionException {
        hbadm = new HBaseAdmin(new Configuration());
    }
    
    public HBaseUtils(Configuration conf) throws MasterNotRunningException, ZooKeeperConnectionException {
        hbadm = new HBaseAdmin(conf);
    }
    
    public void createTable(String tableName, String... columnFamily) throws IOException {
        HTableDescriptor tbldescr = new HTableDescriptor(tableName);
        for (String cf : columnFamily) {
            tbldescr.addFamily(new HColumnDescriptor(cf));
        }
        hbadm.createTable(tbldescr);
    }
    
    public void dropTable(String tableName) throws IOException {
        hbadm.disableTable(tableName);
        hbadm.deleteTable(tableName);
    }
            
    
}

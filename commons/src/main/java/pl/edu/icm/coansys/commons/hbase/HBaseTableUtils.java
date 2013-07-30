/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.hbase;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import pl.edu.icm.coansys.commons.oozie.OozieWorkflowUtils;

/**
 *
 * @author akawa
 */
public final class HBaseTableUtils {

    private static final String CMD_EXIST = "EXIST";
    private static final String CMD_DROP = "DROP";
    private static final String CMD_TRUNCATE = "TRUNCATE";
    private static final String CMD_CREATE = "CREATE";
    private static final String CMD_RECREATE = "DROPCREATE";

    private HBaseTableUtils() {
    }

    public static boolean isTableCreated(HBaseAdmin admin, String tableName) throws IOException {
        return admin.tableExists(tableName);
    }

    public static boolean dropTable(HBaseAdmin admin, String tableName) throws IOException {
        if (isTableCreated(admin, tableName)) {
            if (admin.isTableEnabled(tableName)) {
                admin.disableTable(tableName);
            }
            admin.deleteTable(tableName);
        }
        return true;
    }

    public static boolean truncateTable(HBaseAdmin admin, String tableName) throws IOException {
        HTableDescriptor htableDescriptor;
        if (isTableCreated(admin, tableName)) {
            htableDescriptor = admin.getTableDescriptor(Bytes.toBytes(tableName));
            dropTable(admin, tableName);
        } else {
            htableDescriptor = new HTableDescriptor(Bytes.toBytes(tableName));
        }
        
        // Recreate the talbe
        admin.createTable(htableDescriptor);
        return true;
    }

    public static boolean createSimpleTable(HBaseAdmin admin, String tableName, String[] columnFamilies) throws IOException {
        if (isTableCreated(admin, tableName)) {
            return false;
        }

        HTableDescriptor tableDescriptor = new HTableDescriptor(Bytes.toBytes(tableName));
        if (columnFamilies != null && columnFamilies.length > 0) {
            for (String columnFamily : columnFamilies) {
                HColumnDescriptor columnFamilyDescriptor = new HColumnDescriptor(Bytes.toBytes(columnFamily));
                tableDescriptor.addFamily(columnFamilyDescriptor);
            }
        }
        admin.createTable(tableDescriptor);
        return true;
    }

    public static boolean dropAndCreateSimpleTable(HBaseAdmin admin, String tableName, String[] columnFamilies) throws IOException {
        if (isTableCreated(admin, tableName)) {
            dropTable(admin, tableName);
        }

        return createSimpleTable(admin, tableName, columnFamilies);
    }

    private static String[] shiftArray(String[] args, int shift) {
        if (args.length > shift) {
            String[] shiftedArgs = new String[args.length - shift];
            for (int i = 0; i < shiftedArgs.length; ++i) {
                shiftedArgs[i] = args[i + shift];
            }
            return shiftedArgs;
        }
        return null;
    }

    public static void main(String[] args) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
        Configuration conf = HBaseConfiguration.create();
        HBaseAdmin admin = new HBaseAdmin(conf);

        boolean isOutputCaptured = Boolean.parseBoolean(args[0]);
        String command = args[1];
        String tableName = args[2];

        boolean success = false;
        if (command.equals(CMD_EXIST)) {
            success = isTableCreated(admin, tableName);
        } else if (command.equals(CMD_DROP)) {
            success = dropTable(admin, tableName);
        } else if (command.equals(CMD_TRUNCATE)) {
            success = truncateTable(admin, tableName);
        } else if (command.equals(CMD_CREATE)) {
            success = createSimpleTable(admin, tableName, shiftArray(args, 3));
        } else if (command.equals(CMD_RECREATE)) {
            success = dropAndCreateSimpleTable(admin, tableName, shiftArray(args, 3));
        } else {
            throw new IllegalArgumentException();
        }

        if (isOutputCaptured) {
            OozieWorkflowUtils.captureOutput("exit.value", Boolean.toString(success));
        }
    }
}

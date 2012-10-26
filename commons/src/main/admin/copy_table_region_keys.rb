include Java
 
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HTableDescriptor

base_table_name = ARGV[0] 
table_name = ARGV[1]
 
base_table = HTable.new(base_table_name)
region_start_keys = base_table.getStartKeys()
base_table.close()
 
admin = HBaseAdmin.new(Configuration.new())
table_descriptor = admin.getTableDescriptor(Bytes.toBytes(base_table_name))
table_descriptor.setName(Bytes.toBytes(table_name))
admin.createTable(table_descriptor, region_start_keys)

#
# Usage: hbase org.jruby.Main filename.rb
#

include Java

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.util.Bytes

conf = HBaseConfiguration.new
admin = HBaseAdmin.new conf
tables = admin.listTables
split_threshold = 4000


puts "SPLITING..."
status = admin.clusterStatus
status.servers.each do |server|
	load = status.getLoad server
	regions_load = load.regionsLoad
	regions_load.each do |key, region|
        	region_name = region.nameAsString
		size = region.storefileSizeMB
        	puts "#{region_name}\t#{size}"
		if (size > split_threshold) 
			puts "Spliting on region " + region_name + " asynchronously"
			admin.split region_name
			puts "Sleeping for 60 seconds... Zzzzz"
			sleep 60
		end
	end
end



puts "MAJOR COMPACTION..."
for table in tables
	table_name = table.getNameAsString
	puts "Major compaction on table " + table_name + " asynchronously.."
	admin.majorCompact table_name
	puts "Sleeping for 120 seconds... Zzzzz"
	sleep 120
end

exit

#
# Usage: hbase org.jruby.Main merge_regions.rb
#

include Java
import org.apache.hadoop.hbase.ClusterStatus
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.HServerInfo
import org.apache.hadoop.hbase.HServerLoad
import org.apache.hadoop.hbase.HTableDescriptor
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.util.Merge

def main(table, threshold, region_threshold)
	conf = HBaseConfiguration.new()
    	client = HBaseAdmin.new(conf)
	merge = Merge.new(conf)

	region_map = {}
	region_map.default = {}
    	status = client.clusterStatus
    	status.serverInfo.each do |server|
        	server_name = server.serverName

		load = status.getLoad server
        	regions_load = load.regionsLoad
        	regions_load.each do |key, region|
            		region_name = region.nameAsString
			size = region.storefileSizeMB
	    		requests = region.requestsCount
			print region_name + " size = "
			print size	
			puts
			region_map[region_name] = size
        	end
    	end

	puts "Press enter when HBase is stopped (otherwise merge fails)"
	STDIN.gets

	mergedCnt = 0
	prev_table_name = ""	
	i = 2
	region_map_sorted = region_map.sort{|a,b| a[0] <=> b[0]}
	while i < region_map_sorted.size do
        	region_name = region_map_sorted[i][0]
		size = region_map_sorted[i][1]
		table_name = region_name.split(',')[0]

		prev_table_name = region_map_sorted[i-1][0].split(',')[0]
		prev_size = region_map_sorted[i-1][1]
		prev_region_name = region_map_sorted[i-1][0]

                if (((table == table_name) && (table_name <=> prev_table_name) === 0) && (size < region_threshold || (prev_size +  size < threshold)))
                        print "Megring: " + prev_region_name + " and " + region_name
                        puts
			merge.run [table_name, prev_region_name, region_name].to_java :String
			i = i + 1;
			mergedCnt = mergedCnt + 1
			print "Sleeping for 30 seconds... Zzzz.."
                        puts
                        sleep 30
                end
		
		i = i + 1;
	end

	print "Merged " + mergedCnt.to_s + " pairs of adjacent regions"
	puts
end

table = ARGV[0]
threshold = ARGV[1].to_i
region_threshold = ARGV[2].to_i
main(table, threshold, region_threshold)

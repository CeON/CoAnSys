# This ruby script dumps a text file of region sizes and the servers
# they are on, for determining balance/split effectiveness.
#
# Usage: hbase org.jruby.Main split_regions.rb 1000000
#

include Java
import org.apache.hadoop.hbase.ClusterStatus
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.HServerInfo
import org.apache.hadoop.hbase.HServerLoad
import org.apache.hadoop.hbase.HTableDescriptor
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.util.Merge
import org.apache.hadoop.hbase.util.RegionSplitter

def main(threshold = 100000)
	conf = HBaseConfiguration.new()
    	admin = HBaseAdmin.new(conf)
	merge = Merge.new(conf)

	region_map = {}
	region_map.default = {}
    	status = admin.clusterStatus
    	status.serverInfo.each do |server|
        	server_name = server.serverName

		load = status.getLoad server
        	regions_load = load.regionsLoad
        	regions_load.each do |key, region|
            		region_name = region.nameAsString
			size = region.storefileSizeMB
	    		read_requests = region.writeRequestsCount
	    		write_requests = region.readRequestsCount
	    		requests = region.requestsCount
		
			region_map[region_name] = size
			if (threshold > size)
				admin.split region_name
			end
        	end
    	end
end

threshold = ARGV[0].to_i 
main(threshold)

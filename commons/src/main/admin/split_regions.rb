#
# Usage: hbase org.jruby.Main split_regions.rb 500000
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

def main(table, threshold = 500000)
        conf = HBaseConfiguration.new()
        admin = HBaseAdmin.new(conf)

        region_map = {}
        region_map.default = {}
        status = admin.clusterStatus
	status.serverInfo.each do |server|
                server_name = server.serverName

                load = status.getLoad server
                regions_load = load.regionsLoad
                regions_load.each do |key, region|
                        region_name = region.nameAsString
			table_name = region_name.split(',')[0]
                        size = region.storefileSizeMB
                        read_requests = region.writeRequestsCount
                        write_requests = region.readRequestsCount
                        requests = region.requestsCount

                        region_map[region_name] = size
                        if ((table == table_name) && (requests > threshold))
				print "Splitting " + region_name
				puts
                                admin.split region_name
				print "Sleeping for 15 seconds... Zzzz.."
				puts
				sleep 15
                        end
                end
        end
end

table = ARGV[0]
threshold = ARGV[1].to_i
main(table, threshold)

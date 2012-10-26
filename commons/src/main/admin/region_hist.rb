# This ruby script dumps a text file of region sizes and the servers
# they are on, for determining balance/split effectiveness.
#
# Usage: hbase org.jruby.Main region_hist.rb
#

include Java
import org.apache.hadoop.hbase.ClusterStatus
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.HServerInfo
import org.apache.hadoop.hbase.HServerLoad
import org.apache.hadoop.hbase.HTableDescriptor
import org.apache.hadoop.hbase.client.HBaseAdmin

def main()
	output_file = ARGV[0]
    	f = File.open(output_file, 'w')
	conf = HBaseConfiguration.new()
    	client = HBaseAdmin.new(conf)

    	status = client.clusterStatus
    	status.serverInfo.each do |server|
        server_name = server.serverName
        printed_server = false

	load = status.getLoad server
        regions_load = load.regionsLoad
        regions_load.each do |key, region|
            region_name = region.nameAsString
            size = region.storefileSizeMB
	    read_requests = region.writeRequestsCount
	    write_requests = region.readRequestsCount
	    requests = region.requestsCount
            f.puts "#{server_name}\t#{region_name}\t#{size}\t#{requests}\t#{write_requests}\t#{read_requests}"
            printed_server = true
        end
        if !printed_server then
            puts "#{server_name}\tNONE\t0"
        end
    end
end

main()

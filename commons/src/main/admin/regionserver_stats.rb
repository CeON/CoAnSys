# This ruby script dumps a text file of region sizes and the servers
# they are on, for determining balance/split effectiveness.
#
# Usage: hbase org.jruby.Main regionserver_stats.rb
#

include Java
import org.apache.hadoop.hbase.ClusterStatus
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.HServerInfo
import org.apache.hadoop.hbase.HServerLoad
import org.apache.hadoop.hbase.HTableDescriptor
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.util.Bytes

def main()
	conf = HBaseConfiguration.new()
    	client = HBaseAdmin.new(conf)

	majorCompactionSize = 1024 * 10

	totalStorefileCnt = 0
	totalRegionCnt = 0
	totalStoreCnt = 0

    	status = client.clusterStatus
	puts
    	status.serverInfo.each do |server|
        	server_name = server.serverName
        	printed_server = false

		load = status.getLoad server
		
		regionCnt = load.numberOfRegions
		storefileSize = load.storefileSizeInMB
		storefileCnt = load.storefiles
		requestSecondCnt = load.numberOfRequests
		memStoreSize = load.memStoreSizeInMB
		storeCnt = 0
		requestCnt = 0

        	regions_load = load.regionsLoad
        	regions_load.each do |key, region|
			storeCnt += region.stores
	    		requestCnt += region.requestsCount

           		region_name = region.nameAsString
			table_name = region_name.split(',')[0]
            		printed_server = true

			regionStoreCnt = region.stores
			regionStorefileCnt = region.storefiles
			regionStorefileSizeMB = region.storefileSizeMB
			if (regionStorefileCnt > regionStoreCnt && regionStorefileSizeMB > majorCompactionSize)
				puts " -- Candidate for major compaction: #{server_name}\t#{region_name}\tSC:#{regionStoreCnt}\tSFC:#{regionStorefileCnt}\tSFS:#{regionStorefileSizeMB}MB"
				# admin.majorCompact(region_name)
			end
        	end

		totalStorefileCnt += storefileCnt
		totalRegionCnt += regionCnt
		totalStoreCnt += storeCnt

        	if !printed_server then
            		puts "#{server_name}\tNONE\t0"
        	end
            	puts "#{server_name}\tRC:#{regionCnt}\tMSS:#{memStoreSize}\tSC:#{storeCnt}\tSFC:#{storefileCnt}\tSFS:#{storefileSize / 1024}\tRQSC:#{requestSecondCnt}\tRQC:#{requestCnt}"
    	end

	puts
	puts "Total\tSFC:#{totalStorefileCnt}\tTSC:#{totalStoreCnt}"
	puts
	puts "RC = Region Count"
	puts "MSS = MemStore Size in MB"
	puts "SC = Store Cnt"
	puts "SFC = Storefile Cnt"
	puts "SFS = Storefile Size in GB" 
	puts "RQSC = Request Per Second Cnt"
	puts "RQC = Request Cnt"
end

main()

--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

-- -----------------------------------------------------
-- -----------------------------------------------------
-- macros section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE getProtosFromHbase(tableName,auxil) RETURNS idProtoTab {
	$idProtoTab = LOAD 'hbase://$tableName' 
	  	USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto','-loadKey true') 
	  	AS (id:chararray, proto:bytearray);
};

DEFINE getBWBWFromHDFS(localization,method) RETURNS val {
	$val = LOAD '$localization' USING $method('org.apache.hadoop.io.BytesWritable', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
};

DEFINE getLocal(localization,auxil) RETURNS docsimOutput {
	$docsimOutput = LOAD $localization using PigStorage();
};

--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

-- -----------------------------------------------------
-- -----------------------------------------------------
-- macros section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE getProtosFromHbase(tableName) RETURNS idProtoTab {
	$idProtoTab = LOAD 'hbase://$tableName' 
	  	USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto','-loadKey true') 
	  	AS (id:chararray, proto:bytearray);
};

DEFINE getLocal(localization) RETURNS docsimOutput {
	$docsimOutput = LOAD $localization using PigStorage();
};

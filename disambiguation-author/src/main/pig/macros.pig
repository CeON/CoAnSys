-------------------------------------------------------
-- load BWMeta documents form sequence files stored in hdfs
-------------------------------------------------------
DEFINE load_bwndata_hdfs(inputPath) RETURNS doc {
        raw_bytes = LOAD '$inputPath' USING pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader();
        raw_doc = FOREACH raw_bytes GENERATE
                        pl.edu.icm.coansys.importers.pig.udf.BytesToDataByteArray($0) AS rowkey,
                        FLATTEN(pl.edu.icm.coansys.importers.pig.udf.DocumentComponentsProtoTupler($1)) AS (docId, mproto, cproto);

        $doc = FOREACH raw_doc GENERATE rowkey, pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple(mproto, cproto) AS document;
};

-------------------------------------------------------
-- load BWMeta metadata form sequence files stored in hdfs
-------------------------------------------------------
DEFINE load_bwndata_metadata_hdfs(inputPath) RETURNS meta {
        raw_bytes = LOAD '$inputPath' USING pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader();
        raw_meta = FOREACH raw_bytes GENERATE
                        pl.edu.icm.coansys.importers.pig.udf.BytesToDataByteArray($0) AS rowkey,
                        pl.edu.icm.coansys.importers.pig.udf.BytesToDataByteArray($1) AS mproto;

        $meta = FOREACH raw_meta
                GENERATE rowkey, pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple(mproto) AS document;
};


-------------------------------------------------------
-- load BWMeta documents form HBase tabls that contains
-------------------------------------------------------
DEFINE load_bwndata(tableName) RETURNS doc {
	raw_doc = LOAD 'hbase://$tableName' 
		USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('c:cproto, m:mproto', '-loadKey true')
		AS (rowkey: bytearray, cproto: bytearray, mproto: bytearray);
	
	$doc = FOREACH raw_doc 
                GENERATE rowkey, pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple(mproto, cproto) AS document;
};

-------------------------------------------------------
-- load BWMeta documents form HBase tabls that contains
-------------------------------------------------------
DEFINE load_bwndata_metadata(tableName) RETURNS doc {
	raw_doc = LOAD 'hbase://$tableName' 
		USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto', '-loadKey true -caching 1000')
		AS (rowkey: bytearray, mproto: bytearray);
	
	$doc = FOREACH raw_doc 
                GENERATE rowkey, pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple(mproto) AS document;
};

-------------------------------------------------------
-- filer out nulls
-------------------------------------------------------
DEFINE drop_nulls(A, column) RETURNS B {
	$B = FILTER $A BY $A.$column IS NOT NULL;
};

-------------------------------------------------------
-- distinct 
-------------------------------------------------------
DEFINE get_distinct(A, column1, column2) RETURNS unique {
	B = FOREACH $A GENERATE $A.$column1, $A.$column2;
	$unique = DISTINCT B;
};

-------------------------------------------------------
-- copy 
-------------------------------------------------------
DEFINE get_copy(A) RETURNS B {
	$B = FOREACH $A GENERATE *;
};

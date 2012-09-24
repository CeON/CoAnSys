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
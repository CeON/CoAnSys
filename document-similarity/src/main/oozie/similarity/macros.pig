-------------------------------------------------------
-- load BWMeta documents form HBase tabls that contains
-------------------------------------------------------
DEFINE load_bwndata(tableName) RETURNS doc {
	raw_doc = LOAD 'hbase://$tableName' 
		USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('c:cproto, m:mproto', '-loadKey true')
		AS (rowkey: bytearray, cproto: bytearray, mproto: bytearray);
	
	$doc = FOREACH raw_doc GENERATE rowkey, DocumentProtobufBytesToTuple(mproto, cproto) AS document;
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

-------------------------------------------------------
-- 
-------------------------------------------------------
DEFINE calculate_tf_idf(docTerm) RETURNS tfidf {
	-- term count in a given document
	A1 = GROUP $docTerm BY (docId, term);
	A = FOREACH A1 GENERATE FLATTEN(group), COUNT($docTerm) as tc;
		
	-- total term count in a given document
	B1 = GROUP A BY docId;
	B = FOREACH B1 GENERATE FLATTEN(A) AS (docId, term, tc), SUM(A.tc) AS ttc;
	
	-- total number of documents in the corpus
	C1 = GROUP B BY docId;
	C2 = GROUP C1 all;
	C = FOREACH C2 GENERATE FLATTEN(C1), COUNT(C1) AS dc;
	D = FOREACH C GENERATE FLATTEN(B) AS (docId, term, tc, ttc), dc;
	
	-- total number of documents that a given word occurs in
	E1 = GROUP D BY term;
	E = FOREACH E1 GENERATE FLATTEN(D) AS (docId, term, tc, ttc, dc), COUNT(D) AS ttdc;
	
	$tfidf = FOREACH E GENERATE docId, term, ((double) tc / (double) ttc) * LOG( (1.0 + (double) dc) / ( 1.0 + (double) ttdc)) AS tfidf;
};


-------------------------------------------------------
-- load BWMeta documents form sequence files stored in hdfs
-------------------------------------------------------
DEFINE load_bwndata_hdfs(inputPath, sampling) RETURNS doc {
	raw_bytes = LOAD '$inputPath' USING pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader();	
	raw_bytes_sample = SAMPLE raw_bytes $sampling;
	raw_doc = FOREACH raw_bytes_sample GENERATE 
			pl.edu.icm.coansys.importers.pig.udf.BytesToDataByteArray($0) AS rowkey, 
			FLATTEN(pl.edu.icm.coansys.importers.pig.udf.DocumentComponentsProtoTupler($1)) AS (docId, mproto, cproto);
	
	$doc = FOREACH raw_doc GENERATE rowkey, pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple(mproto, cproto) AS document;
};

-------------------------------------------------------
-- load BWMeta metadata form sequence files stored in hdfs
-------------------------------------------------------
DEFINE load_bwndata_metadata_hdfs(inputPath, sampling) RETURNS meta {
	raw_bytes = LOAD '$inputPath' USING pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader();
	raw_bytes_sample = SAMPLE raw_bytes $sampling;
	raw_meta = FOREACH raw_bytes_sample GENERATE 
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
		USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto, c:cproto', '-loadKey true -caching 50 -limit 100')
		AS (rowkey: bytearray, mproto: bytearray, cproto: bytearray);
	
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
-- clean and drop nulls
-------------------------------------------------------
DEFINE stem_and_filter_out(docterms, type) RETURNS dt {
	doc_keyword_stemmed = FOREACH $docterms GENERATE rowkey AS docId, FLATTEN(StemmedPairs(document#'$type')) AS term;
	doc_keyword_filtered = FILTER doc_keyword_stemmed BY term IS NOT NULL AND term != '' AND StopWordFilter(term);
	$dt = FOREACH doc_keyword_filtered GENERATE docId, (chararray) term;
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
-- calculate tfidf
-------------------------------------------------------
DEFINE calculate_tf_idf(docTerm) RETURNS tfidf {
	-- term count in a given document
	A1 = GROUP $docTerm BY (docId, term);
	A = FOREACH A1 GENERATE FLATTEN(group), COUNT($docTerm) as tc;
		
	-- total terms count in a given document
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
	
	$tfidf = FOREACH E GENERATE docId, term, ((double) tc / (double) ttc) * LOG((double) dc / (double) ttdc) AS tfidf;
};


-------------------------------------------------------
-- calculate tfidf 2
-------------------------------------------------------
DEFINE calculate_tf_idf2(docTerm) RETURNS tfidf {
	-- term count in a given document
	A1 = GROUP $docTerm BY (docId, term);
	A = FOREACH A1 GENERATE FLATTEN(group), COUNT($docTerm) as tc;
		
	-- total terms count in a given document
	B1 = GROUP A BY docId;
	B = FOREACH B1 GENERATE FLATTEN(A) AS (docId, term, tc), SUM(A.tc) AS ttc;
	
	-- total number of documents in the corpus
	CA1 = GROUP B BY docId;
	CA2 = FOREACH CA1 GENERATE group AS docId;
        CA3 = GROUP CA2 all;
        CA4 = FOREACH CA3 GENERATE COUNT(CA2) AS dc;
	C = CROSS B, CA4;	

	D = FOREACH C GENERATE docId, term, tc, ttc, dc;
	
	-- total number of documents that a given word occurs in
	E1 = GROUP D BY term;
	E = FOREACH E1 GENERATE FLATTEN(D) AS (docId, term, tc, ttc, dc), COUNT(D) AS ttdc;
	
	$tfidf = FOREACH E GENERATE docId, term, ((double) tc / (double) ttc) * LOG( (double) dc / (double) ttdc) AS tfidf;
};

DEFINE tf_idf(in_relation, id_field, token_field, minTfidf) RETURNS tfidf_values { 
  	-- Calculate the term count per document
	doc_word_group = group $in_relation by ($id_field, $token_field);
  	doc_word_totals = foreach doc_word_group generate 
    		FLATTEN(group) as ($id_field, token), 
		COUNT_STAR($in_relation) as doc_total;

  	-- Calculate the document size
	pre_term_group = group doc_word_totals by $id_field;
  	pre_term_counts = foreach pre_term_group generate
    		group AS $id_field,
    		FLATTEN(doc_word_totals.(token, doc_total)) as (token, doc_total), 
    		SUM(doc_word_totals.doc_total) as doc_size;
 
  	-- Calculate the TF
  	term_freqs = foreach pre_term_counts generate $id_field as $id_field,
    		token as token,
    		((double)doc_total / (double)doc_size) AS term_freq;
 
  	-- Get count of documents using each token, for idf
	token_usages_group = group term_freqs by token;
  	token_usages = foreach token_usages_group generate
    		FLATTEN(term_freqs) as ($id_field, token, term_freq),
    		COUNT_STAR(term_freqs) as num_docs_with_token;
 
  	-- Get document count
  	just_ids = foreach $in_relation generate $id_field;
	just_unique_ids = distinct just_ids;
	ndocs_group = group just_unique_ids all;
  	ndocs = foreach ndocs_group generate COUNT_STAR(just_unique_ids) as total_docs;
 
  	-- Note the use of Pig Scalars to calculate idf
  	$tfidf_values = foreach token_usages {
    		idf    = LOG((double)ndocs.total_docs/(double)num_docs_with_token);
    		tf_idf = (double)term_freq * idf;
    		generate $id_field as $id_field,
      			token as $token_field,
      			tf_idf as tfidf;
  	};
};


DEFINE top_n_per_group(in_relation, group_field, order_field, order_direction, topn, paral) RETURNS out_relation { 
	grouped = GROUP $in_relation BY $group_field;
	$out_relation = FOREACH in_relation {
           sorted = ORDER $in_relation BY $order_field $order_direction;
           top = LIMIT sorted $topn;
           GENERATE flatten(top);
	};
};

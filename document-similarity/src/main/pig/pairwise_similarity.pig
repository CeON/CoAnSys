%default tfidfPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-new9/weighted'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/docsim-10'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'
%default parallel 10

REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar;
REGISTER /usr/lib/hbase/hbase.jar;
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar;
REGISTER '$commonJarsPath';

DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

-------------------------------------------------------
-- business code section
----------------------------------------------------
set default_parallel $parallel

/* tfidf should be pre-sorted by term, since the current version of merge join does not support order operator between load and join */
tfidf = LOAD '$tfidfPath' AS (docId: bytearray, term: chararray, tfidf: double);
tfidf2 = LOAD '$tfidfPath' AS (docId: bytearray, term: chararray, tfidf: double);

tfidf_join = FILTER(JOIN tfidf BY term, tfidf2 BY term USING 'merge' parallel $parallel) BY tfidf::docId < tfidf2::docId;
	
term_docs_TFIDF = FOREACH tfidf_join GENERATE 
	tfidf::term AS term, 
	tfidf::docId AS docId1, tfidf2::docId As docId2, 
	tfidf::tfidf AS tfidf1, tfidf2::tfidf As tfidf2;

term_docs_similarity = FOREACH term_docs_TFIDF GENERATE 
	term, docId1, docId2, tfidf1, tfidf2, 
	KeywordSimilarity(term, docId1, tfidf1, docId2, tfidf2) AS similarity;

docs_terms_group = GROUP term_docs_similarity BY (docId1, docId2);
docs_terms_similarity = FOREACH docs_terms_group GENERATE 
	FLATTEN(group) AS (docId1, docId2), 
	DocsCombinedSimilarity(term_docs_similarity.docId1, term_docs_similarity.docId2, term_docs_similarity.similarity) AS similarity;

docs_similarity = FOREACH docs_terms_similarity GENERATE docId1, docId2, similarity;
docs_similarity2 = FOREACH docs_similarity GENERATE docId2 AS docId1, docId1 AS docId2, similarity;
docs_similarity_union = UNION docs_similarity, docs_similarity2;
doc_similarities = GROUP docs_similarity_union BY docId1;

STORE docs_similarity INTO '$outputPath';

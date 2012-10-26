%default inputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-new-lzo3/weighted'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/docsim-lzo'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

%default parallel 32

REGISTER '$commonJarsPath';

DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

-------------------------------------------------------
-- business code section
----------------------------------------------------
set default_parallel $parallel
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec lzo

/* tfidf should be pre-sorted by term, since the current version of merge join does not support order operator between load and join */
T = LOAD '$inputPath' AS (docId: bytearray, term: chararray, tfidf: double);
T2 = LOAD '$inputPath' AS (docId: bytearray, term: chararray, tfidf: double);

TJ = JOIN T BY term, T2 BY term USING 'merge';
TJP = FOREACH TJ 
	GENERATE T::term AS term, 
	T::docId AS docId1, T2::docId As docId2, 
	T::tfidf AS tfidf1, T2::tfidf As tfidf2;

TD = FILTER TJP BY docId < docId2;
TDS = FOREACH TD 
	GENERATE term, docId1, docId2, tfidf1, tfidf2,
	KeywordSimilarity(term, docId1, tfidf1, docId2, tfidf2) AS similarity;

docs_terms_group = GROUP TDS BY (docId1, docId2);
docs_terms_similarity = FOREACH docs_terms_group GENERATE 
	FLATTEN(group) AS (docId1, docId2), 
	DocsCombinedSimilarity(TDS.docId1, TDS.docId2, TDS.similarity) AS similarity;

docs_similarity = FOREACH docs_terms_similarity GENERATE docId1, docId2, similarity;
docs_similarity2 = FOREACH docs_similarity GENERATE docId2 AS docId1, docId1 AS docId2, similarity;
docs_similarity_union = UNION docs_similarity, docs_similarity2;
doc_similarities = GROUP docs_similarity_union BY docId1;

STORE docs_similarity INTO '$outputPath';

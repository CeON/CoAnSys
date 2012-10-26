%default inputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-new-lzo3/weighted'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/docsim-lzo'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

%default parallel 32
%default tmpCompressionCodec gz

REGISTER '$commonJarsPath';

DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

-------------------------------------------------------
-- business code section
----------------------------------------------------
set default_parallel $parallel
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec $tmpCompressionCodec

/* tfidf should be pre-sorted by term, since the current version of merge join does not support order operator between load and join */
t = LOAD '$inputPath' AS (docId: bytearray, term: chararray, tfidf: double);
t2 = LOAD '$inputPath' AS (docId: bytearray, term: chararray, tfidf: double);

tj = JOIN t BY term, t2 BY term USING 'merge';
tjp = FOREACH tj GENERATE t::term AS term,
	t::docId AS docId1, t2::docId As docId2, 
	t::tfidf AS tfidf1, t2::tfidf As tfidf2;

term_doc_tfidf = FILTER tjp BY docId1 < docId2;
term_doc_similarity = FOREACH term_doc_tfidf GENERATE term, docId1, docId2, tfidf1, tfidf2,
	KeywordSimilarity(term, docId1, tfidf1, docId2, tfidf2) AS similarity;

docs_terms_group = GROUP term_doc_similarity BY (docId1, docId2);
docs_terms_similarity = FOREACH docs_terms_group GENERATE FLATTEN(group) AS (docId1, docId2), 
	DocsCombinedSimilarity(term_doc_similarity.docId1, term_doc_similarity.docId2, term_doc_similarity.similarity) AS similarity;

docs_similarity = FOREACH docs_terms_similarity GENERATE docId1, docId2, similarity;
docs_similarity2 = FOREACH docs_similarity GENERATE docId2 AS docId1, docId1 AS docId2, similarity;
docs_similarity_union = UNION docs_similarity, docs_similarity2;
doc_similarities = GROUP docs_similarity_union BY docId1;

STORE docs_similarity INTO '$outputPath';

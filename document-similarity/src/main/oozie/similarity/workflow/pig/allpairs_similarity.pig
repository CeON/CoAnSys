%default sample 0.1

%default tfidfPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-new9/weighted'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/docsim-11'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'
%default parallel 10

-------------------------------------------------------
-- register section
-------------------------------------------------------
REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar;
REGISTER /usr/lib/hbase/hbase.jar;
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar;

REGISTER '$commonJarsPath';

-------------------------------------------------------
-- define section
-------------------------------------------------------
DEFINE CosineSimilarity pl.edu.icm.coansys.similarity.pig.udf.CosineSimilarity();

-------------------------------------------------------
-- business code section
-------------------------------------------------------
set default_parallel $parallel
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec gz

tfidf = LOAD '$tfidfPath' AS (docId: chararray, term: chararray, tfidf: double);
g1 = GROUP tfidf BY docId;
g2 = FOREACH g1 GENERATE *;

tfidf_cross = FILTER(CROSS g1, g2 parallel $parallel) BY g1::group < g2::group;

-- measure cosine document similarity
similarity = FOREACH tfidf_cross {
		A = ORDER g1::tfidf BY term;
		B = ORDER g2::tfidf BY term;
		GENERATE CosineSimilarity(g1::group, A, g2::group, B) AS cosineTuple;
};

-- flatten cosine document similarity
S1 = FOREACH similarity GENERATE FLATTEN(cosineTuple) AS (docId1, docId2, similarity);
S2 = FOREACH S1 GENERATE docId2 AS docId1, docId1 AS docId2, similarity;
S = UNION S1, S2;
SG = GROUP S BY docId1;

--measure cosine document similarity
STORE S INTO '$outputPath';

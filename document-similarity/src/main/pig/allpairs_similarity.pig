-------------------------------------------------------
-- register section
-------------------------------------------------------
REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar
REGISTER /usr/lib/hbase/hbase.jar
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'

-------------------------------------------------------
-- define section
-------------------------------------------------------
DEFINE CosineSimilarity pl.edu.icm.coansys.similarity.pig.udf.CosineSimilarity();

-------------------------------------------------------
-- business code section
-------------------------------------------------------
TFIDF = LOAD '$tfidfPath' AS (docId: chararray, term: chararray, tfidf: double);
G1 = GROUP TFIDF BY docId;
G2 = FOREACH G1 GENERATE *;

TFIDF_cross = FILTER(CROSS G1, G2) BY G1::group < G2::group;

-- measure cosine document similarity
similarity = FOREACH TFIDF_cross {
		A = ORDER G1::TFIDF BY term;
		B = ORDER G2::TFIDF BY term;
		GENERATE CosineSimilarity(G1::group, A, G2::group, B) AS cosineTuple;
};

-- flatten cosine document similarity
S1 = FOREACH similarity GENERATE FLATTEN(cosineTuple) AS (docId1, docId2, similarity);
S2 = FOREACH S1 GENERATE docId2 AS docId1, docId1 AS docId2, similarity;
S = UNION S1, S2;
SG = GROUP S BY docId1;

--measure cosine document similarity
STORE SG INTO '$outputPath';
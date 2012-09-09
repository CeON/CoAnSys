-------------------------------------------------------
-- register section
-------------------------------------------------------
REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar
REGISTER /usr/lib/hbase/hbase.jar
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER /home/akawa/Documents/git-projects/CoAnSys/importers/target/importers-1.0-SNAPSHOT.jar
REGISTER /home/akawa/Documents/git-projects/CoAnSys/commons/target/commons-1.0-SNAPSHOT.jar
REGISTER /home/akawa/Documents/git-projects/CoAnSys/document-similarity/target/document-similarity-1.0-SNAPSHOT.jar

-------------------------------------------------------
-- define section
-------------------------------------------------------
DEFINE DocumentProtobufBytesToTuple pl.edu.icm.coansys.commons.pig.udf.DocumentProtobufBytesToTuple();
DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

-------------------------------------------------------
-- import section
-------------------------------------------------------
IMPORT 'macros.pig';

TFIDF = LOAD '$tfidfPath' AS (docId, term: chararray, tfidf: double);
TFIDF_2 = get_copy(TFIDF); 

TFIDF_join = FILTER(JOIN TFIDF BY term, TFIDF_2 BY term) BY TFIDF::docId < TFIDF_2::docId;
	
term_docs_TFIDF = FOREACH TFIDF_join GENERATE TFIDF::term AS term, 
	TFIDF::docId AS docId1, TFIDF_2::docId As docId2, TFIDF::tfidf AS tfidf1, TFIDF_2::tfidf As tfidf2;

term_docs_similarity = FOREACH term_docs_TFIDF GENERATE term, docId1, docId2, tfidf1, tfidf2, 
						KeywordSimilarity(term, docId1, tfidf1, docId2, tfidf2) AS similarity;

docs_terms_group = GROUP term_docs_similarity BY (docId1, docId2);
docs_terms_similarity = FOREACH docs_terms_group GENERATE FLATTEN(group) AS (docId1, docId2), 
						DocsCombinedSimilarity(term_docs_similarity.docId1, term_docs_similarity.docId2, term_docs_similarity.similarity) AS similarity;

docs_similarity = FOREACH docs_terms_similarity GENERATE docId1, docId2, similarity;
doc_similarities = GROUP docs_similarity BY docId1;

STORE doc_similarities INTO '$outputPath';
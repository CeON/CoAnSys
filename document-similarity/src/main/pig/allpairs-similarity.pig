/* 
sudo -u hdfs hadoop fs -mkdir /user/pig/commonlibs/
sudo -u hdfs hadoop fs -chmod 777 /user/pig/commonlibs/
hadoop fs -put /usr/lib/hbase/lib/guava-11.2.jar /user/pig/commonlibs/
hadoop fs -put /usr/lib/hbase/hbase.jar /user/pig/commonlibs/
hadoop fs -put /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar /user/pig/commonlibs/
*/

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
-- import section
-------------------------------------------------------
IMPORT 'macros.pig';

TFIDF = LOAD '$tfidfPath' AS (docId, term, tfidf);
TFIDF_group = GROUP TFIDF BY docId;
TFIDF_group2 = get_copy(TFIDF_group);

TFIDF_cross = FILTER(CROSS TFIDF_group, TFIDF_group2) 
				BY TFIDF_group::group < TFIDF_group2::group;
	
-- measure cosine document similarity
similarities = FOREACH TFIDF_cross GENERATE FLATTEN(
		pl.edu.icm.coansys.similarity.pig.udf.CosineSimilarity(*))
		AS (docId1:chararray, docId2:chararray, similarity:double);

doc_similarity = GROUP similarities BY docId1;

--measure cosine document similarity
STORE doc_similarity INTO '$outputPath';
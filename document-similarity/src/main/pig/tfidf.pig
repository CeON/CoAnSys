-------------------------------------------------------
-- register section
-------------------------------------------------------
REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar
REGISTER /usr/lib/hbase/hbase.jar
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER ../../../../importers/target/importers-1.0-SNAPSHOT.jar
REGISTER ../../../../commons/target/commons-1.0-SNAPSHOT.jar
REGISTER ../../../../document-similarity/target/document-similarity-1.0-SNAPSHOT.jar
REGISTER ../../../../disambiguation/target/disambiguation-1.0-SNAPSHOT.jar

-------------------------------------------------------
-- define section
-------------------------------------------------------
DEFINE DocumentProtobufBytesToTuple pl.edu.icm.coansys.commons.pig.udf.DocumentProtobufBytesToTuple();
DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE StemmedPairs pl.edu.icm.coansys.similarity.pig.udf.StemmedPairs();
DEFINE StopWordFilter pl.edu.icm.coansys.similarity.pig.udf.StopWordFilter();

-------------------------------------------------------
-- import section
-------------------------------------------------------
IMPORT 'macros.pig';

-------------------------------------------------------
-- parameter section
-------------------------------------------------------
%default KEYWORD_SUBDIR '/keyword'
%default TITLE_SUBDIR '/title'
%default ABSTRACT_SUBDIR '/abstract'
%default JOINED_SUBDIR '/weighted'
%default SIM_SUBDIR '/similarity'

%default KEYWORD_WEIGHT 1.0
%default TITLE_WEIGHT 1.0
%default ABSTRACT_WEIGHT 1.0

-------------------------------------------------------
-- business code section
-------------------------------------------------------
doc = load_bwndata('$tableName');

-- stem, clean, filter out
doc_keyword = stem_and_filter_out(doc, 'keywords');
doc_title = stem_and_filter_out(doc, 'title');
doc_abstract = stem_and_filter_out(doc, 'abstract');

-- calculate tf-idf for each group of terms
tfidf_keyword = calculate_tf_idf(doc_keyword);
tfidf_abstract = calculate_tf_idf(doc_abstract);
tfidf_title = calculate_tf_idf(doc_title);

-- get distinct words
doc_term_K = FOREACH doc_keyword GENERATE docId, term;
doc_term_T = FOREACH doc_title GENERATE docId, term;
doc_term_A = FOREACH doc_abstract GENERATE docId, term;
doc_term_union = UNION doc_term_K, doc_term_A, doc_term_T;
doc_term_distinct = DISTINCT doc_term_union;

-- calculate weighted results
tfidf_all_joined_A1 = JOIN doc_term_distinct BY (docId, term) LEFT OUTER, tfidf_abstract BY (docId, term);
tfidf_all_joined_A = FOREACH tfidf_all_joined_A1 GENERATE doc_term_distinct::docId AS docId, doc_term_distinct::term AS term, tfidf AS tfidfAbstract;
tfidf_all_joined_AK1 = JOIN tfidf_all_joined_A BY (docId, term) LEFT OUTER, tfidf_keyword BY (docId, term);
tfidf_all_joined_AK = FOREACH tfidf_all_joined_AK1 GENERATE tfidf_all_joined_A::docId AS docId, tfidf_all_joined_A::term AS term, tfidfAbstract, tfidf AS tfidfKeyword;
tfidf_all_joined_AKT1 = JOIN tfidf_all_joined_AK BY (docId, term) LEFT OUTER, tfidf_title BY (docId, term);
tfidf_all_joined_AKT = FOREACH tfidf_all_joined_AKT1 GENERATE tfidf_all_joined_AK::docId AS docId, tfidf_all_joined_AK::term AS term, tfidfAbstract, tfidfKeyword, tfidf AS tfidfTitle;

-- calculate weighted tfidf
tfidf_all_joined = FOREACH tfidf_all_joined_AKT GENERATE docId, term, 
	WeightedTFIDF($KEYWORD_WEIGHT, tfidfKeyword, $TITLE_WEIGHT, tfidfTitle, $ABSTRACT_WEIGHT, tfidfAbstract) AS tfidf;

-- store into separate direcotires
STORE tfidf_keyword INTO '$outputPath$KEYWORD_SUBDIR';
STORE tfidf_abstract INTO '$outputPath$TITLE_SUBDIR';
STORE tfidf_title INTO '$outputPath$ABSTRACT_SUBDIR';
STORE tfidf_all_joined INTO '$outputPath$JOINED_SUBDIR';
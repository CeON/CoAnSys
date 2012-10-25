%default KEYWORD_SUBDIR '/keyword'
%default TITLE_SUBDIR '/title'
%default ABSTRACT_SUBDIR '/abstract'
%default JOINED_SUBDIR '/weighted'

%default keywordWeight 0.33
%default titleWeight 0.33
%default abstractWeight 0.33
%default sample 0.01

%default inputPath 'hdfs://hadoop-master:8020/user/akawa/full/hbase-dump/mproto-m*'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-limited-20'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'
%default parallel 32
%default minTfidf 0.50

REGISTER '$commonJarsPath'

DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE StemmedPairs pl.edu.icm.coansys.similarity.pig.udf.StemmedPairs();
DEFINE StopWordFilter pl.edu.icm.coansys.similarity.pig.udf.StopWordFilter();

IMPORT 'macros.pig';

SET default_parallel $parallel
SET mapred.child.java.opts -Xmx8000m
SET pig.tmpfilecompression true
SET pig.tmpfilecompression.codec lzo

-------------------------------------------------------
-- business code section
-------------------------------------------------------
doc = load_bwndata_metadata_hdfs('$inputPath', $sample);

-- stem, clean, filter out
doc_keyword = stem_and_filter_out(doc, 'keywords');
doc_title = stem_and_filter_out(doc, 'title');
doc_abstract = stem_and_filter_out(doc, 'abstract');

-- get distinct words
doc_term_K = FOREACH doc_keyword GENERATE docId, term;
doc_term_T = FOREACH doc_title GENERATE docId, term;
doc_term_A = FOREACH doc_abstract GENERATE docId, term;
doc_term_union = UNION doc_term_K, doc_term_A, doc_term_T;
doc_term_distinct = DISTINCT doc_term_union;

-- calculate tf-idf for each group of terms
tfidf_keyword = tf_idf(doc_keyword, docId, term, $minTfidf, $parallel);
tfidf_abstract = tf_idf(doc_abstract, docId, term, $minTfidf, $parallel);
tfidf_title = tf_idf(doc_title, docId, term, $minTfidf, $parallel);

-- calculate weighted results
tfidf_all_joined_A = FOREACH (JOIN doc_term_distinct BY (docId, term) LEFT OUTER, tfidf_abstract BY (docId, term) parallel $parallel)
	GENERATE doc_term_distinct::docId AS docId, doc_term_distinct::term AS term, tfidf AS tfidfAbstract;
tfidf_all_joined_AK = FOREACH (JOIN tfidf_all_joined_A BY (docId, term) LEFT OUTER, tfidf_keyword BY (docId, term) parallel $parallel)
	GENERATE tfidf_all_joined_A::docId AS docId, tfidf_all_joined_A::term AS term, tfidfAbstract, tfidf AS tfidfKeyword;
tfidf_all_joined_AKT = FOREACH (JOIN tfidf_all_joined_AK BY (docId, term) LEFT OUTER, tfidf_title BY (docId, term) parallel $parallel)
	GENERATE tfidf_all_joined_AK::docId AS docId, tfidf_all_joined_AK::term AS term, tfidfAbstract, tfidfKeyword, tfidf AS tfidfTitle;
-- calculate weighted tfidf
tfidf_all = FOREACH tfidf_all_joined_AKT 
	GENERATE docId, (chararray) term, WeightedTFIDF($keywordWeight, tfidfKeyword, $titleWeight, tfidfTitle, $abstractWeight, tfidfAbstract) AS tfidf;

-- store into separate direcotires
STORE tfidf_keyword INTO '$outputPath$KEYWORD_SUBDIR';
STORE tfidf_abstract INTO '$outputPath$TITLE_SUBDIR';
STORE tfidf_title INTO '$outputPath$ABSTRACT_SUBDIR';
STORE tfidf_all INTO '$outputPath$JOINED_SUBDIR';

%default TFIDF_KEYWORD_SUBDIR '/tfidf/keyword'
%default TFIDF_TITLE_SUBDIR '/tfidf/title'
%default TFIDF_ABSTRACT_SUBDIR '/tfidf/abstract'
%default TFIDF_WEIGHTED_SUBDIR '/tfidf/weighted'
%default TFIDF_NON_WEIGHTED_SUBDIR '/tfidf/nonweighted'
%default TFIDF_TOPN_WEIGHTED_SUBDIR '/tfidf/weighted-topn'
%default STOPWORDS_SUBDIR '/stopwords'
%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'

%default tfidfKeywordWeight 0.50
%default tfidfTitleWeight 0.35
%default tfidfAbstractWeight 0.15

%default stopwordDocumentFrequencyTreshold 0.50
%default tfidfTopnTermPerDocument 20
%default similarityTopnDocumentPerDocument 20
%default tfidfMinValue 0.50

%default sample 0.5
%default parallel 32
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'full/hbase-dump/mproto-m*'
%default outputPath 'document-similarity-output'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

REGISTER '$commonJarsPath'

DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE StemmedPairs pl.edu.icm.coansys.similarity.pig.udf.StemmedPairs();
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

SET default_parallel $parallel
SET mapred.child.java.opts $mapredChildJavaOpts
SET pig.tmpfilecompression true
SET pig.tmpfilecompression.codec $tmpCompressionCodec

IMPORT 'macros.pig';

-------------------------------------------------------
-- business code section
-------------------------------------------------------
doc = load_bwndata_metadata_hdfs('$inputPath', $sample);

-- stem, clean, filter out
doc_keyword_all = stem_words(doc, 'keywords');
doc_title_all = stem_words(doc, 'title');
doc_abstract_all = stem_words(doc, 'abstract');

-- get distinct words
doc_all_union = UNION doc_keyword_all, doc_title_all, doc_abstract_all;
doc_all_distinct = DISTINCT doc_all_union;

-- comment lines bellow if you do not want to run some experimental algorithm to run stopwords detection
/*
-- find and store stopwords
stopwords = find_stopwords(doc_all_distinct, docId, term, $stopwordDocumentFrequencyTreshold);
STORE stopwords INTO '$outputPath$STOPWORDS_SUBDIR';

-- remove stopwrods from terms
doc_keyword = remove_stopwords(doc_keyword_all, stopwords, docId, term, '::');
doc_title = remove_stopwords(doc_title_all, stopwords, docId, term, '::');
doc_abstract = remove_stopwords(doc_abstract_all, stopwords, docId, term, '::');
doc_all = remove_stopwords(doc_all_distinct, stopwords, docId, term, '::');
*/

-- use this, if stopwords are not removed by lines above
doc_keyword = get_copy(doc_keyword_all);
doc_title = get_copy(doc_title_all);
doc_abstract = get_copy(doc_abstract_all);
doc_all = get_copy(doc_all_distinct);

-- calculate tf-idf for each group of terms
tfidf_keyword = calculate_tfidf(doc_keyword, docId, term, $tfidfMinValue);
tfidf_abstract = calculate_tfidf(doc_abstract, docId, term, $tfidfMinValue);
tfidf_title = calculate_tfidf(doc_title, docId, term, $tfidfMinValue);
tfidf_all = calculate_tfidf(doc_all, docId, term, $tfidfMinValue);

-- calculate weighted results
tfidf_all_joined_A = FOREACH (JOIN doc_all_distinct BY (docId, term) LEFT OUTER, tfidf_abstract BY (docId, term))
	GENERATE doc_all_distinct::docId AS docId, doc_all_distinct::term AS term, tfidf AS tfidfAbstract;
tfidf_all_joined_AK = FOREACH (JOIN tfidf_all_joined_A BY (docId, term) LEFT OUTER, tfidf_keyword BY (docId, term))
	GENERATE tfidf_all_joined_A::docId AS docId, tfidf_all_joined_A::term AS term, tfidfAbstract, tfidf AS tfidfKeyword;
tfidf_all_joined_AKT = FOREACH (JOIN tfidf_all_joined_AK BY (docId, term) LEFT OUTER, tfidf_title BY (docId, term))
	GENERATE tfidf_all_joined_AK::docId AS docId, tfidf_all_joined_AK::term AS term, tfidfAbstract, tfidfKeyword, tfidf AS tfidfTitle;
-- calculate weighted tfidf
tfidf_weighted = FOREACH tfidf_all_joined_AKT 
	GENERATE docId, term, WeightedTFIDF($tfidfKeywordWeight, tfidfKeyword, $tfidfTitleWeight, tfidfTitle, $tfidfAbstractWeight, tfidfAbstract) AS tfidf;

-- store tfidf values into separate direcotires
STORE tfidf_keyword INTO '$outputPath$TFIDF_KEYWORD_SUBDIR';
STORE tfidf_abstract INTO '$outputPath$TFIDF_TITLE_SUBDIR';
STORE tfidf_title INTO '$outputPath$TFIDF_ABSTRACT_SUBDIR';
STORE tfidf_all INTO '$outputPath$TFIDF_NON_WEIGHTED_SUBDIR';
STORE tfidf_weighted INTO '$outputPath$TFIDF_WEIGHTED_SUBDIR';

-- calculate and store topn terms per document in weighted results
tfidf_weighted_topn = get_topn_per_group(tfidf_weighted, docId, tfidf, 'desc', $tfidfTopnTermPerDocument);
tfidf_weighted_topn_projected = FOREACH tfidf_weighted_topn GENERATE
	(chararray)top::docId AS docId, (chararray)top::term AS term, (double)top::tfidf AS tfidf;
STORE tfidf_weighted_topn_projected INTO '$outputPath$TFIDF_TOPN_WEIGHTED_SUBDIR';

-- calculate and store document similarity for all documents
document_similarity = calculate_pairwise_similarity(tfidf_weighted_topn_projected, docId, term, tfidf, '::');
STORE document_similarity INTO '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR';

-- calculate and store topn similar documents for each document
document_similarity_topn = get_topn_per_group(document_similarity, docId1, similarity, 'desc', $similarityTopnDocumentPerDocument);
STORE document_similarity_topn INTO '$outputPath$SIMILARITY_TOPN_DOCS_SUBDIR';

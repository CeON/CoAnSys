%default TFIDF_KEYWORD_SUBDIR '/tfidf/keyword'
%default TFIDF_TITLE_SUBDIR '/tfidf/title'
%default TFIDF_ABSTRACT_SUBDIR '/tfidf/abstract'
%default TFIDF_WEIGHTED_SUBDIR '/tfidf/weighted'
%default TFIDF_NON_WEIGHTED_SUBDIR '/tfidf/nonweighted'
%default STOPWORDS_SUBDIR '/stopwords'
%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'

%default tfidfKeywordWeight 0.50
%default tfidfTitleWeight 0.35
%default tfidfAbstractWeight 0.15

%default stopwordDocumentFrequencyTreshold 0.75
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

-- find and store stopwords
stopwords = find_stopwords(doc_all_distinct, docId, term, $stopwordDocumentFrequencyTreshold);
STORE stopwords INTO '$outputPath$STOPWORDS_SUBDIR';

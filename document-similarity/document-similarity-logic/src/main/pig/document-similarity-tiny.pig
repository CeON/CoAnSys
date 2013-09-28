--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2013 ICM-UW
-- 
-- CoAnSys is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- CoAnSys is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU Affero General Public License for more details.
-- 
-- You should have received a copy of the GNU Affero General Public License
-- along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
--

%default DOC_TERM_ALL '/term/all'
%default DOC_TERM_KEYWORDS '/term/keywords'
%default DOC_TERM_TITLE '/term/title'
%default TFIDF_NON_WEIGHTED_SUBDIR '/tfidf/nonweighted'
%default TFIDF_TOPN_WEIGHTED_SUBDIR '/tfidf/weighted-topn'
%default TFIDF_TOPN_ALL_SUBDIR '/tfidf/all-topn'
%default TFIDF_TF_ALL_SUBDIR '/tfidf/tf-all-topn'
%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'

%default tfidfTopnTermPerDocument 20
%default similarityTopnDocumentPerDocument 20
%default tfidfMinValue 0.4

%default sample 0.5
%default parallel 10
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'full/hbase-dump/mproto-m*'
%default outputPath 'document-similarity-output'
%default jars '*.jar'
%default commonJarsPath '../oozie/similarity/workflow/lib/$jars'

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
doc = load_from_hdfs('$inputPath', $sample);
doc = foreach doc generate $0 as docId, $1 as documents;
doc_raw = foreach doc generate $0, document.title as title, document.abstract as abstract;
-- speparated line as FLATTEN w a hidden CROSS
doc_keyword_raw = foreach doc generate $1 AS docId, FLATTEN(document.keywords) AS keywords;
/*DESCRIBE doc_keyword_raw;*/
-- stem, clean, filter out
doc_keyword_all = stem_words(doc_keyword_raw, docId, keywords);
doc_title_all = stem_words(doc_raw, docId, title);
doc_abstract_all = stem_words(doc_raw, docId, abstract);

-- get all words (with duplicates for tfidf)
doc_all = UNION doc_keyword_all, doc_title_all, doc_abstract_all;

-- store document and terms
--STORE doc_title_all INTO '$outputPath$DOC_TERM_TITLE';
--STORE doc_keyword_all INTO '$outputPath$DOC_TERM_KEYWORDS';
DESCRIBE doc_all;
STORE doc_all INTO '$outputPath$DOC_TERM_ALL';

-- calculate tf-idf for each group of terms
tfidf_all = calculate_tfidf(doc_all, docId, term, $tfidfMinValue);
-- store tfidf values into separate direcotires
DESCRIBE tfidf_all;
STORE tfidf_all INTO '$outputPath$TFIDF_NON_WEIGHTED_SUBDIR';

-- calculate and store topn terms per document in all results
tfidf_all_topn = get_topn_per_group(tfidf_all, docId, tfidf, 'desc', $tfidfTopnTermPerDocument);
tfidf_all_topn_projected = FOREACH tfidf_all_topn GENERATE top::docId AS docId, top::term AS term, top::tfidf AS tfidf;
STORE tfidf_all_topn_projected INTO '$outputPath$TFIDF_TOPN_ALL_SUBDIR';

tfidf_all_topn_projected_loaded = LOAD '$outputPath$TFIDF_TOPN_ALL_SUBDIR' AS (docId: chararray, term: chararray, tfidf: double);
duplicate = foreach tfidf_all_topn_projected_loaded generate *;
-- calculate and store document similarity for all documents
document_similarity = calculate_pairwise_similarity(tfidf_all_topn_projected_loaded,duplicate, docId, term, tfidf, '::',$parallel);
DESCRIBE document_similarity;
STORE document_similarity INTO '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR';

-- calculate and store topn similar documents for each document
document_similarity_topn = get_topn_per_group(document_similarity, docId1, similarity, 'desc', $similarityTopnDocumentPerDocument);
STORE document_similarity_topn INTO '$outputPath$SIMILARITY_TOPN_DOCS_SUBDIR';

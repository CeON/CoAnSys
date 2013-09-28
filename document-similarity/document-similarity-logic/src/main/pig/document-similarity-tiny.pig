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

%default sample 0.1
%default parallel 10
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf'
%default time '2013-09-28--10-37'
%default outputPath 'document-similarity-output/$time/'
%default jars '*.jar'
%default commonJarsPath '../../../../document-similarity-workflow/target/oozie-wf/lib/$jars'

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
doc = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);
--B = SAMPLE A $sampling;
B = limit A 100;
C = FOREACH B GENERATE $0 as docId, pl.edu.icm.coansys.similarity.pig.udf.DocumentProtobufToTupleMap($1) as document ;
--doc = load_from_hdfs('$inputPath', $sample);
--doc = foreach doc generate $0 as docId, $1 as document;

doc_raw = foreach doc generate docId, document.title as title, document.abstract as abstract;
-- speparated line as FLATTEN w a hidden CROSS
doc_keyword_raw = foreach doc generate docId, FLATTEN(document.keywords) AS keywords;
-- stem, clean, filter out
doc_keyword_all = stem_words(doc_keyword_raw, docId, keywords);
doc_title_all = stem_words(doc_raw, docId, title);
doc_abstract_all = stem_words(doc_raw, docId, abstract);

-- get all words (with duplicates for tfidf)
doc_all = UNION doc_keyword_all, doc_title_all, doc_abstract_all;

-- store document and terms
--STORE doc_title_all INTO '$outputPath$DOC_TERM_TITLE';
--STORE doc_keyword_all INTO '$outputPath$DOC_TERM_KEYWORDS';
STORE doc_all INTO '$outputPath$DOC_TERM_ALL';
-- calculate tf-idf for each group of terms
tfidf_all = calculate_tfidf(doc_all, docId, term, $tfidfMinValue);
-- store tfidf values into separate direcotires
STORE tfidf_all INTO '$outputPath$TFIDF_NON_WEIGHTED_SUBDIR';

exec;
-- calculate and store topn terms per document in all results
tfidf_all_topn = get_topn_per_group(tfidf_all, docId, tfidf, 'desc', $tfidfTopnTermPerDocument);
tfidf_all_topn_projected = FOREACH tfidf_all_topn GENERATE top::docId AS docId, top::term AS term, top::tfidf AS tfidf;
tfidf_all_topn_sorted = order tfidf_all_topn_projected by docId asc;
STORE tfidf_all_topn_sorted  INTO '$outputPath$TFIDF_TOPN_ALL_SUBDIR';
exec;

tfidf_all_topn_sorted_orig = LOAD '$outputPath$TFIDF_TOPN_ALL_SUBDIR' AS (docId: chararray, term: chararray, tfidf: double);
tfidf_all_topn_sorted_dupl = LOAD '$outputPath$TFIDF_TOPN_ALL_SUBDIR' AS (docId: chararray, term: chararray, tfidf: double);
-- calculate and store document similarity for all documents
document_similarity = calculate_pairwise_similarity(tfidf_all_topn_sorted_orig,
		tfidf_all_topn_sorted_dupl, docId, term, tfidf, '::',$parallel);
STORE document_similarity INTO '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR';

exec;
mix_l = LOAD '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR' as (chararray,chararray,double);
mix_r = foreach mix_l generate $1,$0,$2;
mix_f = union mix_l,mix_r;
mix_f = foreach mix_f generate $0 as (docIdA:chararray), $1 as (docIdB:chararray),$2 as (similarity:double);

-- calculate and store topn similar documents for each document
document_similarity_topn = get_topn_per_group(mix_f, docIdA, similarityX, 'desc', $similarityTopnDocumentPerDocument);
STORE document_similarity_topn INTO '$outputPath$SIMILARITY_TOPN_DOCS_SUBDIR';
/**/


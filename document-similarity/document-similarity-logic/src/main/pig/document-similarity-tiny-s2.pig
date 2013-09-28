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
/*** (d) load sorted results (e) duplicate it *************************************/
/*** (f) perform doc-sim calculation [MERGE-SORT] (g) close current tasks *********/
tfidf_all_topn_sorted_orig = LOAD '$outputPath$TFIDF_TOPN_ALL_SUBDIR' AS (docId: chararray, term: chararray, tfidf: double);
tfidf_all_topn_sorted_dupl = LOAD '$outputPath$TFIDF_TOPN_ALL_SUBDIR' AS (docId: chararray, term: chararray, tfidf: double);
-- calculate and store document similarity for all documents
document_similarity = calculate_pairwise_similarity(tfidf_all_topn_sorted_orig,
		tfidf_all_topn_sorted_dupl, docId, term, tfidf, '::',$parallel);
STORE document_similarity INTO '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR';
/********************* END:MERGE-SORT ZONE *****************************************/

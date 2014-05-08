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
%default TFIDF_TOPN_ALL_TEMP '/tfidf/all-topn-tmp'
%default TFIDF_TF_ALL_SUBDIR '/tfidf/tf-all-topn'
%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_ALL_LEFT_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_NORMALIZED_LEFT_DOCS_SUBDIR '/similarity/normalizedalldocs'
%default SIMILARITY_NORMALIZED_ALL_DOCS_SUBDIR '/similarity/normalizedleftdocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'
%default DENOMINATOR '/similarity/denominator'
%default NOMINATOR '/similarity/nominator'
%default SIMILARITY_TOPN_DOCS_PB_SUBDIR '/similarity/protobuf'

%default tfidfTopnTermPerDocument 20
%default similarityTopnDocumentPerDocument 20
%default tfidfMinValue 0.4

%default sample 0.1
%default parallel 10
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf'
%default outputPath 'document-similarity-output/'
%default jars '*.jar'
%default commonJarsPath 'lib/$jars'

REGISTER '$commonJarsPath'

DEFINE BagPow pl.edu.icm.coansys.similarity.pig.udf.PowForBag();
DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE StemmedPairs pl.edu.icm.coansys.similarity.pig.udf.StemmedPairs();
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

SET default_parallel $parallel
SET mapred.child.java.opts $mapredChildJavaOpts
SET pig.tmpfilecompression true
SET pig.tmpfilecompression.codec $tmpCompressionCodec
%DEFAULT ds_scheduler default
SET mapred.fairscheduler.pool $ds_scheduler
--SET pig.noSplitCombination true;
IMPORT 'macros.pig';

-------------------------------------------------------
-- business code section
-------------------------------------------------------

-- consider both <docIdA, docIdB,sim> and <docIdB,docIdA,sim>
mix_l = LOAD '$outputPath$SIMILARITY_NORMALIZED_ALL_DOCS_SUBDIR' as (docA:chararray,docB:chararray,sim:float);
mix_r = foreach mix_l generate $1 as docA:chararray,$0 as docB:chararray,$2 as sim:float;
mix_xf = union mix_l,mix_r;
mix_f = distinct mix_xf;
describe mix_f;
-- calculate and store topn similar documents for each document
document_similarity_topnX = get_topn_per_group(mix_f, docA, sim, 'desc', $similarityTopnDocumentPerDocument);
document_similarity_topnX2 = filter document_similarity_topnX by ($0 is not null and $1 is not null and $2 is not null);
STORE document_similarity_topnX2 INTO '$outputPath$SIMILARITY_TOPN_DOCS_SUBDIR';

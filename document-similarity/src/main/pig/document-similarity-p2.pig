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

%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'

%default similarityTopnDocumentPerDocument 20

%default sample 0.5
%default parallel 10
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'full/hbase-dump/mproto-m*'
%default outputPath 'document-similarity-output'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

REGISTER '$commonJarsPath'

DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');

SET default_parallel $parallel
SET mapred.child.java.opts $mapredChildJavaOpts
--SET pig.tmpfilecompression true
--SET pig.tmpfilecompression.codec $tmpCompressionCodec

IMPORT 'macros.pig';

-------------------------------------------------------
-- business code section
-------------------------------------------------------
-- load data sorted by term, docId, tfidf
doc_tfidf = LOAD '$inputPath' AS (docId: chararray, term: chararray, tfidf: double);
doc_tfidf2 = LOAD '$inputPath' AS (docId: chararray, term: chararray, tfidf: double);

-- calculate and store document similarity for all documents
document_similarity = calculate_pairwise_similarity(doc_tfidf, doc_tfidf2, docId, term, tfidf, '::', 190);
DESCRIBE document_similarity;
STORE document_similarity INTO '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR';

-- calculate and store topn similar documents for each document
document_similarity_topn = get_topn_per_group(document_similarity, docId1, similarity, 'desc', $similarityTopnDocumentPerDocument);
STORE document_similarity_topn INTO '$outputPath$SIMILARITY_TOPN_DOCS_SUBDIR';

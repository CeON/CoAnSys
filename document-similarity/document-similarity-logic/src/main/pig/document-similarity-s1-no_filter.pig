--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2015 ICM-UW
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

%default RAW_DATA '/raw'
%default DOC_TERM_ALL '/term/all'
%default DOC_TERM_KEYWORDS '/term/keywords'
%default DOC_TERM_TITLE '/term/title'
%default TFIDF_NON_WEIGHTED_SUBDIR '/tfidf/nonweighted'
%default TFIDF_TOPN_WEIGHTED_SUBDIR '/tfidf/weighted-topn'
%default TFIDF_TOPN_ALL_TEMP '/tfidf/all-topn-tmp'
%default TFIDF_TOPN_ALL_SUBDIR '/tfidf/all-topn'
%default TFIDF_TF_ALL_SUBDIR '/tfidf/tf-all-topn'
%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'
%default TERM_COUNT '/term-count'
%default WORD_COUNT '/filtered-by-ranked-word-count';

%default tfidfTopnTermPerDocument 20
%default similarityTopnDocumentPerDocument 20
%default tfidfMinValue 0.4

%default sample 0.1
%default parallel 10
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'hdfs://hadoop-master.vls.icm.edu.pl:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq'
%default time '2013-09-28--10-37'
%default outputPath 'document-similarity-output/$time/'
%default jars '*.jar'
%default commonJarsPath '../../../../document-similarity-workflow/target/oozie-wf/lib/$jars'

REGISTER '$commonJarsPath'

DEFINE WeightedTFIDF pl.edu.icm.coansys.similarity.pig.udf.TFIDF('weighted');
DEFINE StemmedPairs pl.edu.icm.coansys.similarity.pig.udf.ExtendedStemmedPairs();
DEFINE KeywordSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dks');
DEFINE DocsCombinedSimilarity pl.edu.icm.coansys.similarity.pig.udf.AvgSimilarity('dkcs');
DEFINE DocToTupleMap pl.edu.icm.coansys.similarity.pig.udf.DocumentProtobufToTupleMap();

SET default_parallel $parallel
SET mapred.child.java.opts $mapredChildJavaOpts
SET mapreduce.map.java.opts $mapredChildJavaOpts
SET mapreduce.reduce.java.opts $mapredChildJavaOpts
SET pig.tmpfilecompression true
SET pig.tmpfilecompression.codec $tmpCompressionCodec
%DEFAULT scheduler default
SET mapred.fairscheduler.pool $scheduler
--SET pig.noSplitCombination true;
IMPORT 'macros.pig';

-------------------------------------------------------
-- business code section
-------------------------------------------------------
docIn = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);
B = SAMPLE docIn $sample;
--B = limit docIn 100;
doc = FOREACH B GENERATE $0 as docId, DocToTupleMap($1) as document;

doc_raportX = foreach doc generate docId, document.title as title, document.abstract as abstract, document.keywords as keywords;
STORE doc_raportX INTO '$outputPath$RAW_DATA';
doc_raport = LOAD '$outputPath$RAW_DATA' as (docId:chararray, title:chararray, abstract:chararray, keywords:{keyword:(value:chararray)});

doc_raw = foreach doc_raport generate docId, title, abstract;
doc_keyword_raw = foreach doc_raport generate docId, FLATTEN(keywords) as keywords;

-- stem, clean, filter out
doc_keyword_all = stem_words(doc_keyword_raw, docId, keywords);
doc_title_all = stem_words(doc_raw, docId, title);
doc_abstract_all = stem_words(doc_raw, docId, abstract);

-- get all words (with duplicates for tfidf)
doc_allX = UNION doc_keyword_all, doc_title_all, doc_abstract_all;

-- store document and terms
STORE doc_title_all INTO '$outputPath$DOC_TERM_TITLE';
STORE doc_keyword_all INTO '$outputPath$DOC_TERM_KEYWORDS';
STORE doc_allX INTO '$outputPath$DOC_TERM_ALL';

doc_all = LOAD '$outputPath$DOC_TERM_ALL' as (docId:chararray, term:chararray);
-- calculate tf-idf for each group of terms
tfidf_all = calculate_tfidf_nofiltering(doc_all, docId, term);
-- store tfidf values into separate direcotires
STORE tfidf_all INTO '$outputPath$TFIDF_NON_WEIGHTED_SUBDIR';

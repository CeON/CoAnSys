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
%default TFIDF_TOPN_ALL_TEMP '/tfidf/all-topn-tmp'
%default TFIDF_TOPN_ALL_SUBDIR '/tfidf/all-topn'
%default TFIDF_TF_ALL_SUBDIR '/tfidf/tf-all-topn'
%default SIMILARITY_ALL_DOCS_SUBDIR '/similarity/alldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'

%default tfidfTopnTermPerDocument 20
%default similarityTopnDocumentPerDocument 20
%default tfidfMinValue 0.4

%default sample 1.0
%default parallel 40
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'hdfs://hadoop-master.vls.icm.edu.pl:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq'
%default time '1'
%default outputPath 'document-similarity-output/$time/'
%default jars '*.jar'
%default commonJarsPath 'lib/$jars'
REGISTER '$commonJarsPath'

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

docIn = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);
B = SAMPLE docIn $sample;
--B = limit docIn 100;
doc = FOREACH B GENERATE $0 as docId, pl.edu.icm.coansys.similarity.pig.udf.DocumentProtobufToTupleMap($1) as document ;
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
doc_allX = UNION doc_keyword_all, doc_title_all, doc_abstract_all;
-- store document and terms
--STORE doc_title_all INTO '$outputPath$DOC_TERM_TITLE';
--STORE doc_keyword_all INTO '$outputPath$DOC_TERM_KEYWORDS';
STORE doc_allX INTO '$outputPath$DOC_TERM_ALL';


%default ds_removal_rate 0.99



--**************** term count *****************
in = LOAD '$outputPath$DOC_TERM_ALL' as (docId:chararray, term:chararray);
terms = foreach in generate term;
group_by_terms = group terms by term;
X = foreach group_by_terms generate group as term;
X1 = group X all; 
tcX = foreach X1 generate (long)FLOOR(COUNT(X)*$ds_removal_rate) as count;
%default tc '/term-count' 
store tcX into '$outputPath$tc';
--**************** term count *****************

--**************** word count rank *****************
%default tc '/term-count'
tc = load '$outputPath$tc' as (val:double);
group_by_terms = group in by term;
wc = foreach group_by_terms generate COUNT(in) as count, group as term, in.docId as docs;
wc_ranked = rank wc by count desc;
term_lower_tmp = filter wc_ranked by $0 > tc.val;
doc_selected_termsX = foreach term_lower_tmp generate FLATTEN(docs) as docId, term;
%default wc '/word-count-ranked';
store doc_selected_termsX into '$outputPath$wc';
--**************** word count rank *****************/

doc_selected_termsX = load '$outputPath$wc' as (docId:chararray, term:chararray);
tfidf_allX = calculate_tfidf_nofiltering(doc_selected_terms, docId, term);
-- store tfidf values into separate direcotires
STORE tfidf_allX INTO '$outputPath$TFIDF_NON_WEIGHTED_SUBDIR';
tfidf_all = load '$outputPath$TFIDF_NON_WEIGHTED_SUBDIR';
-- calculate and store topn terms per document in all results
tfidf_all_topn = get_topn_per_group(tfidf_all, docId, tfidf, 'desc', $tfidfTopnTermPerDocument);
tfidf_all_topn_projectedX = FOREACH tfidf_all_topn GENERATE top::docId AS docId, top::term AS term, top::tfidf AS tfidf;
STORE tfidf_all_topn_projectedX  INTO '$outputPath$TFIDF_TOPN_ALL_TEMP';

tfidf_all_topn_projected = load '$outputPath$TFIDF_TOPN_ALL_TEMP' as (docId:chararray, term:chararray, tfidf:float);
 
docIdTermTfidfGr = group tfidf_all_topn_projected by term;
partialSimX = foreach docIdTermTfidfGr generate FLATTEN(pl.edu.icm.coansys.similarity.pig.udf.GeneratePartialSim(*)) as (docA:chararray, docB:chararray, sim:float);
%default PARTIAL_SIM '/partial-sim'
STORE partialSimX  INTO '$outputPath$PARTIAL_SIM';
partialSim = LOAD '$outputPath$PARTIAL_SIM' as (docA:chararray, docB:chararray, sim:float);
partialSimGr = group partialSim by (docA,docB); 
fullSim = foreach partialSimGr generate group.docA as docA,group.docB as docB, (float)SUM(partialSim.sim)/(float)COUNT(partialSim.sim); 
finalResult = foreach partialSimGr{
	summed = group.docA as docA,group.docB as docB, (float)SUM(partialSim.sim)/(float)COUNT(partialSim.sim) as finalsim;
	summed_ord = order summed by finalsim desc;
	topn = limit summed_ord similarityTopnDocumentPerDocument;
	generate docA, docB, finalsim as sim;  
}
%default FINALRES '/ranked_final_val';
store finalResult into '$outputPath$FINALRES';
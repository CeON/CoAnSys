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
%default $SIMILARITY_NORMALIZED_ALL_DOCS_SUBDIR '/similarity/normalizedalldocs'
%default SIMILARITY_TOPN_DOCS_SUBDIR '/similarity/topn'

%default tfidfTopnTermPerDocument 20
%default similarityTopnDocumentPerDocument 20
%default tfidfMinValue 0.4

%default sample 0.1
%default parallel 10
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m

%default inputPath '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf'
%default outputPath 'document-similarity-output/'

--%default jars '*.jar'
--%default commonJarsPath '../../../../document-similarity-workflow/target/oozie-wf/lib/$jars'
--REGISTER '$commonJarsPath'

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
/******************************* BLEND RESULTS ************************************/
-- consider both <docIdA, docIdB,sim> and <docIdB,docIdA,sim>
XleftSim = LOAD '$outputPath$SIMILARITY_ALL_DOCS_SUBDIR' 
	as (docIdA:chararray,docIdB:chararray,sim:float);
x = limit XleftSim 1;

leftSim = foreach x generate sim;
dump x;

/******

grMax = group leftSim all;
XmaxSim = foreach grMax generate MAX(leftSim.sim) as val;

%default MAX_SIM '/simililarity/max_sim'
STORE XmaxSim INTO '$outputPath$MAX_SIM';
/*******
maxSim = LOAD '$outputPath$MAX_SIM' as (val:float);

leftSim_normalized = foreach XleftSim generate docIdA, docIdB, sim/maxSim.val;

STORE leftSim_normalized INTO '$outputPath$SIMILARITY_NORMALIZED_ALL_DOCS_SUBDIR';
*******/

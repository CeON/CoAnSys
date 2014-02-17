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



-- This pig script is a 3rd step of the process creating test set of 
-- titles pairs which are and aren't duplicates. 
-- As input data of the script you have to provide:
-- - document metadata in protocol buffers format before deduplication 
-- - output of the separate_duplicates.sh script
-- (see comments in the script separate_duplicates.sh in this project)


-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT JARS '*.jar'
%DEFAULT commonJarsPath 'lib/$JARS'

%DEFAULT inputDocs /srv/polindex/seqfile
%DEFAULT debugData /user/acz/deduplication-debug/false_duplicates/false_duplicates_text_voter_1_0
%DEFAULT outputFile /user/acz/deduplication-debug/false_duplicates/false_duplicates_titles_pairs_1_0

-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT parallel_param 16
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
-- %DEFAULT mapredChildJavaOpts -Xmx256m
-- set mapred.child.java.opts $mapredChildJavaOpts
set default_parallel $parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set dfs.client.socket-timeout 60000
%DEFAULT scheduler_pool default
SET mapred.fairscheduler.pool $scheduler_pool

-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

-- Load pairs id-title from protobuf sqfile
-- Load pairs id1-id2 from debug text files
-- Join id1 with id-title to obtain id1, title1, id2 triples
-- Join id2 with id-title to obtain id1, title1, id2, title2 quadruple

PROTOBUF = LOAD '$inputDocs' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
IDTITLE = FOREACH PROTOBUF GENERATE FLATTEN(pl.edu.icm.coansys.deduplication.document.debugging.ExtractTitleUDF(value)) as (id:chararray, title:chararray);

IDPAIRS = LOAD '$debugData' USING PigStorage('\t') AS (ids:chararray);
IDSSPLITTED = FOREACH IDPAIRS GENERATE FLATTEN(STRSPLIT(ids, ', ', 2)) AS (id1:chararray, id2:chararray);

FIRSTTITLEASSIGNED = JOIN IDTITLE BY id, IDSSPLITTED BY id1 USING 'replicated';
-- DESCRIBE FIRSTTITLEASSIGNED;
-- FIRSTTITLEASSIGNED: {IDTITLE::id: chararray,IDTITLE::title: chararray,IDSSPLITTED::id1: chararray,IDSSPLITTED::id2: chararray}

BOTHTITLESASSIGNED = JOIN IDTITLE BY id, FIRSTTITLEASSIGNED BY IDSSPLITTED::id2 USING 'replicated';
-- DESCRIBE BOTHTITLESASSIGNED;
-- BOTHTITLESASSIGNED: {IDTITLE::id: chararray,IDTITLE::title: chararray,FIRSTTITLEASSIGNED::IDTITLE::id: chararray,FIRSTTITLEASSIGNED::IDTITLE::title: chararray,FIRSTTITLEASSIGNED::IDSSPLITTED::id1: chararray,FIRSTTITLEASSIGNED::IDSSPLITTED::id2: chararray}

RESULT = FOREACH BOTHTITLESASSIGNED GENERATE FIRSTTITLEASSIGNED::IDSSPLITTED::id1 AS id1, FIRSTTITLEASSIGNED::IDSSPLITTED::id2 AS id2, FIRSTTITLEASSIGNED::IDTITLE::title AS title1, IDTITLE::title AS title2;

STORE RESULT INTO '$outputFile';

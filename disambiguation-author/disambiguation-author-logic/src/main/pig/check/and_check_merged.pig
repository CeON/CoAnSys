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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------

--REGISTER ../disambiguation/lib/*.jar
--REGISTER /usr/lib/pig/piggybank.jar

%DEFAULT and_inputDocsData merged/pbn_mbojan
%DEFAULT and_accuracy_check_output acc_stat/pbn_mbojan
%DEFAULT and_sample 1.0
%DEFAULT and_feature_info 'Intersection#EX_AUTH_FNAME#1.0#1,Intersection#EX_PERSON_PBN_ID#1.0#1,Intersection#EX_PERSON_COANSYS_ID#1.0#1'
%DEFAULT and_lang 'all'
%DEFAULT and_skip_empty_features 'true'
%DEFAULT and_use_extractor_id_instead_name 'false'
%DEFAULT and_snameToString 'false'
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('-featureinfo $and_feature_info -lang $and_lang -skipEmptyFeatures $and_skip_empty_features -useIdsForExtractors $and_use_extractor_id_instead_name -snameToString $and_snameToString');
DEFINE pairsCreation pl.edu.icm.coansys.disambiguation.author.pig.SvmUnnormalizedPairsCreator('featureInfo=$and_feature_info');

-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT and_parallel_param 8
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT mapredChildJavaOpts -Xmx2048m

set default_parallel $and_parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set mapred.child.java.opts $mapredChildJavaOpts
-- ulimit must be more than two times the heap size value !
-- set mapred.child.ulimit unlimited
set dfs.client.socket-timeout 60000
%default and_scheduler default
set mapred.fairscheduler.pool $and_scheduler 

-- ---------------------------------
--------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

-- -----------------------------------------------------
-- READING SQ, FIRST FILTERING
-- -----------------------------------------------------

A1 = LOAD '$and_inputDocsData' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
A2 = sample A1 $and_sample;
B1 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (dockey:chararray, cId:chararray, sname, metadata:map[{(chararray)}]);


-- FOR MBOJAN csv data, (1) change and_snameToString to true, (2) uncomment following block line:
-- B2 = foreach B1 generate dockey as dockey, cId as cId, sname as sname, metadata#'EX_PERSON_COANSYS_ID' as coansys_id, metadata#'EX_PERSON_PBN_ID' as pbn_id;
-- STORE B2 INTO 'mbojan_out_csv' USING PigStorage(',', '-schema');


-- preparing PAIRS for getting info about AND feature model effectiveness:
C1 = FILTER B1 BY (metadata#'EX_PERSON_COANSYS_ID' is not null) and (metadata#'EX_PERSON_PBN_ID' is not null);
C2 = group C1 by sname;
-- generate contributor pairs for each sname, among others with pbn id and coansys id intersection
D = foreach C2 generate flatten(pairsCreation(*));
-- in D we've got:
-- (8872a69e-2069-35b7-95d1-7f35dab3c41e,EX_AUTH_FNAME,1.0,1.0,EX_PERSON_PBN_ID,1.0,1.0,EX_PERSON_COANSYS_ID,0.0,0.0)
-- for us interesing is only intersection of id, to determine if 2 contributr is same or not same by pbn and by coansys separately:
E = foreach D generate $5 as pbn, $8 as coansys;
-- prepare effectiveness stats:
F = foreach E generate ((pbn == 1.0 and pbn == coansys) ? 1 : 0) as correct_same, ((pbn == 0.0 and pbn == coansys) ? 1 : 0) as correct_not_same, ((pbn == 1.0 and coansys == 0.0) ? 1 : 0) as pbn_same_coanys_not_same, ((pbn == 0.0 and coansys == 1.0) ? 1 : 0) as pbn_not_same_coanys_same;
-- sum results:
G = GROUP F ALL;
--H = FOREACH G GENERATE SUM(F.correct_same) AS correct_same;
H = FOREACH G GENERATE SUM(F.correct_same) AS correct_same, SUM(F.correct_not_same) AS correct_not_same, SUM(F.pbn_same_coanys_not_same) as pbn_same_coanys_not_same, SUM(F.pbn_not_same_coanys_same) as pbn_not_same_coanys_same;
--DESCRIBE H;
--DUMP H;
store H into '$and_accuracy_check_output' using PigStorage('-schema');

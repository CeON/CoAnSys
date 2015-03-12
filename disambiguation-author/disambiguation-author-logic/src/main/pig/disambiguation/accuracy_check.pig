--
-- This file is part of coansysId project.
-- Copyright (c) 2012-2015 ICM-UW
--
-- coansysId is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- coansysId is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU Affero General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with coansysId. If not, see <http://www.gnu.org/licenses/>.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------

--REGISTER ../disambiguation/lib/*.jar
--REGISTER /usr/lib/pig/piggybank.jar

%DEFAULT and_output_unserialized 'workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/output_unserialized'
%DEFAULT and_accuracy_check_output acc_check
%DEFAULT and_sample 1.0
%DEFAULT and_feature_info 'Intersection#EX_PERSON_ID#1.0#1,Intersection#EX_PERSON_COANSYS_ID#1.0#1'
%DEFAULT and_lang 'all'
%DEFAULT and_skip_empty_features 'true'
%DEFAULT and_use_extractor_id_instead_name 'false'
%DEFAULT and_snameToString 'false'
DEFINE pairsCreation pl.edu.icm.coansys.disambiguation.author.pig.SvmUnnormalizedPairsCreator('featureInfo=$and_feature_info');
DEFINE toMap pl.edu.icm.coansys.disambiguation.author.pig.ToMapPigConverter();

-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT and_parallel_param 16
--%DEFAULT pig_tmpfilecompression_param true
--%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
--%DEFAULT pig_cachedbag_mem_usage 0.1
--%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT mapredChildJavaOpts -Xmx2048m

set default_parallel $and_parallel_param
--set pig.tmpfilecompression $pig_tmpfilecompression_param
--set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
--set pig.cachedbag.memusage $pig_cachedbag_mem_usage
--set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
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

-- load unserialized AND results
-- externalId is original externalId person id (of course could not exist)
-- coansysIdId is UUID given during AND
unserialized = LOAD '$and_output_unserialized' as (cId:chararray, coansysId:chararray, docKey_unused:chararray, externalId, sname);

--DESCRIBE unserialized; DUMP unserialized;

A = FILTER unserialized BY coansysId is not null and externalId is not null and sname is not null;

--DESCRIBE A; DUMP A;

-- simulate schema as after EXTRACT_CONTRIDATA_GIVENDATA - needed by pairsCreation()
B = foreach A generate docKey_unused, cId, sname, toMap('EX_PERSON_ID', {(externalId)}, 'EX_PERSON_COANSYS_ID', {(coansysId)}) as metadata;

--DESCRIBE B; DUMP B;

C = group B by sname;

--DESCRIBE C; DUMP C;

-- generate contributor pairs for each sname, with external id and coansysId id intersection
D = foreach C generate flatten(pairsCreation(*));

--DESCRIBE D; DUMP D;

-- in D we've got:
-- (8872a69e-2069-35b7-95d1-7f35dab3c41e,EX_PERSON_ID,1.0,1.0,EX_PERSON_COANSYS_ID,0.0,0.0)
-- for us interesing is only intersection of id, to determine if 2 contributr is same or not same by pbn and by coansysId separately:
E = foreach D generate $2 as externalId, $5 as coansysId;

-- prepare effectiveness stats:
F = foreach E generate ((externalId == 1.0 and externalId == coansysId) ? 1 : 0) as correct_same, ((externalId == 0.0 and externalId == coansysId) ? 1 : 0) as correct_not_same, ((externalId == 1.0 and coansysId == 0.0) ? 1 : 0) as external_same_coanys_not_same, ((externalId == 0.0 and coansysId == 1.0) ? 1 : 0) as external_not_same_coanys_same;

-- sum results:
G = GROUP F ALL;

H = FOREACH G GENERATE SUM(F.correct_same) AS correct_same, SUM(F.correct_not_same) AS correct_not_same, SUM(F.external_same_coanys_not_same) as external_same_coanys_not_same, SUM(F.external_not_same_coanys_same) as external_not_same_coanys_same;

--DESCRIBE H; DUMP H;

store H into '$and_accuracy_check_output' using PigStorage('\t', '-schema');


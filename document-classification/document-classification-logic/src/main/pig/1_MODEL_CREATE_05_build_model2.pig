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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_dataEnriched /tmp/dataEnriched
%DEFAULT dc_m_int_numOfNeighbours 5
%DEFAULT dc_m_pigScript_modelBuilderClass mlknnThresBuild
%DEFAULT dc_m_hdfs_model /tmp/dataModel
%DEFAULT PIG_ENDING .pig
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.94.6-cdh4.3.0-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT 'MODEL_BLD_CLASS_$dc_m_pigScript_modelBuilderClass$PIG_ENDING';
IMPORT 'AUXIL_macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_m_double_sample 0.001
%DEFAULT parallel_param 16
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT dc_m_mapredChildJavaOpts -Xmx2000m
set mapred.child.java.opts $dc_m_mapredChildJavaOpts
set default_parallel $parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
%DEFAULT dc_m_speculative true
set mapred.map.tasks.speculative.execution $dc_m_speculative
set mapred.reduce.tasks.speculative.execution $dc_m_speculative
%DEFAULT dc_scheduler default
SET mapred.fairscheduler.pool $dc_scheduler

%DEFAULT suffix 'bla2'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
posXl = load '20131006_pos$suffix' as (group:chararray,{(categQ:chararray,neigh:int,docsocc:int)});
negXl = load '20131006_neg$suffix' as (group:chararray,{(categQ:chararray,neigh:int,docsocc:int)});
	negXs = order negXl by group asc;
	posXs = order posXl by group asc;

	Z = join posXl by group full outer,negXl by group;-- using 'merge'; -- (group::posX::categ),pos::{(categ,count,docscount)}, (group::negX::categ),neg::{(categ,count,docscount)}?

store Z into '20131006_pos_neg$suffix';
Z = load '20131006_pos_neg$suffix';
dump Z;
/**********************
**********************/






/*
B = $dc_m_pigScript_modelBuilderClass(A,$dc_m_int_numOfNeighbours);

store B into 'posNeg';
dump B;
*/
/*
retX = foreach allX6 generate FLATTEN(pl.edu.icm.coansys.classification.documents.pig.proceeders.THRES_FOR_CATEG(*,'$DEF_NEIGH'))
                as (categ:chararray, thres:int, f1:double);
$ret = filter retX by $0 is not null;
store B into '$dc_m_hdfs_model';
*/

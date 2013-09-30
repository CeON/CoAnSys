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
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_neighs /tmp/dataEnriched
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
set mapred.child.java.opts $dc_m_mapredChildJavaOptss
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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

A = LOAD '$dc_m_hdfs_neighs'  as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});--keyA,keyB,sim,{categA},{categB}
--A0 = sample A 0.0001;
--A1 = foreach A0 generate flatten(categsA) as categ;
A1 = foreach A generate flatten(categsA) as categ;
A2 = distinct A1;
A2X = group A2 all;
--A2XX = foreach A2X generate 'kujawiak',COUNT(A2);
--dump A2XX;

A3 = foreach A2X generate 1 as crosspoint, A2 as categQ;
A4 = foreach A generate *, 1 as crosspoint;
A5 = join A4 by crosspoint, A3 by crosspoint using 'replicated';
B = foreach A5 generate keyA,keyB,sim,categsA , categsB, flatten(categQ) as categQ;

C = $dc_m_pigScript_modelBuilderClass(B,$dc_m_int_numOfNeighbours);
store C into '$dc_m_hdfs_model';

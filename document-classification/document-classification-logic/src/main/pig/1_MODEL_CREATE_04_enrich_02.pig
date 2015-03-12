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
%DEFAULT jars '*.jar'
%DEFAULT commonJarsPath 'lib/$jars'

%DEFAULT dc_m_hdfs_neighs /tmp/docNeigh
%DEFAULT dc_m_hdfs_docClassifMapping /tmp/dataForDocClassif
%DEFAULT dc_m_hdfs_dataEnriched /tmp/dataEnriched
%DEFAULT dc_m_int_numOfNeighbours 4

%DEFAULT dc_m_pigScript_featureVector tfidf
%DEFAULT dc_m_pigScript_similarityMetric cosine
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
IMPORT 'AUXIL_docsim.macros.def.pig';
IMPORT 'AUXIL_macros.def.pig';
IMPORT 'SIM_$dc_m_pigScript_similarityMetric.pig';
IMPORT 'FV_$dc_m_pigScript_featureVector.pig';
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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT midproduct 'mid'
%DEFAULT lateproduct 'late'
B4 = load '$dc_m_hdfs_dataEnriched$midproduct' as (group: chararray,B3: {(key: chararray,word: chararray,tfidf: double)});
B5 = foreach B4 generate *;
-- "!=" viariant 
/************* 
CroZ = filter(cross B4, B5) by B4::group != B5::group;
G = $dc_m_pigScript_similarityMetric(CroZ); --keyA,keyB,sim
--G01 = foreach G00 generate $1, $0,$2; --keyB,keyA,sim
--G = union G00,G01;
G1 = group G by keyA;
G2 = foreach G1{
	n = order G by sim desc;
	m = limit n $dc_m_int_numOfNeighbours;
	generate m;
}
G30 = foreach G2 generate flatten($0);
*************/ 
-- "<" viariant
CroZ = filter(cross B4, B5) by B4::group < B5::group;
G00 = $dc_m_pigScript_similarityMetric(CroZ); --keyA,keyB,sim
G01 = foreach G00 generate $1, $0,$2; --keyB,keyA,sim
G = union G00,G01;
G1 = group G by keyA;
G2 = foreach G1{
	n = order G by sim desc;
	m = limit n $dc_m_int_numOfNeighbours;
	generate m;
}
G30 = foreach G2 generate flatten($0);

store G30 into '$dc_m_hdfs_dataEnriched$lateproduct';


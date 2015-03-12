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
%DEFAULT dc_m_hdfs_model /tmp/dataModel
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
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
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

DEFINE posNeg pl.edu.icm.coansys.classification.documents.pig.proceeders.FLAT_POS_NEG();
DEFINE tres pl.edu.icm.coansys.classification.documents.pig.proceeders.THRES_FOR_CATEG();
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
A = LOAD '$dc_m_hdfs_dataEnriched'  as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});--keyA,keyB,sim,{categA},{categB}
Ax = distinct A;
B1 = foreach Ax generate flatten(posNeg(keyA,categsA,categsB)) as (keyA, categQ, pos, neg);
B2 = group B1 by (keyA,categQ);
B3 = foreach B2 generate group.keyA as keyA, group.categQ as categQ, SUM(B1.pos) as pos, SUM(B1.neg) as neg;
split B3 into
        	B3pos if pos>0,
	        B3neg if neg>0;
B4pos = group B3pos by (categQ,pos);
pos = foreach B4pos generate group.categQ as categQ, group.pos as neigh, COUNT(B3pos) as docsocc;
posX = group pos by categQ;

B4neg = group B3neg by (categQ,neg);
neg = foreach B4neg generate group.categQ as categQ, group.neg as neigh, COUNT(B3neg) as docsocc;
negX = group neg by categQ;

C = join posX by group full outer,negX by group;
D = foreach C generate FLATTEN(tres(*,'$dc_m_int_numOfNeighbours')) as (categ:chararray, thres:int, f1:double);
E = filter D by $0 is not null;
store E into '$dc_m_hdfs_model';

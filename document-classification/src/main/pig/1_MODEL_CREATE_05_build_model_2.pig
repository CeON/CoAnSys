--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_neighs /tmp/dataEnriched
%DEFAULT dc_m_hdfs_model /tmp/dataModel
%DEFAULT dc_m_int_numOfNeighbours 5
%DEFAULT dc_m_pigScript_modelBuilderClass mlknnThresBuild
%DEFAULT PIG_ENDING .pig
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'
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
set default_parallel $parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
-- -----------------------------------------------------
-- -----------------------------------------------------
-- define section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE t pl.edu.icm.coansys.classification.documents.pig.proceeders.THRES_FOR_CATEG2();
DEFINE p pl.edu.icm.coansys.classification.documents.pig.proceeders.POS_NEG2();
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
A = LOAD '$dc_m_hdfs_dataEnriched'  as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});--keyA,keyB,sim,{categA},{categB}
/*************************************************/
/*************WHY ARE THERE DUPLICATES?!**********/
/*************************************************/
AA = distinct A;
AAA = group AA by keyA;
A2 = foreach AAA generate flatten(p(*)) as (categ:chararray,pos:int,neg:int);
split A2 into
	P if pos != 0,
	N if pos == 0;
P1 = group P by (categ,pos);
P2 = foreach P1 generate group.categ as categ,group.pos as count, COUNT(P) as occ; --categ,count,occ;

N1 = group N by (categ,neg);
N2 = foreach N1 generate group.categ as categ,group.neg as count, COUNT(N) as occ; --categ,count,occ;

O1 = join P2 by (categ,count) full, N2 by (categ,count);
O2 = foreach O1 generate 
	(P2::categ is not null ? P2::categ : N2::categ ) as categ,
	(int)(P2::count is not null ? P2::count : N2::count )  as count,
	(long)(P2::occ is not null ? P2::occ : 0 )  as occ_pos, 
	(long)(N2::occ is not null ? N2::occ : 0 )  as occ_neg;
O3 = group O2 by categ;
O4 = foreach O3 generate flatten(t(5,*)) as (categ:chararray, thres:int, f1:double);
/*************************************************/
/***WHY SOMETIMES THRESHOLD IS NOT DETERMINED*****/
/*************************************************/
O5 = filter O4 by thres!=-1;
O6 = filter O5 by categ is not null;
store O6 into '$dc_m_hdfs_model';

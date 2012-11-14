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
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
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
set default_parallel 16
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec gz
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

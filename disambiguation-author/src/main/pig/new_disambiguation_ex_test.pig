--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT JARS '*.jar'
%DEFAULT commonJarsPath 'lib/$JARS'

%DEFAULT dc_m_hdfs_inputDocsData '../resources/test/dane0.in'
%DEFAULT time 20130709_1009
%DEFAULT dc_m_hdfs_outputContribs '../resources/test/dane0_$time.out'
%DEFAULT dc_m_meth_extraction getBWBWFromHDFS
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader

DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
-- DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA();
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('unused#EX_YEAR#0#0,unused#EX_TITLE#0#0');
DEFINE sinlgeAND pl.edu.icm.coansys.disambiguation.author.pig.SingleAND();
DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('0.7','EX_TITLE#unused#1#1,EX_YEAR#unused#0#1');
DEFINE GenUUID pl.edu.icm.coansys.disambiguation.author.pig.GenUUID();
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-*-cdh4.*-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- ------------------------------------------------------- D: {sname: chararray,datagroup: {(sname:chararray, metadata:bytearray, contribPos:int)},count: long}
IMPORT 'AUXIL_docsim.macros.def.pig';
IMPORT 'AUXIL_macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_m_double_sample 1.0
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


--DEFINE exhaustiveAND pl.icm.edu.disambiguation('$params');
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

B = LOAD '../resources/test/dane0.in' as (cId:chararray, contribPos:int, sname:chararray, metadata:map[]); 

C = group B by sname;

D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;

F100 = foreach D generate flatten( exhaustiveAND(*) );

dump F100;
-- store F100 into '$dc_m_hdfs_outputContribs';

--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_inputDocsData /bwndata/seqfile/bazekon-20130314.sf 
%DEFAULT time 20130709_1009
%DEFAULT dc_m_hdfs_outputContribs disambiguation/outputContribs$time
%DEFAULT dc_m_meth_extraction getBWBWFromHDFS
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader

DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_SNAME_DOCUMENT_METADATA();
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
A1 = $dc_m_meth_extraction('$dc_m_hdfs_inputDocsData','$dc_m_meth_extraction_inner'); 
A2 = sample A1 $dc_m_double_sample;
B = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (key:chararray, sname:chararray, metadata:bytearray);
describe B;
C = group B by sname;
D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;
E = limit D 10;
store E into '$dc_m_hdfs_outputContribs';
/*
split D into
	D1 if count = 1,
	D100 if (count > 1 and count < 100),
	D1000 if (count >= 100 and count < 1000),
	DX if count >= 1000;
E1 = foreach D1 generate FLATTEN(genUUID(datagroup.sname)), FLATTEN(CONTRIB(datagroup.data));
E100 = foreach D100 generate exhaustiveAND(*);
E1000_1 = foreach D1000 generate approximateAND(*);
E1000_2 = foreach E1000_1 generate approximateAND(*);
EX = foreach D1 generate FLATTEN(genUUID(datagroup.sname)), FLATTEN(CONTRIB(datagroup.data));

F = union E1,E100,E1000,EX;
store F into '$dc_m_hdfs_outputContribs';
*/

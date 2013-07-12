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

--%DEFAULT dc_m_hdfs_outputContribs disambiguation/outputContribs$time
%DEFAULT dc_m_meth_extraction getBWBWFromHDFS
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader

DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_SNAME_DOCUMENT_METADATA_FOR_FILTERS();
DEFINE sinlgeAND pl.edu.icm.coansys.disambiguation.author.pig.SingleAND();
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
-- A2: {key: chararray,value: bytearray}

B = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (sname:chararray, metadata:bytearray, contribPos:int);

C = group B by sname;

D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;
-- D: {sname: chararray,datagroup: {(sname:chararray, metadata:bytearray, contribPos:int)},count: long}

-- patrzy na ostatnia kolumne w D (ilosc kontrybutorow o tym samym sname)
D1 = FILTER D BY count == 1;

E = LIMIT D1 1;

F = foreach E generate flatten( datagroup ) as (sname, metadata, contribPos);
-- F: {datagroup::sname: chararray,datagroup::metadata: bytearray,datagroup::contribPos: int}

G = foreach F generate (metadata, contribPos);

DUMP G;
-- DESCRIBE G;
-- STORE G INTO 'singleAndTest_Table';

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

%DEFAULT dc_m_hdfs_inputDocsData /srv/bwndata/seqfile/bazekon-20130314.sf
%DEFAULT time 20130709_1009
%DEFAULT dc_m_hdfs_outputContribs disambiguation/outputContribs$time
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader
%DEFAULT dc_m_str_feature_info 'TitleDisambiguator#EX_TITLE#1#1,YearDisambiguator#EX_YEAR#1#1'
%DEFAULT threshold '-1.0'
%DEFAULT lang 'pl'

--DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('$dc_m_str_feature_info','$lang');
DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('$threshold','$dc_m_str_feature_info');
--DEFINE aproximateAND pl.edu.icm.coansys.disambiguation.author.pig.AproximateAND('$threshold','$dc_m_str_feature_info');
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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

A1 = LOAD '$dc_m_hdfs_inputDocsData' USING $dc_m_meth_extraction_inner('org.apache.hadoop.io.BytesWritable', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
-- A2: {key: chararray,value: bytearray}
A2 = sample A1 $dc_m_double_sample;

B1 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (cId:chararray, contribPos:int, sname:chararray, metadata:map[{(chararray)}]);

B = FILTER B1 BY cId is not null;

C = group B by sname;
-- D: {sname: chararray, datagroup: {(cId: chararray,cPos: int,sname: chararray,data: map[{(val_0: chararray)}])}, count: long}
D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;

split D into
	D1 if count == 1,
	D100 if (count > 1);

-- -----------------------------------------------------
-- SINGLE CONTRIBUTORS ---------------------------------
-- -----------------------------------------------------
D1A = foreach D1 generate flatten( datagroup );-- as (cId:chararray, contribPos:int, sname:chararray, metadata:map);
-- E1: {cId: chararray,uuid: chararray}
E1 = foreach D1A generate cId as cId, FLATTEN(GenUUID(TOBAG(cId))) as uuid;
-- -----------------------------------------------------
-- SMALL GRUPS OF CONTRIBUTORS -------------------------
-- -----------------------------------------------------
D100A = foreach D100 generate flatten( exhaustiveAND( datagroup ) ) as (uuid:chararray, cIds:chararray);
E100 = foreach D100A generate flatten( cIds ) as cId, uuid;
-- -----------------------------------------------------
-- RESOULT ----------------- ---------------------------
-- -----------------------------------------------------
R = union E1, E100;
-- R: {cId: chararray,uuid: chararray}
-- S = ORDER R BY uuid,cId;

-- DUMP R;
store R into '$dc_m_hdfs_outputContribs';


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

%DEFAULT dc_m_hdfs_inputDocsData2 '../../test/resources/data.in' 
%DEFAULT time 20130709_1009
%DEFAULT dc_m_hdfs_outputContribs disambiguation/outputContribs$time
%DEFAULT dc_m_meth_extraction getBWBWFromHDFS
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader
%DEFAULT dc_m_str_feature_info 'TitleDisambiguator#EX_TITLE#1#1,#EX_YEAR#0#1'


DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
-- DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA();

DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('$dc_m_str_feature_info');
DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('0.7','$dc_m_str_feature_info');
DEFINE sinlgeAND pl.edu.icm.coansys.disambiguation.author.pig.SingleAND();
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
--A1 = $dc_m_meth_extraction('$dc_m_hdfs_inputDocsData','$dc_m_meth_extraction_inner'); 

--A2 = sample A1 $dc_m_double_sample;
-- A2: {key: chararray,value: bytearray}

B = load '$dc_m_hdfs_inputDocsData2' as (cId:chararray,cPos:int,sname:chararray,data:map[]);


C = group B by sname;

D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;
-- D: {sname: chararray,datagroup: {(sname:chararray, metadata:bytearray, contribPos:int)},count: long}

-- dump D;
-- patrzy na ostatnia kolumne w D (ilosc kontrybutorow o tym samym sname)
E = foreach D generate exhaustiveAND(*);
dump E;
/*
-- zmiana koncepcji dla singli:
-- dla kontrybutorow D1: porozbijac databagi (ktore przeciez maja po jednym elemencie)
-- na tabele z rekordami o tych wlasnie tuplach, wtedy w udfi'e nie bede musial zrzucac z databagow
D1A = foreach D1 generate flatten( datagroup );-- as (cId:chararray, contribPos:int, sname:chararray, metadata:map);

D1B = foreach D1A generate cId, FLATTEN(GenUUID(TOBAG(cId)));


--E1 = foreach S generate flatten( sinlgeAND( metadata, contribPos ) );
-- UUID - contribKey (gdzie dla singli UUID = contribkey
store D1A into '$dc_m_hdfs_outputContribs';
--store D1B into '$dc_m_hdfs_outputContribs'; 


-- dump E1;

-- E100 = foreach D100 generate exhaustiveAND(*) as authors;

-- udf ma wypluwac bag'a
-- UUID_1,				 UUID_2, UUID_3
-- {key_1, key_2, key_3},{key_4},{key_5, key_6}
-- to mają byc klucze kontrybutorow nie dokumentow! (w metadanych)


-- OK? F1 = foreach E1 generate flatten(TOKENIZE(authors)) as autor;
-- F100 = foreach E100 generate flatten(TOKENIZE(authors)) as autor;

-- store F1 into '$dc_m_hdfs_outputContribs';

/*
-- to mi wypluje podmacierze
E1000_1 = foreach D1000 generate approximateAND1(*); 
-- a dalej mam obliczyc te podmacierze
E1000_2 = foreach E1000_1 generate approximateAND2(*);

-- 																			datagroup.data
EX = foreach DX generate FLATTEN(genUUID(datagroup.sname)), FLATTEN(CONTRIB(*));

-- [?] czy przed union nie powinno byc rozbicie pojedynczego rekordu na rekody wzgledem UUID
-- zebysmy mieli UUID - key, UUID - key?
G = union F1,F100,F1000,FX;
store G into '$dc_m_hdfs_outputContribs';
*/

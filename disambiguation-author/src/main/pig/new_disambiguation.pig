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
%DEFAULT dc_m_meth_extraction getBWBWFromHDFS
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader
%DEFAULT dc_m_str_feature_info 'TitleDisambiguator#EX_TITLE#1#1,#EX_YEAR#0#1'

DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
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
A1 = $dc_m_meth_extraction('$dc_m_hdfs_inputDocsData','$dc_m_meth_extraction_inner'); 

A2 = sample A1 $dc_m_double_sample;
-- A2: {key: chararray,value: bytearray}

-- z kazdego dokumentu (rekordu tabeli wejsciowe) tworze rekordy z kontrybutorami
B = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (cId:chararray, contribPos:int, sname:chararray, metadata:map[{(chararray)}]); 

C = group B by sname;

D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;
-- D: {sname: chararray, datagroup: {(cId: chararray,cPos: int,sname: chararray,data: map[{(val_0: chararray)}])}, count: long}

-- patrzy na ostatnia kolumne w D (ilosc kontrybutorow o tym samym sname)
split D into
	D1 if count == 1,
	D100 if (count > 1 and count < 100),
	D1000 if (count >= 100 and count < 1000),
	DX if count >= 1000;



-- dla kontrybutorow D1: splaszczamy databagi (ktore przeciez maja po jednym elemencie)
D1A = foreach D1 generate flatten( datagroup );-- as (cId:chararray, contribPos:int, sname:chararray, metadata:map);
E1 = foreach D1A generate cId as cId, FLATTEN(GenUUID(TOBAG(cId))) as uuid;
-- contribID, UUID


D100LIM = LIMIT D100 2;
--do exhaustive trafia teraz: sname, datagroup, countl
D100A = foreach D100LIM generate flatten( exhaustiveAND(*) ) as (uuid:chararray, cIds:chararray);
describe D100A;
DUMP D100A;
-- D100A = foreach D100LIM generate flatten( exhaustiveAND( datagroup.cId, datagroup.metadata ) ) as (uuid:chararray, cIds:chararray);
-- z flatten: 
-- UUID_1, {key_1, key_2, key_3}
-- UUID_4, {key_4}
-- bez flatten:
-- UUID_1,				 UUID_2, UUID_3
-- {key_1, key_2, key_3},{key_4},{key_5, key_6}
-- gdzie key_* to klucze kontrybutorow (autorow dokumentow) w metadanych
E100 = foreach D100A generate flatten( cIds ) as cId, uuid;

describe E1;
-- E1: {cId: chararray,uuid: chararray}
DUMP E1;
describe E100;
-- E100: {cId: NULL,uuid: chararray}
DUMP E100;


-- krzaczy bo w E1 cId jest typu chararray a w E100 NULL
R = union E1, E100;
describe R;
DUMP R;

--Z = foreach D100A generate uuid;
--describe Z;
--DUMP Z;
-- store D1B into '$dc_m_hdfs_outputContribs'; 




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


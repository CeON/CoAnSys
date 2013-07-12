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
%DEFAULT dc_m_hdfs_outputContribs disambiguation/outputContribs
%DEFAULT dc_m_meth_extraction getBWBWFromHDFS
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.pig.udf.RichSequenceFileLoader

DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_SNAME_DOCUMENT_METADATA();
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

-- snameDocument...Extrator ma zwracac czworki nie trojki. Dodatkowa kolumna (pos) to indeks danego sname w tablicy autorow w metadanych
-- zebym w getContributors nie musial iterowac sie po calej liscie, tylko od razu strzelic w indeks

B = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (sname:chararray, metadata:bytearray, contribPos:int);
-- B: {key: chararray,sname: chararray,metadata: bytearray}

-- sname: {(ket,sname,metadata), (key,sname,metadata), ...)}
-- [?] czyli nie przejmujemy sie tym, ze J. Kowalski to moze byc to samo co J. Kowalski

C = group B by sname;

-- C: {group: chararray,B: {(key: chararray,sname: chararray,metadata: bytearray)}}
--     ^ sname

D = foreach C generate group as sname, B as datagroup, COUNT(B) as count;
-- D: {sname: chararray,datagroup: {(sname:chararray, metadata:bytearray, contribPos:int)},count: long}

-- D: {sname: chararray,datagroup: {(key: chararray,sname: chararray,metadata: bytearray)},count: long}
-- i powyzszego tupla dostaje do mojego udf'a. z dategroup moge sobie policzyc SIM
E = limit D 3;
-- store E into '$dc_m_hdfs_outputContribs';

-- patrzy na ostatnia kolumne w D (ilosc kontrybutorow o tym samym sname
split D into
	D1 if count == 1,
	D100 if (count > 1 and count < 100),
	D1000 if (count >= 100 and count < 1000),
	DX if count >= 1000;

-- zmiana koncepcji dla singli:
-- dla kontrybutorow D1: porozbijac databagi (ktore przeciez maja po jednym elemencie)
-- na tabele z rekordami o tych wlasnie tuplach, wtedy w udfi'e nie bede musial zrzucac z databagow
S = foreach D1 generate flatten( datagroup ) as (sname, metadata, contribPos);
-- S: {datagroup::sname: chararray,datagroup::metadata: bytearray,datagroup::contribPos: int}

E1 = foreach S generate flatten( sinlgeAND( metadata, contribPos ) );
	
-- i wtedy do singleAND dawac S i mam slicznego prostego tupla z metadata, contribPos i zwracac UUID - key

store E1 into '$dc_m_hdfs_outputContribs'; 


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

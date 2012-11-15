--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hbase_inputDocsData SpringerMetadataOnly
%DEFAULT dc_m_hdfs_neighs /tmp/docsim.pigout
%DEFAULT dc_m_int_folds 5
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
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

rawX = getProtosFromHbase('$dc_m_hbase_inputDocsData'); 
raw = SAMPLE rawX $dc_m_double_sample;

extracted_X = FOREACH raw GENERATE 
		$0 as key,
		pl.edu.icm.coansys.classification.documents.pig.extractors.
			EXTRACT_MAP_WHEN_CATEG_LIM($1,'0') as data, --		
		(int)(RANDOM()*$dc_m_int_folds) as part;

neigh = filter extracted_X by $1 is not null;
--neigh = SAMPLE neighX $dc_m_double_sample;
STORE neigh into '$dc_m_hdfs_neighs'; --key,map,part

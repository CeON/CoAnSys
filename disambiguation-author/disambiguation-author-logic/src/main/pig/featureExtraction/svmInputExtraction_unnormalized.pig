--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2015 ICM-UW
--
-- CoAnSys is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- CoAnSys is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU Affero General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT JARS '*.jar'
%DEFAULT commonJarsPath 'lib/$JARS'

%DEFAULT collection orcid
%DEFAULT dc_m_hdfs_inputDocsData '/srv/orcid/20150724'
%DEFAULT dc_m_hdfs_output 'projects/201507/and-model-from-orcid/unnormalized_data'
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
%DEFAULT dc_m_str_feature_info 'Intersection#EX_CLASSIFICATION_CODES#1#1,Intersection#EX_KEYWORDS_SPLIT#1#1,Intersection#EX_KEYWORDS#1#1,Intersection#EX_TITLE_SPLIT#1#1,Intersection#EX_YEAR#1#1,Intersection#EX_TITLE#1#1,Intersection#EX_DOC_AUTHS_SNAMES#1#1,Intersection#EX_DOC_AUTHS_FNAME_FST_LETTER#1#1,Intersection#EX_AUTH_FNAMES_FST_LETTER#1#1,Intersection#EX_AUTH_FNAME_FST_LETTER#1#1,Intersection#EX_EMAIL#1#1,Intersection#EX_PERSON_ID#1#1'
%DEFAULT threshold '-0.8' 
%DEFAULT lang 'all' 

/******
%DEFAULT dc_m_str_feature_info '#EX_AUTH_FNAME_FST_LETTER#1#1,#EX_AUTH_FNAME#1#1,#EX_AUTH_FNAMES_FST_LETTER#1#1,#EX_AUTH_FNAMES#1#1,#EX_CLASSIFICATION_CODES#1#1,#EX_COAUTH_SNAME#1#1,#EX_DOC_AUTHS_FNAME_FST_LETTER#1#1,#EX_DOC_AUTHS_SNAMES#1#1,#EX_EMAIL#1#1,#EX_EMAIL_PREFIX#1#1,#EX_KEYWORDS#1#1,#EX_KEYWORDS_SPLIT#1#1,#EX_PERSON_COANSYS_ID#1#1,#EX_PERSON_ID#1#1,#EX_PERSON_IDS#1#1,#EX_PERSON_PBN_ID#1#1,#EX_TITLE#1#1,#EX_TITLE_SPLIT#1#1,#EX_YEAR#1#1' 
%DEFAULT dc_m_str_feature_info '#EX_AUTH_INITIALS#-0.0000166#8,#EX_CLASSIFICATION_CODES#0.99#12,#EX_KEYWORDS_SPLIT#0.99#22,#EX_KEYWORDS#0.0000369#40'
%DEFAULT dc_m_str_feature_info '#EX_CLASSIFICATION_CODES#1#1,#EX_KEYWORDS_SPLIT#1#1,#EX_KEYWORDS#1#1,#EX_TITLE_SPLIT#1#1,YearDisambiguator#EX_YEAR#1#1,#EX_TITLE#1#1,CoAuthorsSnameDisambiguatorFullList#EX_DOC_AUTHS_SNAMES#1#1,#EX_DOC_AUTHS_FNAME_FST_LETTER#1#1,#EX_AUTH_FNAMES_FST_LETTER#1#1,#EX_AUTH_FNAME_FST_LETTER#1#1,#EX_PERSON_ID#1#1,#EX_EMAIL#1#1'
-- DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
******/

-- skipEmptyFeatures set to true is important for filtering snames without orcID
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('-featureinfo $dc_m_str_feature_info -lang $lang -skipEmptyFeatures true');
DEFINE pairsCreation pl.edu.icm.coansys.disambiguation.author.pig.SvmUnnormalizedPairsCreator('featureInfo=$dc_m_str_feature_info');
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
--REGISTER /usr/lib/hbase/hbase-*-cdh4.*-security.jar
--REGISTER /usr/lib/hbase/lib/guava-*.jar

REGISTER '$commonJarsPath'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_m_double_sample 0.01
%DEFAULT parallel_param 8
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT mapredChildJavaOpts -Xmx8000m
set default_parallel $parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set mapred.child.java.opts $mapredChildJavaOpts
-- ulimit must be more than two times the heap size value !
-- set mapred.child.ulimit unlimited
set dfs.client.socket-timeout 60000
set job.name 'svm_unnormalized_pairs_creator-$collection'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
A1 = LOAD '$dc_m_hdfs_inputDocsData' USING $dc_m_meth_extraction_inner('org.apache.hadoop.io.BytesWritable', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
--A2 = limit A1 1;
A2 = sample A1 $dc_m_double_sample;
A3 = foreach A2 generate flatten(snameDocumentMetaExtractor($1)) as (dockey:chararray, cId:chararray, sname:int, metadata:map[{()}]);
--A4 = FILTER A3 BY cId is not null and metadata#'EX_PERSON_ID' is not null and not IsEmpty(metadata#'EX_PERSON_ID');
A = group A3 by sname;
/*
-- statistics:
CNT1 = foreach A generate group as sname, COUNT(A4) as contrib_no;
CNT2 = group CNT1 by contrib_no;
CNT3 = foreach CNT2 generate group as contrib_no, COUNT(CNT1) as blocks_no;
CNT4 = ORDER CNT3 BY contrib_no DESC;
-- (contrib_no, blocks_no with contrib_no)
store CNT4 into '$dc_m_hdfs_output/contribBlocksStat';
*/

D = foreach A generate flatten(pairsCreation(*));
%DEFAULT unnormalizedValPairs 'unnormalizedValPairs'
store D into '$dc_m_hdfs_output/$unnormalizedValPairs';

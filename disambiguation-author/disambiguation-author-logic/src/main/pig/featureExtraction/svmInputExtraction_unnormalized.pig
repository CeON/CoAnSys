--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2013 ICM-UW
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

%DEFAULT dc_m_hdfs_inputDocsData /srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq
%DEFAULT time 2
%DEFAULT dc_m_hdfs_output svmInput/outputTime$time
%DEFAULT dc_m_meth_extraction_inner pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
--%DEFAULT dc_m_str_feature_info 'ClassificationCodeDisambiguator#EX_CLASSIFICATION_CODES#1#1,EmailDisambiguator#EX_EMAIL#1#1,EmailPrefixDisambiguator#EX_EAMIL_PREFIX#1#1,TitleSplitDisambiguator#EX_TITLE_SPLIT#1#1,KeyWordsDisambiguator#EX_KEYWORDS#1#1,KeyPhrasesDisambiguator#EX_KEYPHRASES#1#1,YearDisambiguator#EX_YEAR#1#1,CoAuthorsSnameDisambiguator#EX_COAUTH_SNAME#1#1,#EX_PERSON_ID#1#1'
%DEFAULT dc_m_str_feature_info 'CoAuthorsSnameDisambiguatorFullList#EX_AUTH_INITIALS#-0.0000166#8,ClassifCodeDisambiguator#EX_CLASSIFICATION_CODES#0.99#12,KeyphraseDisambiguator#EX_KEYWORDS_SPLIT#0.99#22,KeywordDisambiguator#EX_KEYWORDS#0.0000369#40'
%DEFAULT threshold '-0.8'
%DEFAULT lang 'all'

-- DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE snameDocumentMetaExtractor pl.edu.icm.coansys.disambiguation.author.pig.extractor.EXTRACT_CONTRIBDATA_GIVENDATA('-featureinfo $dc_m_str_feature_info -lang $lang');
DEFINE pairsCreation pl.edu.icm.coansys.disambiguation.author.pig.SvmPairsCreator('$dc_m_str_feature_info');
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-*-cdh4.*-security.jar
REGISTER /usr/lib/hbase/lib/guava-*.jar

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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
A1 = LOAD '$dc_m_hdfs_inputDocsData' USING $dc_m_meth_extraction_inner('org.apache.hadoop.io.BytesWritable', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
--A2 = limit A1 1;
--A2 = sample A1 $dc_m_double_sample;
A3 = foreach A1 generate flatten(snameDocumentMetaExtractor($1)) as (cId:chararray, sname:chararray, metadata:map[{(chararray)}]);
A4 = FILTER A3 BY cId is not null;
A = group A4 by sname;
B = foreach A generate *;
C = join A by sname, B by sname;
D = foreach C generate flatten($1),flatten($2);
--DX = filter D by $1<$4;
%DEFAULT twocontribs 'twocontribs'
store D into '$dc_m_hdfs_output$twocontribs';
E = foreach D generate  pairsCreation(*);
%DEFAULT unnormalizedValPairs 'unnormalizedValPairs'
store E into '$dc_m_hdfs_output$unnormalizedValPairs';

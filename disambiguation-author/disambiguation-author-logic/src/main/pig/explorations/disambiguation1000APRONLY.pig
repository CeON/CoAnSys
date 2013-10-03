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
%DEFAULT commonJarsPath '../lib/$JARS'

%DEFAULT dc_m_hdfs_inputDocsData extracted/springernewaffinity0/part*
%DEFAULT time 0
%DEFAULT dc_m_hdfs_outputContribs exploration/aproximateONLY_nosim_extrId_newaffinity_$time
%DEFAULT dc_m_str_feature_info 'CoAuthorsSnameDisambiguatorFullList#EX_AUTH_INITIALS#-0.0000166#8,ClassifCodeDisambiguator#EX_CLASSIFICATION_CODES#0.99#12,KeyphraseDisambiguator#EX_KEYWORDS_SPLIT#0.99#22,KeywordDisambiguator#EX_KEYWORDS#0.0000369#40'
%DEFAULT threshold '-0.8'
%DEFAULT use_extractor_id_instead_name 'true'
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!   v
%DEFAULT aproximate_remember_sim 'false'
%DEFAULT statistics 'true'

DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('$threshold','$dc_m_str_feature_info','$use_extractor_id_instead_name','$statistics');
DEFINE aproximateAND pl.edu.icm.coansys.disambiguation.author.pig.AproximateAND_BFS('$threshold', '$dc_m_str_feature_info','$aproximate_remember_sim','$use_extractor_id_instead_name','$statistics');
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
%DEFAULT parallel_param 30
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
set mapred.fairscheduler.pool bigjobs
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
D1000 = LOAD '$dc_m_hdfs_inputDocsData' as (sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long);

-- -----------------------------------------------------
-- BIG GRUPS OF CONTRIBUTORS ---------------------------
-- -----------------------------------------------------
-- D1000A: {datagroup: NULL,simTriples: NULL}
D1000A = foreach D1000 generate flatten( aproximateAND( datagroup ) ) as (datagroup, simTriples);
-- D1000B: {uuid: chararray,cIds: chararray}
-- D1000B = foreach D1000A generate flatten( exhaustiveAND( datagroup, simTriples ) ) as (uuid:chararray, cIds:chararray);
-- E1000: {cId: chararray,uuid: chararray}
-- E1000 = foreach D1000B generate flatten( cIds ) as cId, uuid;

store D1000A into '$dc_m_hdfs_outputContribs';

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

%DEFAULT and_inputDocsData extracted/springer_sample02/part*
%DEFAULT and_time 20130709_1009
%DEFAULT and_outputContribs disambiguation/outputContribs$and_time
%DEFAULT and_feature_info 'CoAuthorsSnameDisambiguatorFullList#EX_AUTH_INITIALS#-0.0000166#8,ClassifCodeDisambiguator#EX_CLASSIFICATION_CODES#0.99#12,KeyphraseDisambiguator#EX_KEYWORDS_SPLIT#0.99#22,KeywordDisambiguator#EX_KEYWORDS#0.0000369#40'
%DEFAULT and_threshold '-0.8'
%DEFAULT and_use_extractor_id_instead_name 'true'
%DEFAULT and_statistics 'true'

DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('$and_threshold','$and_feature_info','$and_use_extractor_id_instead_name','$and_statistics');
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-*-cdh4.*-security.jar
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar


-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT and_sample 1.0
%DEFAULT and_parallel_param 16
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT mapredChildJavaOpts -Xmx8000m
set mapred.child.java.opts $mapredChildJavaOpts
set default_parallel $and_parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set dfs.client.socket-timeout 60000
%DEFAULT and_scheduler benchmark80
SET mapred.fairscheduler.pool $and_scheduler
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
D100 = LOAD '$and_inputDocsData' as (sname:int, datagroup:{(cId:chararray, sname:int, data:map[{(int)}])}, count:long);

-- -----------------------------------------------------
-- SMALL GRUPS OF CONTRIBUTORS -------------------------
-- -----------------------------------------------------
D100A = foreach D100 generate flatten( exhaustiveAND( datagroup ) ) as (uuid:chararray, cIds:{(chararray)});
E100 = foreach D100A generate flatten( cIds ) as cId, uuid;

%DEFAULT one 'one'
%DEFAULT exh 'exh'
%DEFAULT appSim 'app-sim'
%DEFAULT appNoSim 'app-no-sim'


store E100 into '$and_outputContribs/$exh';


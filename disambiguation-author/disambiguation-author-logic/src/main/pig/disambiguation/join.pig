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

-- outputContribs as input for this script
%DEFAULT and_outputContribs 'workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted'
%DEFAULT and_cid_dockey 'workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/cid_dockey'
%DEFAULT and_outputPB 'finall_out'

%DEFAULT and_sample 1.0
DEFINE serialize pl.edu.icm.coansys.disambiguation.author.pig.serialization.SERIALIZE_RESULTS()
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
%DEFAULT and_parallel_param 85
%DEFAULT pig_tmpfilecompression_param true
%DEFAULT pig_tmpfilecompression_codec_param gz
%DEFAULT job_priority normal
%DEFAULT pig_cachedbag_mem_usage 0.1
%DEFAULT pig_skewedjoin_reduce_memusage 0.3
%DEFAULT mapredChildJavaOpts -Xmx8000m

set default_parallel $and_parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set mapred.child.java.opts $mapredChildJavaOpts
-- ulimit must be more than two times the heap size value !
-- set mapred.child.ulimit unlimited
set dfs.client.socket-timeout 60000
%default and_scheduler benchmark80
set mapred.fairscheduler.pool $and_scheduler 
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

%DEFAULT semi 'tmp'
%DEFAULT final 'identities'
%DEFAULT and_splitter_output 'splitted'
%DEFAULT one 'one'
%DEFAULT exh 'exh'
%DEFAULT appSim 'app-sim'
%DEFAULT appNoSim 'app-no-sim'
%DEFAULT sep '/'
%DEFAULT cid_dockey 'cid_dockey'

CidDkey = LOAD '$and_cid_dockey' as (cId:chararray, dockey:chararray);
CidAuuid = LOAD '$and_outputContribs$sep*' as (cId:chararray, uuid:chararray);
-- TODO: is that load correct with '*' ?

A = JOIN CidDkey BY cId, CidAuuid BY cId;
B = FOREACH A generate cId, uuid, docKey;
C = group B by docKey;
--TODO wyrzucic doc key z data bag'a
D = FOREACH C generate group as docKey, B as trio;
DUMP D;
DESCRIBE D;
-- E = FOREACH C generate serialize(*);
-- store E into '$and_outputPB';
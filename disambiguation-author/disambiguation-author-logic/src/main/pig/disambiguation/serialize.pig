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

-- outputContribs as input for this script
%DEFAULT and_outputContribs 'workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/outputContribs'
%DEFAULT and_cid_dockey 'workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/cid_dockey'
%DEFAULT and_outputPB 'finall_out'

DEFINE serialize pl.edu.icm.coansys.disambiguation.author.pig.serialization.SERIALIZE_RESULTS();

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
%DEFAULT mapredChildJavaOpts -Xmx1024m

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

CidDkey = LOAD '$and_cid_dockey' as (cId:chararray, docKey:chararray);
CidAuuid = LOAD '$and_outputContribs/*' as (cId:chararray, uuid:chararray);

A = JOIN CidDkey BY cId, CidAuuid BY cId;
B = FOREACH A generate CidDkey::cId as cId, uuid as uuid, docKey as docKey;
C = group B by docKey;

-- D = FOREACH C generate group as docKey, B as trio;

E = FOREACH C generate FLATTEN(serialize(*));
STORE E into '$and_outputPB' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable');

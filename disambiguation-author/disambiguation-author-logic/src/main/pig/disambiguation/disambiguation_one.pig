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

%DEFAULT and_inputDocsData workflows/pl.edu.icm.coansys-disambiguation-author-workflow/results/splitted/one
%DEFAULT and_outputContribs disambiguation/outputContribs$and_time

DEFINE GenUUID pl.edu.icm.coansys.disambiguation.author.pig.GenUUID();

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
%DEFAULT mapredChildJavaOpts -Xmx256m
set mapred.child.java.opts $mapredChildJavaOpts
set default_parallel $and_parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
set dfs.client.socket-timeout 60000
%DEFAULT and_scheduler default
SET mapred.fairscheduler.pool $and_scheduler
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

D1 = LOAD '$and_inputDocsData' as (sname: int, datagroup: {(cId: chararray,sname: int,data: map[{(int)}])}, count: long);

-- -----------------------------------------------------
-- SINGLE CONTRIBUTORS ---------------------------------
-- -----------------------------------------------------
D1A = foreach D1 generate flatten( datagroup );-- as (cId:chararray, sname:int, metadata:map);
-- E1: {cId: chararray,uuid: chararray}
E1 = foreach D1A generate cId as cId, FLATTEN(GenUUID(TOBAG(cId))) as uuid;


store E1 into '$and_outputContribs';

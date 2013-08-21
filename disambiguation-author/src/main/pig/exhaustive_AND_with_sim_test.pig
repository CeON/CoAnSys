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

%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_inputDocsData '../../test/resources/data2.in'
%DEFAULT time 20130709_1009
%DEFAULT dc_m_hdfs_outputContribs disambiguation/outputContribs$time
%DEFAULT dc_m_str_feature_info 'TitleDisambiguator#EX_TITLE#1#1,YearDisambiguator#EX_YEAR#1#1'
%DEFAULT threshold '-1.0'

DEFINE exhaustiveAND pl.edu.icm.coansys.disambiguation.author.pig.ExhaustiveAND('$threshold', '$dc_m_str_feature_info');
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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
--%DEFAULT dc_m_double_sample 1.0

%DEFAULT parallel_param 1
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
SET debug 'off'

-- Note, that for testing sname and data in map are here as chararray not int (as we are going to use normally)
A = load '$dc_m_hdfs_inputDocsData' as (datagroup:{(cId:chararray, sname:chararray, metadata:map[{(chararray)}])},simTriples:{(x:int,y:int,sim:float)});
--A = load '$dc_m_hdfs_inputDocsData' as ({(chararray, int, chararray, map[{(chararray)}])},{(x:int,y:int,sim:double)});
--A = load '$dc_m_hdfs_inputDocsData' as (datagroup,simTriples);

B = foreach A generate flatten( exhaustiveAND( datagroup, simTriples ) ) as (uuid:chararray, cIds:chararray);
store B into '$dc_m_hdfs_outputContribs';

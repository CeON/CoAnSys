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
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_src /tmp/dataForDocClassif
%DEFAULT dc_m_int_concreteInvestigatedFold 0
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
-- declare section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT TR TR
%DEFAULT TE TE
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
%DEFAULT dc_m_mapredChildJavaOpts -Xmx2000m
set mapred.child.java.opts $dc_m_mapredChildJavaOpts
set default_parallel $parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
%DEFAULT dc_m_speculative true
set mapred.map.tasks.speculative.execution $dc_m_speculative
set mapred.reduce.tasks.speculative.execution $dc_m_speculative
%DEFAULT dc_scheduler default
SET mapred.fairscheduler.pool $dc_scheduler
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
D = LOAD '$dc_m_hdfs_src' as (key:chararray,data,part:int);
/*
split A into
        Te if part == 1,
        Tr if part != 1;
*/
Te = filter D by part == $dc_m_int_concreteInvestigatedFold;
Tr = filter D by part != $dc_m_int_concreteInvestigatedFold;

sh echo "===========$dc_m_hdfs_src$TR$dc_m_int_concreteInvestigatedFold================="
sh echo "===========$dc_m_hdfs_src$TE$dc_m_int_concreteInvestigatedFold============="

store Te into '$dc_m_hdfs_src$TE$dc_m_int_concreteInvestigatedFold';
store Tr into '$dc_m_hdfs_src$TR$dc_m_int_concreteInvestigatedFold';


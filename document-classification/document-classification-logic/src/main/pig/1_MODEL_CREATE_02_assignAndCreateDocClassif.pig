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
%DEFAULT jars '*.jar'
%DEFAULT commonJarsPath 'lib/$jars'

%DEFAULT dc_m_hdfs_neighs /tmp/dataNeight
%DEFAULT dc_m_hdfs_docClassifMapping /tmp/dataForDocClassif
%DEFAULT dc_m_int_folds 5
%DEFAULT dc_m_int_categBoundary 1 
%DEFAULT dc_m_pigScript_strategyOfNeigCandidatesFiltering categsPresentGEQNumber
--%DEFAULT dc_m_pigScript_strategyOfNeigCandidatesFiltering categsPresentInAllFolds 
--%DEFAULT dc_m_pigScript_strategyOfNeigCandidatesFiltering distinctCategs
--%DEFAULT dc_m_pigScript_strategyOfNeigCandidatesFiltering categsPresentInAllFoldsInGEQNumber 
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
-- define section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE distinctCategs(C,dc_m_int_folds,num) returns ret{
	X0 = foreach $C generate flatten(categs) as categ;
	$ret = distinct X0;
}

DEFINE categsPresentInAllFolds(C,dc_m_int_folds,num) RETURNS ret{
	C1 = foreach $C generate key,flatten(categs) as categ, part;
	D = group C1 by (categ,part);--(categ,part),{(key,categ,part)}
	E = foreach D generate group.categ as categ, group.part;
	E1 = distinct E;
	E2 = group E1 by categ;
	E3 = foreach E2 generate group as categ, COUNT(E1) as count;
	E4 = filter E3 by count==$dc_m_int_folds;
	E5 = foreach E4 generate categ;
	$ret = distinct E5;
}

DEFINE categsPresentInAllFoldsInGEQNumber(C,dc_m_int_folds,num) RETURNS ret{
	C1 = foreach $C generate key,flatten(categs) as categ, part;
	D = group C1 by (categ,part);--(categ,part),{(key,categ,part)}
	E = foreach D generate group.categ as categ, group.part as part, COUNT(C1) as microcount;
	F = filter E by microcount>=$num;
	G = group F by categ;
	H = foreach G generate group as categ, COUNT(F) as minorcount;
	I = filter H by minorcount==$dc_m_int_folds;
	E = foreach I generate categ;
	$ret = distinct E;
}

DEFINE categsPresentGEQNumber(C,dc_m_int_folds,num) RETURNS ret{
	C1 = foreach $C generate key,flatten(categs) as categ, part;
	D = group C1 by categ;--(categ),{(key,categ,part)}
	E = foreach D generate group as categ, COUNT(C1) as count;
	F = filter E by count>=$num;
	G = foreach F generate categ;
	$ret = distinct G;
}

DEFINE howmanyrecords(tab) returns ret{
	A = group $tab all;
	$ret = foreach A generate COUNT($tab) as count;
}
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_m_double_sample 0.01
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

A = load '$dc_m_hdfs_neighs' as (key:chararray, data:map[], part:int); --key,map,part
A0 = foreach A generate key, data#'categories' as categs:{(categ:chararray)}, part;
--dump A0;
--C = foreach A generate key, pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_BAG_FROM_MAP(data,'categories') as categs, part; --key,categs,part
/*A1 selection is needed only when reading data filed (map) is faulty*/
--A1 = filter A0 by key matches 'SPRINGER.+';
store A0 into '$dc_m_hdfs_docClassifMapping'; --key,{categ}, part

/*
CODE BELOW IS USEFULL ONLY IF YOU ALLOW SOME PART OF CLASSIFICATION CODES TO PROCESS,
E.G. THOSE, WHICH OCCURES IN OTHER FOLDS
*/
/*
dC0 = $dc_m_pigScript_strategyOfNeigCandidatesFiltering(C,$dc_m_int_folds,$dc_m_int_categBoundary); --categs
dC00 = filter dC0 by categ!='';
--dC01 = howmanyrecords(dC00);
C1  = foreach C generate *, 1 as crosspoint;
dC1 = foreach dC00 generate *,1 as crosspoint;
C2 = join C1 by crosspoint, dC1 by crosspoint using 'replicated';--key, categs:{categ},part,crosspoint,allowed,crosspoint;
C3 = foreach C2 generate key,flatten(categs) as reg,categ as allowed, part;
C4 = filter C3 by reg == allowed;
C5 = foreach C4 generate key,reg as categ, part;
C6 = group C5 by (key,part);
C7 = foreach C6 generate group.key as key, C5.categ as categs, group.part as part;
store C7 into '$dc_m_hdfs_docClassifMapping'; --key,{categ}, part
*/

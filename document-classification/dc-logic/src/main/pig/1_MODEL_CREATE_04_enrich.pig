--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_neighs /tmp/docNeigh
%DEFAULT dc_m_hdfs_docClassifMapping /tmp/dataForDocClassif
%DEFAULT dc_m_hdfs_dataEnriched /tmp/dataEnriched
%DEFAULT dc_m_int_numOfNeighbours 4

%DEFAULT dc_m_pigScript_featureVector tfidf
%DEFAULT dc_m_pigScript_similarityMetric cosine
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
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT 'AUXIL_docsim.macros.def.pig';
IMPORT 'AUXIL_macros.def.pig';
IMPORT 'SIM_$dc_m_pigScript_similarityMetric.pig';
IMPORT 'FV_$dc_m_pigScript_featureVector.pig';
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
set default_parallel $parallel_param
set pig.tmpfilecompression $pig_tmpfilecompression_param
set pig.tmpfilecompression.codec $pig_tmpfilecompression_codec_param
set job.priority $job_priority
set pig.cachedbag.memusage $pig_cachedbag_mem_usage
set pig.skewedjoin.reduce.memusage $pig_skewedjoin_reduce_memusage
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

/*load classes having categories*/
X1 = LOAD '$dc_m_hdfs_docClassifMapping' as (key:chararray,categs:bag{(categ:chararray)}); --key,{categ}
X2 = foreach X1 generate key;
X3 = group X2 all;
X4 = foreach X3 generate X2, 1 as crosspoint;

D1 = LOAD '$dc_m_hdfs_neighs' as (key:chararray,data:map[],part:int);
/*D11 is only needed when rows are constructed previously in a faulty way */
--D11 = filter D1 by key matches 'SPRINGER.+';
D2 = foreach D1 generate *, 1 as crosspoint;
D3 = join D2 by crosspoint, X4 by crosspoint using 'replicated'; --key,map,part,crosspoint,{keys},crosspoint
D4 = foreach D3 generate key,data,flatten(X2) as allowed;
D5 = filter D4 by key==allowed;
D6 = foreach D5 generate key, data;

E = $dc_m_pigScript_featureVector(D6);

F = group E by key;
G = foreach F generate *;
CroZ = filter(cross F, G) by F::group != G::group;

G = $dc_m_pigScript_similarityMetric(CroZ); --keyA,keyB,sim
G1 = group G by keyA;
G2 = foreach G1{
	n = order G by sim desc;
	m = limit n $dc_m_int_numOfNeighbours;
	generate m;
}
/*
G2x = foreach G2 generate flatten($0);
G20 = group G2x by keyA;
G21 = foreach G20 generate COUNT(G2x);
G22 = distinct G21;
sh echo "--------------------------------"
sh echo "--------------------------------"
sh echo "--------------------------------"
dump G21;
sh echo "--------------------------------"
sh echo "--------------------------------"
sh echo "--------------------------------"
*/
G3 = foreach G2 generate flatten($0);	
H = LOAD '$dc_m_hdfs_docClassifMapping' as (key:chararray,categs:bag{(categ:chararray)}); --key,{categ}
I = join G3 by keyA, H by key;
J = join I by keyB, H by key; --keyA,keyB,sim,key,{categ},key,{categ}

K = foreach J generate $0 as keyA, $1 as keyB, $2 as sim, $4 as categsA, $6 as categsB; --keyA,keyB,sim,{categ},{categ}

store K into '$dc_m_hdfs_dataEnriched'; --keyA,keyB,sim,{categA},{categB}


--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_c_hbase_inputDocsData pdendek_springer_mo
%DEFAULT dc_c_hdfs_model /user/pdendek/model
%DEFAULT dc_c_tmp /user/pdendek/dc-c/parts/
%DEFAULT dc_c_hdfs_classified /user/pdendek/parts/parts/doc-classif/classification
%DEFAULT dc_c_int_categBoundary 1
%DEFAULT featurevector tfidf
%DEFAULT simmeth cosine
%DEFAULT neigh 5
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT 'AUXIL_docsim.macros.def.pig';
IMPORT 'AUXIL_macros.def.pig';
IMPORT 'SIM_$simmeth.pig';
IMPORT 'FV_$featurevector.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_c_double_sample 0.001
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

A = getProtosFromHbase('$dc_c_hbase_inputDocsData'); 
B = FOREACH A GENERATE 
		$0 as key,
		flatten(pl.edu.icm.coansys.classification.documents.pig.extractors.
			EXTRACT_MAP_CATEGOCC($1,'$dc_c_int_categBoundary')) as (data:map[],categocc:long);
B0 = sample B $dc_c_double_sample;
split B0 into
	B1 if categocc > 0, --classified docs
	B2 if categocc == 0; --unclassifed docs
--BEG_COMMENT

STORE B1 INTO 


C1 = foreach B1 generate key as key, data as data;
E1 = $featurevector(C1); --calc feature vector for classif

C2 = foreach B2 generate key as key, data as data;
E2 = $featurevector(C2); --calc feature vector for unclassif

CroZ = cross E1, E2; --assign classif to unclassif
F = $simmeth(CroZ); -- calculate doc similarity, returns: keyA,keyB,sim
F1 = group F by keyA;
F2 = foreach F1{  --find the $neigh most similar documents to the given one
	n = order F by sim desc; 
	m = limit n $neigh;
	generate m;
}
F3 = foreach F2 generate flatten($0);



CC1 = foreach B1 generate key as key, data as data;
describe CC1;
G = foreach CC1 generate key, pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_BAG_FROM_MAP(data,'categories') as categs; --get categories of classif docs
H = join F3 by keyB, G by key; -- add categories to the closest neighbours; obtain: keyA,keyB,sim,key,{categ}
I0 = foreach H generate keyA as keyA,flatten(categs) as categB;
I1 = group I0 by (keyA, categB);
I2 = foreach I1 generate group.keyA as keyA, group.categB as categProp, COUNT(I0) as occ, 1 as crosspoint; --count how many times category occurent in the neighbourhood

J0 = LOAD '$dc_c_hdfs_model' as (categ:chararray,thres:int,f1:double); --get the model
J1 = foreach J0 generate *, 1 as crosspoint;

K0 = join I2 by categProp, J0 by categ; --keyA,categProp,occ,categ,thres,f1;
K1 = filter K0 by occ>=thres; -- retain categories which occured greater or equal then a appropriate threshold
K2 = group K1 by keyA;

L = foreach K2 generate group as key, K1.categProp as categs; --this is the result
STORE L INTO '$dc_c_hdfs_classified';
--END_COMMENT

/************ fake code for tests 
C1 = foreach B1 generate key as key, data as data;
describe C1;
L = foreach C1 generate key, flatten(pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_BAG_FROM_MAP(data,'categories')) as categ;

L1 = foreach L generate (chararray)key,(chararray)categ;

STORE L1 INTO '$dc_c_hdfs_classified';
*************/

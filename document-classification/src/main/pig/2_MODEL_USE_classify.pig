--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_c_in_inputDocsData /tmp/inputDocsData
%DEFAULT dc_c_in_ModelDir /user/pdendek/model
%DEFAULT dc_c_tmp /tmp/tmp
%DEFAULT dc_c_OutputDir /tmp/classification
%DEFAULT dc_c_fv tfidf
%DEFAULT dc_c_sim cosine
%DEFAULT dc_c_neigh 5
%DEFAULT dc_c_meth_extraction getBWBWFromHDFS
%DEFAULT dc_c_meth_extraction_inner pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader
-- -----------------------------------------------------
-- -----------------------------------------------------
-- define section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE documentMetaExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_DOCUMENT_METADATA();
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
IMPORT 'SIM_$dc_c_sim.pig';
IMPORT 'FV_$dc_c_fv.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT dc_c_double_sample 0.3
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

X20 = $dc_c_meth_extraction('$dc_c_in_inputDocsData','$dc_c_meth_extraction_inner'); 
X200 = sample X20 $dc_c_double_sample;
X21 = foreach X200 generate $0, flatten(documentMetaExtractor($1));
X3 = foreach X21 generate $0 as key,keyTiKwAbsCatExtractor($1,0) as data:map[];
X31 = filter X3 by $1 is not null;

/*
X31 = load '/user/pdendek/workflows//coansys_dc-train/1368714576.12/5/1368714618.33//results//neighs' as (key:chararray, data:map[], part:int);
*/
store X31 into '$dc_c_tmp';
X311 = load '$dc_c_tmp' as (key:chararray,data:map[]);
X4 = foreach X311 generate key,data,(bag{tuple(chararray)})data#'categories' as categs;
X41 = foreach X4 generate *, COUNT(categs) as categocc;

split X41 into
	C1 if categocc > 0, --classified docs
	N1 if categocc == 0; --unclassifed docs
--BEG_COMMENT


C2 = foreach C1 generate key as key, data as data;
C3 = $dc_c_fv(C2); --calc feature vector for classif

N2 = foreach N1 generate key as key, data as data;
N3 = $dc_c_fv(N2); --calc feature vector for unclassif

CroZ = cross C3, N3; --assign classif to unclassif
F = $dc_c_sim(CroZ); -- calculate doc similarity, returns: keyA,keyB,sim
F1 = group F by keyA;
F2 = foreach F1{  --find the $dc_c_neigh most similar documents to the given one
	n = order F by sim desc; 
	m = limit n $dc_c_neigh;
	generate m;
}
F3 = foreach F2 generate flatten($0);

G = foreach C1 generate key, categs; --get categories of classif docs
H = join F3 by keyB, G by key; -- add categories to the closest neighbours; obtain: keyA,keyB,sim,key,{categ}
I0 = foreach H generate keyA as keyA,flatten(categs) as categB;
I1 = group I0 by (keyA, categB);
I2 = foreach I1 generate group.keyA as keyA, group.categB as categProp, COUNT(I0) as occ, 1 as crosspoint; --count how many times category occurent in the neighbourhood

J0 = LOAD '$dc_c_in_ModelDir' as (categ:chararray,thres:int,f1:double); --get the model
J00 = filter J0 by $0 is not null;
J1 = foreach J00 generate *, 1 as crosspoint;

K0 = join I2 by categProp, J00 by categ; --keyA,categProp,occ,categ,thres,f1;
K1 = filter K0 by occ>=thres; -- retain categories which occured greater or equal then a appropriate threshold
K2 = group K1 by keyA;

L = foreach K2 generate group as key, K1.categProp as categs; --this is the result
STORE L INTO '$dc_c_OutputDir';

--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dc_m_hdfs_dataEnriched /tmp/dataEnriched
%DEFAULT dc_m_hdfs_model /tmp/dataModel
%DEFAULT dc_m_hdfs_modelEvaluation /tmp/dataTestEval
%DEFAULT dc_m_pigScript_modelUse mlknnThresClassify
%DEFAULT PIG_ENDING .pig
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
IMPORT 'MODEL_BLD_CLASS_$dc_m_pigScript_modelUse$PIG_ENDING';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- macro section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE calcWXF1(in) RETURNS F1{
	WXL = group $in all;
	TFPN = foreach WXL generate SUM($in.tp) as tp, SUM($in.tn) as tn, SUM($in.fp) as fp, SUM($in.fn) as fn;
	PR = foreach TFPN generate tp/(double)(tp+fp) as p, tp/(double)(tp+fn) as r;
	$F1 = foreach PR generate 2*p*r/(p+r) as f1;
};

DEFINE calcWXTFPN(in) RETURNS TFPN{
	WXL = group $in all;
	$TFPN = foreach WXL generate SUM($in.tp) as tp, SUM($in.tn) as tn, SUM($in.fp) as fp, SUM($in.fn) as fn;
};

DEFINE calcF1(in) RETURNS F1{
	TFPN = calcTFPN($in);
	PR = foreach TFPN generate tp/(tp+fp) as p, tp/(tp+fn) as r;
	$F1 = foreach PR generate 2*p*r/(p+r) as f1;
};
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


A = LOAD '$dc_m_hdfs_dataEnriched' as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)}); --keyA,keyB,sim,{categA},{categB}

AA = distinct A;

Z0 = foreach AA generate keyA as keyA, flatten(categsB) as categB;
Z1 = group Z0 by (keyA, categB);
Z2 = foreach Z1 generate group.keyA as keyA, group.categB as categProp, COUNT(Z0) as occ, 1 as crosspoint;

X0 = LOAD '$dc_m_hdfs_model' as (categ:chararray,thres:int,f1:double);
X1 = foreach X0 generate *, 1 as crosspoint;

Y0 = join Z2 by categProp, X0 by categ; --keyA,categProp,occ,categ,thres,f1;
Y1 = filter Y0 by occ>=thres;
Y2 = group Y1 by keyA;

C = foreach Y2 generate group as key, Y1.categProp as categs;

Q1 = foreach AA generate flatten(categsA) as categQ;
Q2 = distinct Q1;
Q3 = group Q2 all;
Q5 = foreach Q3 generate COUNT(Q2) as categCount, 1 as crosspoint;

--C = $dc_m_pigScript_modelUse(M,A4);

D = join A by keyA, C by key;

W1 = foreach D generate flatten(pl.edu.icm.coansys.classification.
	documents.pig.proceeders.ACC_PREC_RECALL_F1_HL_ZOL(categsA, categsB, categs)) as (acc:double, p:double, r:double, f1:double, hl:double, zol:int), 1 as crosspoint;

W10 = group W1 all;

W110 = foreach W10 generate *, COUNT(W1) as count;
W111 = foreach W110 generate SUM(W1.acc)/(double)count as acc,
                        SUM(W1.p)/(double)count as p,
                        SUM(W1.r)/(double)count as r,
                        SUM(W1.f1)/(double)count as f1,
                        SUM(W1.hl)/(double)count as hl,
                        SUM(W1.zol)/(double)count as zol,
                        1 as crosspoint;

W11 = foreach W10 generate SUM(W1.acc)/(double)COUNT(W1) as acc,
			SUM(W1.p)/(double)COUNT(W1) as p,
			SUM(W1.r)/(double)COUNT(W1) as r,
			SUM(W1.f1)/(double)COUNT(W1) as f1,
			SUM(W1.hl)/(double)COUNT(W1) as hl,
			SUM(W1.zol)/(double)COUNT(W1) as zol,
			1 as crosspoint;

W2 = join W111 by crosspoint, Q5 by crosspoint using 'replicated';
W3 = foreach W2 generate '$dc_m_hdfs_model',acc, p,r,f1, hl/(double)categCount, zol;

store W3 into '$dc_m_hdfs_modelEvaluation';


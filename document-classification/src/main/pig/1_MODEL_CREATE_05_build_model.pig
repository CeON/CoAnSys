--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT inLocal /tmp/dataEnriched
%DEFAULT DEF_NEIGH 5
%DEFAULT MODEL_BLD_CLASS mlknnThresBuild
%DEFAULT outLocal /tmp/dataModel
%DEFAULT norbert TMP
%DEFAULT jupiter TMP2
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

IMPORT 'MODEL_BLD_CLASS_$MODEL_BLD_CLASS.pig';
IMPORT 'AUXIL_macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16

A = LOAD '$inLocal'  as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});--keyA,keyB,sim,{categA},{categB}
A1 = foreach A generate flatten(categsA) as categ;
A2 = distinct A1;
A2X = group A2 all;
--A2XX = foreach A2X generate 'kujawiak',COUNT(A2);
--dump A2XX;

A3 = foreach A2X generate 1 as crosspoint, A2 as categQ;
A4 = foreach A generate *, 1 as crosspoint;
A5 = join A4 by crosspoint, A3 by crosspoint using 'replicated';
B = foreach A5 generate keyA,keyB,sim,categsA , categsB, flatten(categQ) as categQ;

C = $MODEL_BLD_CLASS(B,$DEF_NEIGH);
store C into '$outLocal';

/***********************************************************
--/*
C1 = group C all;
C2 = foreach C1 generate 'korowody',COUNT(C);
dump C2;
--/*
--store B into '$outLocal$norbert';

--B = LOAD '$outLocal$norbert'  as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)},categQ:chararray);--keyA,keyB,sim,{categA},{categB},categQ
B1 = foreach B generate flatten(pl.edu.icm.coansys.classification.
                documents.pig.proceeders.POS_NEG(keyA,keyB,categsA,categsB,categQ)) as (keyA, categQ, pos, neg);
B2 = group B1 by (keyA,categQ);
B3 = foreach B2 generate group.keyA as keyA, group.categQ as categQ, SUM(B1.pos) as pos, SUM(B1.neg) as neg;
describe B3;
split B3 into
	B3pos if pos>0,
	B3neg if neg>0;
B4pos = group B3pos by (categQ,pos);
pos = foreach B4pos generate group.categQ as categQ, group.pos as neigh, COUNT(B3pos) as docsocc;
posX = group pos by categQ;

B4neg = group B3neg by (categQ,neg);
neg = foreach B4neg generate group.categQ as categQ, group.neg as neigh, COUNT(B3neg) as docsocc;
negX = group neg by categQ;

allX6 = join posX by $0 full outer,negX by $0; -- (group::posX::categ),pos::{(categ,count,docscount)}, (group::negX::categ),neg::{(categ,count,docscount)}?
describe allX6;

store allX6 into '$outLocal$jupiter';
--*/
--/*
allX6 = LOAD '$outLocal$jupiter' as (posX::group: chararray,posX::pos: {(categQ: chararray,neigh: long,docsocc: long)},negX::group: chararray,negX::neg: {(categQ: chararray,neigh: long,docsocc: long)});
--dump allX6;
 
C = foreach allX6 generate FLATTEN(pl.edu.icm.coansys.classification.
                documents.pig.proceeders.THRES_FOR_CATEG(*,'$DEF_NEIGH'))
                as (categ:chararray, thres:int, f1:double);
C1 = group C all;
C2 = foreach C1 generate 'korowody',COUNT(C);
dump C2;
--store C into '$outLocal'; -- categ:chararray,thres:int,f1:double
--*/
***********************************************************/

register '/mnt/srv-nfs/pdendek/20130417/CoAnSys/document-classification/src/main/pig/lib/*.jar'
set default_parallel 16;
DEFINE t pl.edu.icm.coansys.classification.documents.pig.proceeders.THRES_FOR_CATEG2();
DEFINE p pl.edu.icm.coansys.classification.documents.pig.proceeders.POS_NEG2();

A = load '/user/pdendek/workflows/coansys_dc-train/1368457555.43/5/1368457618.26/results/dataEnrich_Tr_0' as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});
/*************************************************/
/*************WHY ARE THERE DUPLICATES?!**********/
/*************************************************/
AA = distinct A;
AAA = group AA by keyA;
A2 = foreach AAA generate flatten(p(*)) as (categ:chararray,pos:int,neg:int);

--A2X = filter A2 by (pos>5 or neg>5);
--dump A2X;
/***************/
split A2 into
	P if pos != 0,
	N if pos == 0;
P1 = group P by (categ,pos);
P2 = foreach P1 generate group.categ as categ,group.pos as count, COUNT(P) as occ; --categ,count,occ;

N1 = group N by (categ,neg);
N2 = foreach N1 generate group.categ as categ,group.neg as count, COUNT(N) as occ; --categ,count,occ;

O1 = join P2 by (categ,count) full, N2 by (categ,count);
O2 = foreach O1 generate 
	(P2::categ is not null ? P2::categ : N2::categ ) as categ,
	(int)(P2::count is not null ? P2::count : N2::count )  as count,
	(long)(P2::occ is not null ? P2::occ : 0 )  as occ_pos, 
	(long)(N2::occ is not null ? N2::occ : 0 )  as occ_neg;
O3 = group O2 by categ;
store O3 into 'posNeg';

register '/mnt/srv-nfs/pdendek/20130417/CoAnSys/document-classification/src/main/pig/lib/*.jar'
set default_parallel 16;
DEFINE t pl.edu.icm.coansys.classification.documents.pig.proceeders.THRES_FOR_CATEG2();
DEFINE p pl.edu.icm.coansys.classification.documents.pig.proceeders.POS_NEG2();

O31 = load 'posNeg' as (group: chararray,O2: {(categ: chararray,count: int,occ_pos: long,occ_neg: long)});
O4 = foreach O31 generate flatten(t(5,*)) as (categ:chararray, thres:int, f1:double);
store O4 into 'finallyModel';
O40 = load 'finallyModel';
O41 = limit O40 100;
dump O41;



/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/
/********************************************************************************/

register '/mnt/srv-nfs/pdendek/20130417/CoAnSys/document-classification/src/main/pig/lib/*.jar'
set default_parallel 16;
DEFINE t pl.edu.icm.coansys.classification.documents.pig.proceeders.THRES_FOR_CATEG2();
DEFINE p pl.edu.icm.coansys.classification.documents.pig.proceeders.POS_NEG2();

A = load '/user/pdendek/workflows/coansys_dc-train/1368457555.43/5/1368457618.26/results/dataEnrich_Tr_0' as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});
/*************************************************/
/*************WHY ARE THERE DUPLICATES?!**********/
/*************************************************/
AA = distinct A;
A1 = foreach AA generate keyA,keyB,flatten(categsA) as categA, flatten(categsB) as categB;
split A1 into
		A2 if categA==categB,
		B2 if categA!=categB;

--counting a positive presence of a cc in a neighbourhood
A3 = group A2 by (keyA,categA);
A4 = foreach A3 generate group.categA as categ,COUNT(A2) as count;
A5 = group A4 by (categ,count);
A6 = foreach A5 generate group.categ as categ,group.count as count, COUNT(A4) as occ;
--A7 = order A6 by categ;
--A8 = limit A7 100;
--dump A8;

--counting a negative presence of a cc in a neighbourhood
B3 = group B2 by (keyA,categB);
B4 = foreach B3 generate group.categB as categ,COUNT(B2) as count;
B5 = group B4 by (categ,count);
B6 = foreach B5 generate group.categ as categ,group.count as count, COUNT(B4) as occ;
--B7 = order B6 by categ;
--B8 = limit B7 100;
--dump B8;

C1 = join A6 by (categ,count) full, B6 by (categ,count);
C2 = foreach C1 generate 
	(A6::categ is not null ? A6::categ : B6::categ ) as categ,
	(A6::count is not null ? A6::count : B6::count )  as count,
	(A6::occ is not null ? A6::occ : 0 )  as occ_pos, 
	(B6::occ is not null ? B6::occ : 0 )  as occ_neg;

C2A1 = filter C2 by count > 5;
dump C2A1;

/*
C21 = foreach C2 generate count;
C22 = group C21 by count;
C23 = foreach C22 generate group;
--dump C23;

C3 = group C2 by categ;
describe C3;
C4 = foreach C3 generate flatten(t(5,*)) as (categ:chararray, thres:int, f1:double);
C41 = limit C4 100;
dump C41;
*/





/*
set default_parallel 16;
Z = load 'testSuperOcc2' as (keyA, count, X, keyB, sim, categsA,categsB);
Z1 = group Z by (keyA,keyB); 
Z2 = foreach Z1 generate COUNT(Z);
*/
/*
set default_parallel 16;
X = load '/user/pdendek/workflows/coansys_dc-train/1368457555.43/5/1368457618.26/results/dataEnrich_Tr_0' as (keyA:chararray,keyB:chararray,sim:double,categsA:bag{(categA:chararray)},categsB:bag{(categB:chararray)});
XX = distinct X;
X1 = group XX by (keyA,keyB); 
X2 = foreach X1 generate COUNT(XX) as count;
X3 = group X2 by count;
X4 = foreach X3 generate group as count, COUNT(X2) as occ;
dump X4;
*/
/* w/o distinct
(1,47525)
(2,1676)
(3,78)
(4,23)
(6,2)
*/
/* w/ distinct
(1,49304)
*/


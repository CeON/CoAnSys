--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
register /usr/lib/hbase/lib/zookeeper.jar 
register /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
register /usr/lib/hbase/lib/guava-11.0.2.jar 
register lib/document-classification-1.0-SNAPSHOT.jar
register lib/document-classification-1.0-SNAPSHOT-only-dependencies.jar
-- -----------------------------------------------------
-- declaration section
-- -----------------------------------------------------
%default NEIGH_NUM 5
%default S 1
-- -----------------------------------------------------
-- load data
-- -----------------------------------------------------
docsim = LOAD '/tmp/docsim.pigout' as (keyA:chararray, keyB:chararray, sim:double); 
train =  LOAD '/tmp/train.pigout' as (key:chararray, categories:bag{(category:chararray)});
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Categories unconditional probability
-- -----------------------------------------------------
-- -----------------------------------------------------
A = group train all;	--key + categ
B = foreach A generate flatten(train), COUNT(train) as num;
C = foreach B generate key, flatten(categories), num;
D = group C by (category,num);

cat_pos_Prob_H_q = foreach D generate group.category as category, (double)$S+(double)COUNT(C)/(double)(2*$S+group.num) as pos_Prob_H_q;
STORE cat_pos_Prob_H_q INTO '/tmp/PIG_UNCOND_Prob_PH.pigout';
cat_neg_Prob_H_q = foreach cat_pos_Prob_H_q generate category as category, 1 - pos_Prob_H_q as neg_Prob_H_q;
STORE cat_neg_Prob_H_q INTO '/tmp/PIG_UNCOND_Prob_NH.pigout';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Categories conditional probability
-- -----------------------------------------------------
-- -----------------------------------------------------


-- -----------------------------------------------------
-- Select $NEIGH_NUM neightbours of a document
-- -----------------------------------------------------
docsimCategA = join docsim by keyA, train by key;
docsimCategAB_X = join docsimCategA by keyB, train by key;
docsimCategAB = foreach docsimCategAB_X	generate	docsimCategA::docsim::keyA 		as keyA,
													docsimCategA::docsim::keyB 		as keyB,
													docsimCategA::docsim::sim 		as sim,
													docsimCategA::train::categories as categA,
													train::categories 				as categB; 
docsimCategABKey = group docsimCategAB by keyA;
docsimGR_LIM = foreach docsimCategABKey{
		docsim_ordered = order docsimCategAB by sim desc;
		docsim_limited = limit docsim_ordered $NEIGH_NUM;
		generate flatten(docsim_limited); 
	} 
-- -----------------------------------------------------
-- Category Q may represent document X and its neightbours {Y}.
-- When Q describes X and Ys number of such occurences appears in the 'pos' column
-- Otherwise it appears in 'neg' column. 
-- -----------------------------------------------------
allCtg = foreach train generate flatten(categories);
uniqCtg = distinct allCtg;
CtgDocsimGR_LIM = cross uniqCtg, docsimGR_LIM; 
GRCtgDocsimGR_LIM = group CtgDocsimGR_LIM by (category, keyA);
I = foreach GRCtgDocsimGR_LIM generate flatten(
		pl.edu.icm.coansys.classification.
		documents.pig.proceeders.CATEGOCC(*))
		as (categ:chararray, pos:int, neg:int);
-- -----------------------------------------------------
-- The function counts conditional probability of such an event that a document (does not) belongs to the given category 
-- when (some of) his neightbours does  
-- -----------------------------------------------------
DEFINE getProb_E_XH(I, posORneg) RETURNS C {
	CategPos_XX = group $I by (categ,$posORneg);
	CategPos_X = foreach CategPos_XX generate group.categ as categ, flatten($I.$posORneg) as pos, COUNT($I.$posORneg) as occ;
	CategPos = DISTINCT CategPos_X;
	
	SUMCategPos_X = group CategPos by (categ, pos);
	SUMCategPos_XX = foreach SUMCategPos_X generate group.categ as categ, group.pos as pos, flatten(CategPos.occ) as occ ,SUM(CategPos.pos) as sum;
	SUMCategPos = distinct SUMCategPos_XX;
	
	PEHCategPos_XXX = foreach SUMCategPos generate categ, pos, (double)occ/(double)sum;
	$C = distinct PEHCategPos_XXX; 
}
-- -----------------------------------------------------
-- Use the function definied earlier and store results
-- ----------------------------------------------------- 
Prob_E_PH = getProb_E_XH(I,pos);
STORE Prob_E_PH INTO '/tmp/PIG_COND_Prob_E_PH.pigout';
Prob_E_NH = getProb_E_XH(I,neg);
STORE Prob_E_NH INTO '/tmp/PIG_COND_Prob_E_NH.pigout';


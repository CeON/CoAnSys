/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
register /usr/lib/hbase/lib/zookeeper.jar 
register /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
register /usr/lib/hbase/lib/guava-11.0.2.jar 
register lib/document-classification-1.0-SNAPSHOT-jar-with-dependencies.jar
-- -----------------------------------------------------
-- declaration section
-- -----------------------------------------------------
%default NEIGH_NUM 5
%default S 1
-- -----------------------------------------------------
-- load HBase rows
-- -----------------------------------------------------
docsim = LOAD '/tmp/docsim.pigout' as (keyA:chararray, keyB:chararray, sim:double);
--DESCRIBE docsim; 
train =  LOAD '/tmp/train.pigout' as (key:chararray, categories:bag{(category:chararray)});

allCtg = foreach train generate flatten(category);
uniqCtg = distinct allCtg;

allT = group train all;
train2 = foreach allT generate flatten(train), COUNT(train) as num;
ft = foreach train2 generate key, flatten(categories), num;
ftc = group ft by (category,num);

cat_pos_Prob_H_q = foreach ftc generate group.category as category, (double)$S+(double)COUNT(ft)/(double)(2*$S+group.num) as pos_Prob_H_q;
cat_neg_Prob_H_q = foreach cat_pos_Prob_H_q generate category as category, 1 - pos_Prob_H_q as neg_Prob_H_q;



docsimCategA = join docsim by keyA, train by key;
docsimCategAB_X = join docsimCategA by keyB, train by key;
docsimCategAB = foreach docsimCategAB_X	generate	docsimCategA::docsim::keyA 		as keyA,
													docsimCategA::docsim::keyB 		as keyB,
													docsimCategA::docsim::sim 		as sim,
													docsimCategA::train::categories as categA,
													train::categories 				as categB; 
-- ------------------------------------------------------
-- ------------------------------------------------------
-- ------------------------------------------------------

docsimCategABKey = group docsimCategAB by keyA;
--describe docsimCategABKey;
-- get groups of N most similar neightbours
docsimGR_LIM = foreach docsimCategABKey{
		docsim_ordered = order docsimCategAB by sim desc;
		docsim_limited = limit docsim_ordered $NEIGH_NUM;
		generate flatten(docsim_limited); -- as neightbours; -- ew. flatten(docsim_limited) 
	} 

CtgDocsimGR_LIM = cross uniqCtg, docsimGR_LIM; 




I = foreach docsimGR_LIM generate flatten(
		pl.edu.icm.coansys.classification.
		documents.pig.proceeders.CATEGOCC(*))
		as (categ:chararray, keyB:chararray, sim:double);

/*
tr_ne = join train by key, docsimGR_LIM by key;
train_ready = foreach tr_ne generate
		train::key as key,
		train::categories as categories,
		docsimGR_LIM::ne as neightbours;


D - zbiór treningowy
m - rozmiar zbiru treningowego
K - klasyfikacja zbioru treningowego
k - rozmiar sąsiedztwa
s - współczynnik wygładzający

model = foreach train


1. podaj prawdopodobieństwo każdej kategorii a priori

/--
|2. zlicz w ilu przypadkach przy n sąsiadach obiekt ma zadaną kategorię oraz i sąsiadów z danej kategorii
|3. --------------------------||--------------- nie ma zadanej kategorii--------||-----------------------
\--


uniqcnt = foreach grpd {
		sym = daily.symbol;
		uniq_sym = distinct sym;
		generate group, COUNT(uniq_sym);
	}


*/
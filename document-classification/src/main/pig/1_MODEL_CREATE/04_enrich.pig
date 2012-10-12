--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar
REGISTER '../lib/document-classification-1.0-SNAPSHOT.jar'
REGISTER '../lib/document-classification-1.0-SNAPSHOT-only-dependencies.jar'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT inLocal /tmp/docNeigh
%DEFAULT dataForDocClassif /tmp/dataForDocClassif
%DEFAULT featurevector tfidf
%DEFAULT simmeth cosine
%DEFAULT outLocal /tmp/dataEnriched
%DEFAULT neigh 4
%DEFAULT norbert TMP
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT '../AUXILdocsim.macros.def.pig';
IMPORT '../AUXIL/macros.def.pig';
IMPORT '../SIM/$simmeth.pig';
IMPORT '../FV/$featurevector.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16

X1 = LOAD '$dataForDocClassif' as (key:chararray,categs:bag{(categ:chararray)}); --key,{categ}
X2 = foreach X1 generate key;
X3 = group X2 all;
X4 = foreach X3 generate X2, 1 as crosspoint;

D1 = LOAD '$inLocal' as (key:chararray,data:map[],part:int);
D2 = foreach D1 generate *, 1 as crosspoint;
D3 = join D2 by crosspoint, X4 by crosspoint using 'replicated'; --key,map,part,crosspoint,{keys},crosspoint
D4 = foreach D3 generate key,data,flatten(X2) as allowed;
D5 = filter D4 by key==allowed;
D6 = foreach D5 generate key, data;

E = $featurevector(D6);

F = group E by key;
G = foreach F generate *;
CroZ = filter(cross F, G parallel 16) by F::group != G::group;

G = $simmeth(CroZ); --keyA,keyB,sim

--store G into '$outLocal$norbert'; --keyA,keyB,sim
--G = load '$outLocal$norbert' as (keyA:chararray, keyB:chararray,sim:double);
G1 = group G by keyA;
G2 = foreach G1{
	n = order G by sim desc;
	m = limit n $neigh;
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
H = LOAD '$dataForDocClassif' as (key:chararray,categs:bag{(categ:chararray)}); --key,{categ}
I = join G3 by keyA, H by key;
J = join I by keyB, H by key; --keyA,keyB,sim,key,{categ},key,{categ}

K = foreach J generate $0 as keyA, $1 as keyB, $2 as sim, $4 as categsA, $6 as categsB; --keyA,keyB,sim,{categ},{categ}

store K into '$outLocal'; --keyA,keyB,sim,{categA},{categB}


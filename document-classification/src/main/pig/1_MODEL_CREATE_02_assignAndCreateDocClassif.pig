--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT dataNeight /tmp/dataNeight
%DEFAULT dataForDocClassif /tmp/dataForDocClassif
%DEFAULT DEF_FOLDS 5
%DEFAULT DEF_LIM 1 
%DEFAULT filterMethod categsPresentGEQNumber
--%DEFAULT filterMethod categsPresentInAllFolds 
--%DEFAULT filterMethod distinctCategs
--%DEFAULT filterMethod categsPresentInAllFoldsInGEQNumber 
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
-- define section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE distinctCategs(C,DEF_FOLDS,num) returns ret{
	X0 = foreach $C generate flatten(categs) as categ;
	$ret = distinct X0;
}

DEFINE categsPresentInAllFolds(C,DEF_FOLDS,num) RETURNS ret{
	C1 = foreach $C generate key,flatten(categs) as categ, part;
	D = group C1 by (categ,part);--(categ,part),{(key,categ,part)}
	E = foreach D generate group.categ as categ, group.part;
	E1 = distinct E;
	E2 = group E1 by categ;
	E3 = foreach E2 generate group as categ, COUNT(E1) as count;
	E4 = filter E3 by count==$DEF_FOLDS;
	E5 = foreach E4 generate categ;
	$ret = distinct E5;
}

DEFINE categsPresentInAllFoldsInGEQNumber(C,DEF_FOLDS,num) RETURNS ret{
	C1 = foreach $C generate key,flatten(categs) as categ, part;
	D = group C1 by (categ,part);--(categ,part),{(key,categ,part)}
	E = foreach D generate group.categ as categ, group.part as part, COUNT(C1) as microcount;
	F = filter E by microcount>=$num;
	G = group F by categ;
	H = foreach G generate group as categ, COUNT(F) as minorcount;
	I = filter H by minorcount==$DEF_FOLDS;
	E = foreach I generate categ;
	$ret = distinct E;
}

DEFINE categsPresentGEQNumber(C,DEF_FOLDS,num) RETURNS ret{
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
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16

A = load '$dataNeigh' as (key:chararray, data:map[], part:int); --key,map,part
--A0 = foreach A generate key, data#'categories' as categs;
--dump A0;
C = foreach A generate key, pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_BAG_FROM_MAP(data,'categories') as categs, part;

dC0 = $filterMethod(C,$DEF_FOLDS,$DEF_LIM);
dC00 = filter dC0 by categ!='';

dC01 = howmanyrecords(dC00);

C1  = foreach C generate *, 1 as crosspoint;
dC1 = foreach dC00 generate *,1 as crosspoint;

C2 = join C1 by crosspoint, dC1 by crosspoint using 'replicated';--key, categs:{categ},part,crosspoint,allowed,crosspoint;

C3 = foreach C2 generate key,flatten(categs) as reg,categ as allowed, part;
C4 = filter C3 by reg == allowed;
C5 = foreach C4 generate key,reg as categ, part;
C6 = group C5 by (key,part);
C7 = foreach C6 generate group.key as key, C5.categ as categs, group.part as part;
store C7 into '$dataForDocClassif'; --key,{categ}, part

REGISTER '/home/pdendek/CoAnSys-build/keywords-extraction/keywords-extraction-impl/target/keywords-extraction-impl-1.6-SNAPSHOT-jar-with-dependencies.jar'
REGISTER '/home/pdendek/auxiliary/target/Au*-jar-with-dependencies.jar';
REGISTER '/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/target/*.jar'

IMPORT '/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/src/main/pig/macros.pig';
REGISTER ./analyse_oap_duplicates.py USING jython AS judf1;

set default_parallel 40

A = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/10*0_95*80*1_0*12*40/similarity/normalizedleftdocs' as (k1:chararray,k2:chararray,sim:double);
A1 = filter A by sim > 0.999;
A2 = limit A1 20;
-- dump A2;
-- A3 = foreach A2 generate judf1.donothing(k1),k2,sim;
-- dump A3;


B = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/10*0_95*80*1_0*12*40/raw' using PigStorage('\t') as (k:chararray,ti:chararray,ccs, kwds:{(kwd:chararray)});
Bx0 = foreach B generate k,ti,kwds;
--Bx1 = limit B 10;
B1 = foreach Bx0 generate k,FLATTEN(pl.edu.icm.coansys.similarity.pig.udf.SimplifyText(ti,kwds)) as (easyText:chararray);
--dump B1;

C = join A1 by k1 left, B1 by k;
--dump C;
C1 = foreach C generate k1,easyText as t1,k2;
C2 = join C1 by k2, B1 by k;
C3 = foreach C2 generate k1, t1, k2, easyText as t2;
dump C3;
D = foreach C3 generate k1,k2, FLATTEN(judf1.levenstein(t1,t2)), t1, t2;
--dump D;

store D into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/10*0_95*80*1_0*12*40/stringDistance' using PigStorage(';');
-- Total records written : 49942

D1 = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/10*0_95*80*1_0*12*40/stringDistance' using PigStorage(';') as (k1:chararray,k2:chararray,lev:double,t1:chararray,t2:chararray);



REGISTER ./analyse_oap_duplicates.py USING jython AS judf3;
Dx1 = foreach D1 generate k1,k2,t1,t2, lev,(double)ROUND(lev*100)/100 as lev2;
Dx2 = group Dx1 by lev2;
Dx3 = foreach Dx2 generate group as lev, COUNT(Dx1) as c;
dump Dx3;










D2 = filter Dx1 by lev>0.4;
D3 = group D2 all;
D4 = foreach D3 generate COUNT(D2);
dump D4;

/**********************************************/
/**********************************************/
/**********************************************/

E = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/10*0_95*80*1_0*12*40/tfidf/all-topn-tmp' as (k:chararray,term:chararray,tfidf:double);
E1 = foreach E generate k,tfidf;
E2 = group E1 by k;

E21 = foreach E2 generate group as k,COUNT(E1) as c;
E22 = group E21 by c;
E23 = foreach E22 generate group as c, COUNT(E21) as cc;
dump E23;
/**
(1,91969)
(2,69456)
(3,52654)
(4,38389)
(5,27267)
(6,19149)
(7,13318)
(8,9681)
(9,6907)
(10,5088)
(11,3822)
(12,3006)
(13,2319)
(14,1925)
(15,1606)
(16,1429)
(17,1177)
(18,1040)
(19,918)
(20,805)
(21,697)
(22,661)
(23,607)
(24,506)
(25,480)
(26,465)
(27,411)
(28,372)
(29,356)
(30,327)
(31,294)
(32,281)
(33,258)
(34,204)
(35,199)
(36,193)
(37,162)
(38,155)
(39,149)
(40,149)
(41,120)
(42,110)
(43,98)
(44,98)
(45,92)
(46,87)
(47,77)
(48,71)
(49,58)
(50,52)
(51,66)
(52,69)
(53,54)
(54,46)
(55,49)
(56,47)
(57,43)
(58,31)
(59,46)
(60,32)
(61,26)
(62,25)
(63,34)
(64,25)
(65,20)
(66,22)
(67,27)
(68,27)
(69,16)
(70,17)
(71,24)
(72,20)
(73,11)
(74,12)
(75,11)
(76,16)
(77,11)
(78,14)
(79,10)
(80,366)
**/




E22 = order E21 by c desc;
E23 = limit E22 20;
dump E23;
E21 = limit E2 20;
dump E21;

E3 = foreach E2 generate group as k, E1.tfidf as tfidfs;
E4 = foreach E3 generate judf1.sortBagDesc(tfidfs);
store E4 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/10*0_95*80*1_0*12*40/tfidf_vector_csv' using PigStorage(',');



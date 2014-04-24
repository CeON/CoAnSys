

REGISTER ./analyse_oap_duplicates.py USING jython AS judf1;

%DEFAULT pre '0-0_'
%DEFAULT val '955'
%DEFAULT post '-80-1_0-12-40' 
%DEFAULT input '$pre$val$post'

E = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/similarity/normalizedleftdocs' as (k1:chararray,k2:chararray,sim:double);
E1 = foreach E generate sim;
E2 = foreach E1 generate (double)ROUND(sim*100)/100 as simval;
E3 = group E2 by simval;
E4 = foreach E3 generate group as simval, COUNT(E2) as simcount;
store E4 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/number_of_docs_with_given_sim' using PigStorage(',');

X = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/number_of_docs_with_given_sim' using PigStorage(',');
dump X;


/**********
E22 = order E21 by c desc;
E23 = limit E22 20;
dump E23;
E21 = limit E2 20;
dump E21;

E3 = foreach E2 generate group as k, E1.tfidf as tfidfs;
E4 = foreach E3 generate judf1.sortBagDesc(tfidfs);
store E4 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/tfidf_vector_csv' using PigStorage(',');
*********/


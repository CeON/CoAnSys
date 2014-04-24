-- bash -c 'for i in `hadoop fs -ls /user/pdendek/docsim-on-oap/results_oap_eval | rev | cut -d" " -f1 | rev`; do pig -f group_by_sims.pig -param in=${i}; done;'

-- bash -c 'for i in `hadoop fs -ls /user/pdendek/docsim-on-oap/results_oap_eval | rev | cut -d" " -f1 | rev`; do hadoop fs -copyToLocal -param in=${i}_out; done;'

%default in '/user/pdendek/docsim-on-oap/results_oap_eval/A'
%default PREFIX 'hdfs://hadoop-master.vls.icm.edu.pl:8020'
%default SUFIX '/similarity/normalizedleftdocs'
%default infull '$PREFIX/$in$SUFIX'
%default out '_out'
%default outfull '$PREFIX/$in$out'

set default_parallel 40

REGISTER simple_udf.py using jython as judf;

A = load '$infull' as (k1:chararray,k2:chararray,v:double);
B = foreach A generate judf.roundV(v,2) as v:chararray;
C = group B by v;
D = foreach C generate group as v, COUNT(B);
store D into '$outfull';



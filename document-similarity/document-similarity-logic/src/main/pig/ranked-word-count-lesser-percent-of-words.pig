%default jars '*.jar'
%default commonJarsPath 'lib/$jars'
REGISTER '$commonJarsPath'

%default ds_removal_rate 0.99
%default outputPath 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/mhorst/documentssimilarity/chain/working_dir/results'
%default DOC_TERM_ALL '/term/all'
%default time '1'
--%default outputPath 'document-similarity-output/$time/'
SET default_parallel 40
SET mapred.child.java.opts -Xmx8000m

%default wc '/word-count-ranked4';
wc_ranked = load '$outputPath$wc' as (rankval:long, count:long, term:chararray,  docs:bag{(docId:chararray)});

term_lower_tmp = filter wc_ranked by rankval < (double)7600099*$ds_removal_rate;
docs_lower = foreach term_lower_tmp generate FLATTEN(docs);
docs_distinct = distinct docs_lower;
docs_count_tmp = group docs_distinct all;
docs_count = foreach docs_count_tmp generate COUNT(docs_distinct); 
dump docs_count;

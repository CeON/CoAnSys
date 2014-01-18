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

in = LOAD '$outputPath$DOC_TERM_ALL' as (docId:chararray, term:chararray);
group_by_terms = group in by term;
wc = foreach group_by_terms generate COUNT(in) as count,group as  term, in.docId as docs;
wc_ranked = rank wc by count;
%default wc '/word-count-ranked4';
store wc_ranked into '$outputPath$wc';


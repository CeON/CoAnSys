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

--**************** term count *****************
in = LOAD '$outputPath$DOC_TERM_ALL' as (docId:chararray, term:chararray);
terms = foreach in generate term;
group_by_terms = group terms by term;
X1 = group group_by_terms all; 
tc = foreach X1 generate COUNT(group_by_terms) as count; 
%default tc '/term-count2'
dump tc;
--store tc into '$outputPath$tc';
--**************** term count *****************

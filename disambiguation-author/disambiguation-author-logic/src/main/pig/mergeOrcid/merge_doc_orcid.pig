%default commonJarPath '/usr/lib/zookeeper/zookeeper.jar'
REGISTER '$commonJarPath';

%default orcid_mapredChildJavaOpts '-Xmx4g'
SET mapred.child.java.opts $orcid_mapredChildJavaOpts
%default orcid_sample 1.0
%default orcid_parallel 40
set default_parallel $orcid_parallel
%default orcid_pool 'default'
SET mapred.fairscheduler.pool $orcid_pool
%default orcid_input 'orcid_input'
%default nrml_input 'nrml_output'
%default output 'nrml_output'

docsX = LOAD '$nrml_input' USING
        pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
        ('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') AS (kD:chararray,vD:bytearray);
docs = distinct docsX;
orcidX = LOAD '$orcid_input' USING
        pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
        ('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') AS (kO:chararray,vO:bytearray);
orcid = distinct orcidX;
jnd = JOIN docs by kD, orcid by kO;
mtchd = FOREACH jnd GENERATE kD as k, vD as vD, vO as vO;
mrgd = FOREACH mtchd GENERATE FLATTEN(pl.edu.icm.coansys.disambiguation.author.pig.merger.MergeDocumentWithOrcid(k,vD,vO)) as (k:chararray, v:bytearray);
STORE mrgd INTO '$output' USING
        pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
        ('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable');

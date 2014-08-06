%default dsdemo_parallel 40
set default_parallel $dsdemo_parallel
%default dsdemo_pool 'default'
SET mapred.fairscheduler.pool $dsdemo_pool 

%default dsdemo_input 'input'
%default dsdemo_output 'output'
%default dsdemo_authors '$dsdemo_output/AUTH'
%default dsdemo_doc_basic '$dsdemo_output/DOC_BASIC'
%default dsdemo_doc_complete '$dsdemo_output/DOC_COMPLETE'

%default jars '*.jar'
%default commonJarsPath 'lib/$jars'
REGISTER '$commonJarsPath'

A = LOAD '$dsdemo_input' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);

Authors = foreach A 
	generate FLATTEN(generate pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Authors(*)) 
	as (doi:chararray,authNum:chararray,name:chararray);
DocumentsBasic = foreach A 
	generate FLATTEN(generate pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents_Basic(*)) 
	as (doi:chararray,year:chararray,title:chararray);
DocumentsComplete = foreach A 
	generate FLATTEN(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents_Complete(*)) 
	as (doi:chararray,bw2proto:bytearray);

STORE DocumentsComplete INTO '$dsdemo_doc_complete' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable');
STORE Authors INTO '$dsdemo_authors';
STORE DocumentsBasic INTO '$dsdemo_doc_basic';

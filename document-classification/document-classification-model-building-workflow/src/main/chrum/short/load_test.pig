REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'

A = LOAD '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
B = group A all;
C = foreach B generate COUNT(A);
dump C;
-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'

A = LOAD '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);

DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE documentMetaExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_DOCUMENT_METADATA();

DEFINE cc_extr  pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_KEY_CATEG();


B = limit A 1;
C = foreach B generate $0, flatten(documentMetaExtractor($1));
D = foreach C generate $0, key,keyTiKwAbsCatExtractor($1,1) as data;

dump D;

-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
A = LOAD '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
DEFINE cc_extr  pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_KEY_CATEG();
B = sample A 1;

C = foreach B generate flatten(cc_extr(*));
D = foreach C generate $2;
E = filter D by $0!=0;
dump E;

-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
A = LOAD '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
DEFINE cc_extr  pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_KEY_CATEG();
--B = sample A 1;
B = limit A 3;
C = foreach B generate $1;
dump C;






-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
A = LOAD '/srv/polindex/seqfile/polindex-yadda-20130729-text.sf' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
DEFINE cc_extr  pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_KEY_CATEG_SIZE_SIZE();
--B = sample A 1;
B = limit A 3;
C = foreach B generate cc_extr($1);
dump C;


-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
A = LOAD 'hdfs://hadoop-master:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
AA = sample A 0.01;
B = foreach AA generate key;
C = group B all;
D = foreach C generate COUNT(B);
dump D;



-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
A = LOAD 'hdfs://hadoop-master:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
DEFINE cc_extr  pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_KEY_CATEG_SIZE_SIZE();
B = sample A 0.01;
--B = limit A 3;
C = foreach B generate flatten(cc_extr($1));
D = foreach C generate $2 as size;
E = group D by size;
F = foreach E generate group as size, COUNT(D) as sizeocc;
dump F;



-----------------------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'

A = LOAD 'hdfs://hadoop-master:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
DEFINE cc_extr  pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_KEY_CATEG_SIZE_SIZE();
B = sample A 0.01;
--B = limit A 3;
C = foreach B generate flatten(cc_extr($1));
D = foreach C generate $2 as size;
E = filter D by size>0;
F = group E all;
G = foreach F generate COUNT(E);
dump G;

G = foreach F generate SUM(E.size);
dump G;

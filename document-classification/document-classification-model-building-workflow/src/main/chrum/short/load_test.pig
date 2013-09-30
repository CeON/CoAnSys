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

-----------------------------------------
A = load 'workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/results/neighs/*';
B = limit A 1;
dump B;

-------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE documentMetaExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_DOCUMENT_METADATA();


A = LOAD 'hdfs://hadoop-master:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
B = foreach A generate $0, flatten(documentMetaExtractor($1));
C = foreach B generate $0 as key,keyTiKwAbsCatExtractor($1,1) as (data:map[]);
DX = filter C by $1 is not null;
D = limit DX 1;
dump D;

E = foreach D generate key;
F = group E all;
G = foreach F generate COUNT(E);
--dump G; --(52906)


E = foreach D generate key, (bag{tuple(chararray)})data#'categories';
dump E;

F = group E all;
G = foreach F generate COUNT(E);
dump G;

-------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/lib/*.jar'
DEFINE keyTiKwAbsCatExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_MAP_WHEN_CATEG_LIM('en','removeall');
DEFINE documentMetaExtractor pl.edu.icm.coansys.classification.documents.pig.extractors.EXTRACT_DOCUMENT_METADATA();


A = LOAD 'hdfs://hadoop-master:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
A1 = sample A 0.01;
B = foreach A1 generate flatten($0), flatten(documentMetaExtractor($1));
C = foreach B generate $0 as key:chararray,(map[])keyTiKwAbsCatExtractor($1,1) as mydata;
D = filter C by mydata is not null;
E = foreach D generate mydata#'categories' as categs:{(categ:chararray)};
--E = foreach D generate flatten(mydata#'categories') as categs:{(categ:chararray)};
F = filter E by categs is not null;
G = limit F 1;
dump G;


--F = foreach E generate (bag{tuple(chararray)})categs;
F = foreach E generate (bag{})categs;
G = limit F 1;
dump G;


F = limit E 1;
dump E;

X1 = load 'tefile/te.file' as (data:map[]);
X2 = foreach X1 generate data#'title' as chararray;
dump X2;


---------------------

A = load '/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/results/doc_classif_mapping/part-m-00000,/user/pdendek/workflows/pl.edu.icm.coansys-document-classification-model-building-workflow/results/neighs' as (key:chararray, data, part:int); 

/*
split A into
        Te if part == 1,
        Tr if part != 1;
*/
Te = filter A by part == 1;
Tr = filter A by part != 1;

store Tr into '$dc_m_hdfs_src$TR$dc_m_int_concreteInvestigatedFold';
store Te into '$dc_m_hdfs_src$TE$dc_m_int_concreteInvestigatedFold';


-------------------------
REGISTER 'hdfs://hadoop-master:8020/user/pdendek/workflows/pl.edu.icm.coansys-document-similarity-workflow/lib/*.jar'


A = LOAD 'hdfs://hadoop-master:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer*.sq' using pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
B = FOREACH A GENERATE $0 as docId;
C = group B all;
D = foreach C generate COUNT(B);
dump D;



REGISTER '/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/src/main/pig/lib/*.jar'
REGISTER '/home/pdendek/CoAnSys-build/keywords-extraction/keywords-extraction-impl/target/keywords-extraction-impl-1.6-SNAPSHOT-jar-with-dependencies.jar'
REGISTER '/home/pdendek/auxiliary/target/Au*-jar-with-dependencies.jar';
IMPORT '/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/src/main/pig/macros.pig';

DEFINE ProtobufBytesToTuple com.twitter.elephantbird.pig.piggybank.ProtobufBytesToTuple('pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata');

A = LOAD 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/unibi/autoclasscode.sf' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.BytesWritable','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);

B = limit A 1000;
B1 = foreach B generate key, ProtobufBytesToTuple(value);
--B1 = foreach A generate key, ProtobufBytesToTuple(value);
B2 = foreach B1 generate 
 key
 ,DocumentMetadata.basicMetadata.title.(text,language) as tis
 ,DocumentMetadata.documentAbstract.(text,language) as abs
 ,DocumentMetadata.basicMetadata.classifCode.(source,value_bag) as ccs
;
--dump B2;

REGISTER 'analyse_unibi.py' USING jython AS udf;

/*****************************
C1 = foreach B2 generate udf.groupByLangAndFilter(key,tis,abs,ccs) as b:{ t:( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray ) };
C2 = foreach C1 generate FLATTEN(b);
--C3 = order C2 by $0;
--dump C3;

--store C2 into '/home/pdendek/unibi/test3_lang_flattened' using PigStorage(';');
--C2 = load '/home/pdendek/unibi/test3_lang_flattened' using PigStorage(';') as ( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray );

store C2 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/unibi/autoclasscodes_lang_flattened' using PigStorage(';');
*****************************/


D = foreach B2 generate FLATTEN(tis) as (ti:chararray,lang:chararray);
D1 = foreach D generate FLATTEN(udf.detectLanguage(ti)), lang as inLang:chararray;
dump D1;






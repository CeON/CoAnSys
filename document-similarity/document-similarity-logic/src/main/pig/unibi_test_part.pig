/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

/* pastebin */
/*
-- load data with elephantbird
DEFINE SEQFILE_LOADER com.twitter.elephantbird.pig.load.SequenceFileLoader;
DEFINE TEXT_CONVERTER com.twitter.elephantbird.pig.util.TextConverter;
DEFINE BYTES_CONVERTER com.twitter.elephantbird.pig.util.BytesWritableConverter;

A = LOAD 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/unibi/classcode.sf' 
 USING com.twitter.elephantbird.pig.load.SequenceFileLoader(
  '-c com.twitter.elephantbird.pig.util.BytesWritableConverter', 
  '-c com.twitter.elephantbird.pig.util.BytesWritableConverter'
 ) 
 AS (key: bytearray, value: bytearray);
A = foreach A generate (chararray)key as key, value;


*/

set default_parallel 40
set mapred.fairscheduler.pool 'bigjobs'


REGISTER '/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/src/main/pig/lib/*.jar'
REGISTER '/home/pdendek/auxiliary/target/Au*-jar-with-dependencies.jar';
IMPORT '/home/pdendek/CoAnSys-build/document-similarity/document-similarity-logic/src/main/pig/macros.pig';

DEFINE ProtobufBytesToTuple com.twitter.elephantbird.pig.piggybank.ProtobufBytesToTuple('pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata');

A = LOAD 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/unibi/classcode.sf' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.BytesWritable','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);

B = limit A 10;
B1 = foreach A generate key, ProtobufBytesToTuple(value);
--B1 = foreach A generate key, ProtobufBytesToTuple(value);
B2 = foreach B1 generate 
 key
 ,DocumentMetadata.basicMetadata.title.(text,language) as tis
 ,DocumentMetadata.documentAbstract.(text,language) as abs
 ,DocumentMetadata.basicMetadata.classifCode.(source,value_bag) as ccs
;
--dump B2;

REGISTER '../python/analyse_unibi.py' USING jython AS udfR;
C1 = foreach B2 generate udfR.out(key,tis,abs,ccs) as b:{ t:( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray ) };
C2 = foreach C1 generate FLATTEN(b);
--C3 = order C2 by $0;
--dump C3;

--store C2 into '/home/pdendek/unibi/test3_lang_flattened' using PigStorage(';');
--C2 = load '/home/pdendek/unibi/test3_lang_flattened' using PigStorage(';') as ( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray );

store C2 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/unibi/classcodes_lang_flattened' using PigStorage(';');




C21 = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/unibi/classcodes_lang_flattened' using PigStorage(';') as ( key:chararray, title:chararray, abstract:chararray, ccs:{ cc:( type:chararray, code:chararray) }, lang:chararray );

--CX = union C2,C21;
--CX1 = order CX by key;
--dump CX1;

-------- -------- -------- -------- -------- -------- 
-------- --------   TESTS  -------- -------- -------- 
-------- -------- -------- -------- -------- -------- 
-- how many types of classification codes we have?
D1 = foreach C21 generate FLATTEN(ccs);
D2 = group D1 by type;
D3 = foreach D2 generate group as type, COUNT(D1) as num;
dump D3;


-- how many concrete codes in type we have?
D1 = foreach C2{
 X1 = foreach ccs generate CONCAT($0,$1);
 generate FLATTEN(X1) as cc:chararray;
}
D2 = group D1 by cc;
D3 = foreach D2 generate group as cc, COUNT(D1) as num;
dump D3;







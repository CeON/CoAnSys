--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2015 ICM-UW
-- 
-- CoAnSys is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- CoAnSys is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU Affero General Public License for more details.
-- 
-- You should have received a copy of the GNU Affero General Public License
-- along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
--

%default jars '*.jar'
REGISTER './lib/$jars';
REGISTER '/usr/lib/pig/piggybank.jar'

%declare SEQFILE_LOADER 'com.twitter.elephantbird.pig.load.SequenceFileLoader';
%declare SEQFILE_STORAGE 'com.twitter.elephantbird.pig.store.SequenceFileStorage';
%declare TEXT_CONVERTER 'com.twitter.elephantbird.pig.util.TextConverter';
%declare BYTESWRITABLE_CONVERTER 'com.twitter.elephantbird.pig.util.BytesWritableConverter';

DEFINE ProtobufBytesToTuple com.twitter.elephantbird.pig.piggybank.ProtobufBytesToTuple('pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper');

DEFINE countstar(in) RETURNS out {
	gr = group $in all;
	$out = foreach gr generate COUNT($in) as count;
}

set default_parallel 1

--%default input 'test.sf'
%default  input '/user/pdendek/oap-document-similarity-demo/mainInputData/p*'
%default output '/user/pdendek/oap-document-similarity-demo/mainOutputData/dump' 

protos_pig1 = LOAD '$input' USING $SEQFILE_LOADER (
  '-c $TEXT_CONVERTER', '-c $BYTESWRITABLE_CONVERTER'
) AS (k:chararray, v:bytearray);
--protos_pig2 = foreach protos_pig1 generate ProtobufBytesToTuple(v);
--protos_pig3 = limit protos_pig2 1;

/*************** docs info *************/
documentsX = foreach protos_pig1 generate 
	flatten(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents(v)) as (doi:chararray, year:chararray, title:chararray);
xdoc_count = countstar(documentsX);
documents = filter documentsX by (doi is not null and  doi!='') and (year is not null and year!='') and (title is not null and title != '');
dfin = distinct documents;
doc_count = countstar(documents);
--------------------------------------------------------
--dump xdoc_count;------------------- count documents
--dump doc_count;-------------------- count documents after filter
fs -rm -r -f $output/5sep/documents.csv
--/******
--store documents into '$output/5sep/documents.csv' 
store dfin into '$output/5sep/documents.csv' 
using PigStorage('@');
--	USING org.apache.pig.piggybank.storage.CSVExcelStorage('@' ,'NO_MULTILINE','UNIX');
--****/
--dump documents;
/*************** authors info *************/
authorsX = foreach protos_pig1 generate 
	flatten(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Authors(v)) as (doi:chararray, authNum:int, name:chararray);
--dump authorsX;
xauth_count = countstar(authorsX);
authors = filter authorsX by (name is not null and name!='') and (doi is not null and doi != '');
afin = distinct authors;
auth_count = countstar(authorsX);
--------------------------------------------------------
--dump xauth_count; ---------------------- count authors in general
--dump auth_count;-------------------- count authors after filter
fs -rm -r -f $output/5sep/authors.csv
--/********
--store authors into '$output/5sep/authors.csv' 
store afin into '$output/5sep/authors.csv' 
using PigStorage('@');
--	USING org.apache.pig.piggybank.storage.CSVExcelStorage('@' ,'NO_MULTILINE','UNIX');
--*******/
--dump authors;


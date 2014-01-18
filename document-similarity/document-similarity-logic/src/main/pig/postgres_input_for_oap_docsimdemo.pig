--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2013 ICM-UW
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

%declare SEQFILE_LOADER 'com.twitter.elephantbird.pig.load.SequenceFileLoader';
%declare SEQFILE_STORAGE 'com.twitter.elephantbird.pig.store.SequenceFileStorage';
%declare TEXT_CONVERTER 'com.twitter.elephantbird.pig.util.TextConverter';
%declare BYTESWRITABLE_CONVERTER 'com.twitter.elephantbird.pig.util.BytesWritableConverter';

DEFINE ProtobufBytesToTuple com.twitter.elephantbird.pig.piggybank.ProtobufBytesToTuple('pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper');

DEFINE countstar(in) RETURNS out {
	gr = group $in all;
	$out = foreach gr generate COUNT($in) as count;
}

set default_parallel 40

--%default input 'test.sf'
%default  input '/user/pdendek/oap-document-similarity-demo/mainInputData/p*'
%default output '/user/pdendek/oap-document-similarity-demo/mainOutputData/' 

protos_pig1 = LOAD '$input' USING $SEQFILE_LOADER (
  '-c $TEXT_CONVERTER', '-c $BYTESWRITABLE_CONVERTER'
) AS (k:chararray, v:bytearray);
--protos_pig2 = foreach protos_pig1 generate ProtobufBytesToTuple(v);
--protos_pig3 = limit protos_pig2 1;

/*************** docs info *************/
documentsX = foreach protos_pig1 generate 
	flatten(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents(v)) as (doi:chararray, year:chararray, title:chararray);
xdoc_count = countstar(documentsX);
documents = filter documentsX by (doi is not null or doi!='') and (year is not null or year!='') and (title is not null or title != '');
doc_count = countstar(documents);
--------------------------------------------------------
dump xdoc_count;------------------- count documents
dump doc_count;-------------------- count documents after filter
fs -rm -r -f $output/documents.csv
store documents into '$output/documents.csv' using PigStorage(';');


/*************** authors info *************/
authorsX = foreach protos_pig1 generate 
	flatten(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Authors(v)) as (doi:chararray, authNum:int, name:chararray);
--dump authorsX;
xauth_count = countstar(authorsX);
authors = filter authorsX by (name is not null or name!='') and (doi is not null or doi != '');
auth_count = countstar(authorsX);
--------------------------------------------------------
dump xauth_count; ---------------------- count authors in general
dump auth_count;-------------------- count authors after filter
fs -rm -r -f $output/authors.csv
store authors into '$output/authors.csv' using PigStorage(';');

/************** commended out stub 
documents2X = foreach documents generate *;
documents2A = foreach documents2X generate doi as doiA;
documents2B = foreach documents2X generate doi as doiB;
sims_tmp = cross documents2A, documents2B;
sims_tmp_count = countstar(sims_tmp);

sims_tmp2 = filter sims_tmp by doiA < doiB;
sims_tmp2_count = countstar(sims_tmp2);
sims = foreach sims_tmp generate doiA, doiB, RANDOM() as sim;
--------------------------------------------------------
dump sims_tmp_count;-------------------- count cross in general
dump sims_tmp2_count;-------------------- count cross after filter
fs -rm -r -f $output/sims.csv
store sims into '$output/sims.csv' using PigStorage(';');
**************/

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

%default dsdemo_parallel 40
set default_parallel $dsdemo_parallel
%default dsdemo_pool 'default'
SET mapred.fairscheduler.pool $dsdemo_pool 

%default dsdemo_input 'input'
%default dsdemo_output 'output'
%default dsdemo_authors '$dsdemo_output/AUTH'
%default dsdemo_doc_basic '$dsdemo_output/DOC_BASIC'
%default dsdemo_doc_complete '$dsdemo_output/DOC_COMPLETE'

%default commonJarsPath '/usr/lib/zookeeper/zookeeper.jar'
REGISTER '$commonJarsPath'

data = LOAD '$dsdemo_input' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable') 
	as (k:chararray, v:bytearray);
authX = foreach data
	generate FLATTEN(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Authors(v)) 
	as (doi:chararray,authNum:chararray,name:chararray);
authX2 = filter authX by (
	$0 is not null 
	and $1 is not null 
	and $2 is not null); 
auth = distinct authX2;
bscX = foreach data
	generate FLATTEN(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents_Basic(v)) 
	as (doi:chararray,year:chararray,title:chararray);
bscX2 = filter bscX by (
	$0 is not null 
	and $1 is not null 
	and $2 is not null);
bsc = distinct bsc;
cmplX = foreach data
	generate FLATTEN(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents_Complete(v)) 
	as (doi:chararray,bw2proto:bytearray);
cmpl = filter cmplX by (
	$0 is not null 
	and $1 is not null); 
STORE auth INTO '$dsdemo_authors';
STORE bsc INTO '$dsdemo_doc_basic';
STORE cmpl INTO '$dsdemo_doc_complete' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable');

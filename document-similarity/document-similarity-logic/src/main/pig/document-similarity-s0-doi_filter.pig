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
%default commonJarsPath '../../../../document-similarity-workflow/target/oozie-wf/lib/$jars'
REGISTER '$commonJarsPath'

%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx8000m
%default parallel 40
%default sample 0.1
SET default_parallel $parallel
SET mapred.child.java.opts $mapredChildJavaOpts
SET pig.tmpfilecompression true
SET pig.tmpfilecompression.codec $tmpCompressionCodec
%DEFAULT ds_scheduler default
SET mapred.fairscheduler.pool $ds_scheduler

%default inputPath 'hdfs://hadoop-master.vls.icm.edu.pl:8020/srv/bwndata/seqfile/springer-metadata/springer-20120419-springer0*.sq'
%default DOI '/doi'

IMPORT 'macros.pig';

-------------------------------------------------------
-- business code section
-------------------------------------------------------
fs -rm -r -f $inputPath$DOI

docIn = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.
	RichSequenceFileLoader('org.apache.hadoop.io.Text','org.apache.hadoop.io.BytesWritable') 
	as (key:chararray, value:bytearray);
A = SAMPLE docIn $sample;
A1 = foreach A generate 
	flatten(pl.edu.icm.coansys.similarity.pig.udf.DocSimDemo_Documents(value)) as (doi:chararray, year:chararray, title:chararray), key, value;
A2 = filter A1 by (doi is not null and doi!='') and (year is not null and year!='') and (title is not null and title != '');
B = foreach A2 generate doi,value;

STORE B INTO '$inputPath$DOI';


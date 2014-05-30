--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

IMPORT 'macros.pig';
%DEFAULT jars '*.jar';
REGISTER 'lib/$jars'

A = LOAD 'hbase://pbnWorks' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto', '-loadKey true') as (key:chararray, proto:bytearray);
STORE A INTO 'mbojanAND/pbnWorks' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable');







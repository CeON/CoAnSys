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

-------------------------------------------------------
-- load BWMeta documents form sequence files stored in hdfs
-------------------------------------------------------
DEFINE load_chararray_bytearray_hdfs(inputPath, keyClass,valueClass) RETURNS doc {
        $doc = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('$keyClass', '$valueClass') as (k:chararray, v:bytearray);
};

DEFINE load_chararray_chararray_hdfs(inputPath, keyClass,valueClass) RETURNS doc {
	$doc = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('$keyClass', '$valueClass') as (k:chararray, v:chararray);
};

DEFINE load_anydata_hdfs(inputPath, keyClass,valueClass) RETURNS doc {
    $doc = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('$keyClass', '$valueClass');
};

DEFINE load_bwndata_hdfs(inputPath) RETURNS doc {
    $doc = LOAD '$inputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.BytesWritable', 'org.apache.hadoop.io.BytesWritable');
};

DEFINE load_citations_hdfs(citInputPath) RETURNS cit {
    $cit = LOAD '$citInputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.Text');
};

DEFINE load_keywords_hdfs(keywordsInputPath) RETURNS kwds {
    $kwds = LOAD '$keywordsInputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWitable');
};

-- DEFINE store_bwndata_hdfs(outputData, outputPath) RETURNS none {
--     STORE $outputData INTO '$outputPath' USING pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader('org.apache.hadoop.io.BytesWritable', 'org.apache.hadoop.io.BytesWritable');
-- };

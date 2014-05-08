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

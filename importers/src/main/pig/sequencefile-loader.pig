%default inputPath 'sample-data'
%default commonJarsPath '../../../target/importers-1.0-SNAPSHOT.jar'

%declare SEQFILE_STORAGE 'pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader'

REGISTER '$commonJarsPath';

data = LOAD '$inputPath' AS (id: long, email: chararray, age: int, salary: double, tax: float, job: bytearray);

STORE (FOREACH data GENERATE id, email) INTO 'tmp1' USING $SEQFILE_STORAGE('org.apache.hadoop.io.LongWritable', 'org.apache.hadoop.io.Text');
STORE (FOREACH data GENERATE age, salary) INTO 'tmp2' USING $SEQFILE_STORAGE('org.apache.hadoop.io.IntWritable', 'org.apache.hadoop.io.DoubleWritable');
STORE (FOREACH data GENERATE tax, job) INTO 'tmp3' USING $SEQFILE_STORAGE('org.apache.hadoop.io.FloatWritable', 'org.apache.hadoop.io.BytesWritable');

data1 = LOAD 'tmp1' USING $SEQFILE_STORAGE() AS (id: long, email: chararray);
DUMP data1;

data2 = LOAD 'tmp2' USING $SEQFILE_STORAGE() AS (age, salary);
DUMP data2;

-- DUMP will not display content of BytesWritable (because it does not use SequenceFileLoader under the hood)
-- but we can correclty store it using a right loader/storer
data3 = LOAD 'tmp3' USING $SEQFILE_STORAGE() AS (tax: double, job: bytearray);
STORE data3 INTO 'tmp3sf' USING $SEQFILE_STORAGE('org.apache.hadoop.io.DoubleWritable', 'org.apache.hadoop.io.BytesWritable');

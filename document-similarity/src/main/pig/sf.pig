REGISTER ../../../../importers/target/importers-1.0-SNAPSHOT.jar

DEFINE rs pl.edu.icm.coansys.similarity.pig.udf.RichSequenceFileLoader();
DEFINE converter pl.edu.icm.coansys.importers.pig.udf.DocumentProtoToTuple();

A = LOAD 'grotoap10_dump_1349684059443' AS rs;
B = FOREACH A GENERATE converter($0);
DUMP B;
--B1 = FOREACH B GENERATE $1 AS mproto, $2 AS cproto;
--C = FOREACH B1 GENERATE rowkey, pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple(mproto, cproto) AS meta;
--D = FOREACH C GENERATE meta#'title';
--DUMP C;

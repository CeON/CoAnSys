REGISTER ../../../../importers/target/importers-1.0-SNAPSHOT.jar

DEFINE RichSequenceFileLoader pl.edu.icm.coansys.importers.pig.udf.RichSequenceFileLoader();
DEFINE DocumentProtoPartsTupler pl.edu.icm.coansys.importers.pig.udf.DocumentProtoPartsTupler();
DEFINE DocumentFielder pl.edu.icm.coansys.importers.pig.udf.DocumentProtobufBytesToTuple();
DEFINE ToDataByteArray pl.edu.icm.coansys.importers.pig.udf.BytesToDataByteArray();

A = LOAD 'grotoap10_dump/dproto-m-00000' USING RichSequenceFileLoader();
B = FOREACH A GENERATE FLATTEN(DocumentProtoPartsTupler($1)) AS (rowId, mproto, cproto);
C = FOREACH B GENERATE DocumentFielder(mproto, cproto) AS fields;
D = FOREACH C GENERATE fields#'title';

A1 = LOAD 'grotoap10_dump/mproto-m-00000' USING RichSequenceFileLoader();
B1 = FOREACH A1 GENERATE ToDataByteArray($1) AS meta;
C1 = FOREACH B1 GENERATE DocumentFielder(meta) AS fields;
D1 = FOREACH C1 GENERATE fields#'title';

STORE A INTO '$output';
AA = LOAD '$output' USING RichSequenceFileLoader();

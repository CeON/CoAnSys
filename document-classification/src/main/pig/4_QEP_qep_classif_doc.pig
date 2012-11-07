--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT commonJarsPath 'lib/*.jar'

%DEFAULT DEF_SRC /user/pdendek/parts/alg_doc_classif
%DEFAULT DEF_DST _result_docclassif_CodeByDoc
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar

REGISTER '$commonJarsPath'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- set section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec gz
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

A = LOAD '$DEF_SRC';
B = foreach A generate $1, $0;

STORE B INTO 'hbase://$DEF_DST'
       USING org.apache.pig.backend.hadoop.hbase.HBaseStorage(
       'value:value');

--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.92.1-cdh4.0.1-security.jar 
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar
REGISTER '../lib/document-classification-1.0-SNAPSHOT.jar'
REGISTER '../lib/document-classification-1.0-SNAPSHOT-only-dependencies.jar'
-- -----------------------------------------------------
-- -----------------------------------------------------
-- default section
-- -----------------------------------------------------
-- -----------------------------------------------------
%DEFAULT DEF_TO_TRANSLATE /user/pdendek/parts/alg_doc_classif
%DEFAULT DEF_DICTIONARY /user/pdendek/parts/alg_mapping_rowid_docid
%DEFAULT DEF_DST /user/pdendek/parts/alg_translated_sth
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT '../AUXIL/macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16

A = LOAD '$DEF_TO_TRANSLATE';
B = LOAD '$DEF_DICTIONARY';

C = join B by $0, A by $0;
D = foreach C generate $1, $3..;

STORE D INTO '$DEF_DST';

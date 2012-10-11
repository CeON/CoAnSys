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
%DEFAULT DEF_SRC SpringerMetadataOnly
%DEFAULT DEF_DST /tmp/docsim.pigout
%DEFAULT DEF_LIM 5
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT 'docsim.macros.def.pig';
IMPORT '../macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
raw = getProtosFromHbase($DEF_SRC); 
extracted_X = FOREACH raw GENERATE 
		FLATTEN(pl.edu.icm.coansys.classification.
		documents.pig.extractors.EXTRACT_KEY_TI_ABS_KW($0,$1));
extracted = filter extracted_X by $0 is not null;	

cosined = measureCosineDistane(tfidfed);
--STORE withClassif INTO '$DEF_DST';

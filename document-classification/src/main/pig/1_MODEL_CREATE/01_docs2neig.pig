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
%DEFAULT DEF_LIM 1
%DEFAULT DEF_FOLDS 5
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT '../AUXIL/docsim.macros.def.pig';
IMPORT '../AUXIL/macros.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
set default_parallel 16

raw = getProtosFromHbase('$DEF_SRC'); 
extracted_X = FOREACH raw GENERATE 
		$0 as key,
		pl.edu.icm.coansys.classification.documents.pig.extractors.
			EXTRACT_MAP_WHEN_CATEG_LIM($1,'$DEF_LIM') as data, --		
		(int)(RANDOM()*$DEF_FOLDS) as part;

neigh = filter extracted_X by $1 is not null;
--neigh = SAMPLE neighX 0.01;
STORE neigh into '$DEF_DST'; --key,map,part

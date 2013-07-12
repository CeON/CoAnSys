--
-- (C) 2010-2012 ICM UW. All rights reserved.
--
-- -----------------------------------------------------
-- -----------------------------------------------------
-- register section
-- -----------------------------------------------------
-- -----------------------------------------------------
REGISTER /usr/lib/hbase/lib/zookeeper.jar
REGISTER /usr/lib/hbase/hbase-0.94.6-cdh4.3.0-security.jar 
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
IMPORT 'prepdata.def.pig';
IMPORT '../macros.def.pig';
IMPORT '../01_docsim/docsim.macros.def.pig';
IMPORT '../01_docsim/docsim.sim.cosine.def.pig';
IMPORT '../01_docsim/docsim.vector.tfidf.def.pig';
IMPORT '../02_split/docsplit.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------

raw = getProtosFromHbase($DEF_SRC); 
tfidfed = tfidf(raw);
ds = measureCosineDistane(tfidfed);
dc = chooseValidClasses(raw,5);
ret = prepairDataGiven(dc,ds);

describe ret;

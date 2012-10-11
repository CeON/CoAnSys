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
%DEFAULT DEF_NEIGHT 5
-- -----------------------------------------------------
-- -----------------------------------------------------
-- import section
-- -----------------------------------------------------
-- -----------------------------------------------------
IMPORT '../macros.def.pig';
IMPORT '../01_docsim/docsim.macros.def.pig';
IMPORT '../01_docsim/docsim.sim.cosine.def.pig';
IMPORT '../01_docsim/docsim.vector.tfidf.def.pig';
IMPORT '../02_split/docsplit.def.pig';
IMPORT '../03_prepair_data/prepdata.def.pig';
IMPORT 'cremodel.def.pig';
-- -----------------------------------------------------
-- -----------------------------------------------------
-- code section
-- -----------------------------------------------------
-- -----------------------------------------------------
raw = getProtosFromHbase($DEF_SRC); --IMPORT '../01_docsim/docsim.macros.def.pig';
tfidfed = tfidf(raw); --IMPORT '../01_docsim/docsim.vector.tfidf.def.pig';
ds = measureCosineDistane(tfidfed); --IMPORT '../01_docsim/docsim.sim.cosine.def.pig';
dc = chooseValidClassesPart(raw,5); --IMPORT '../02_split/docsplit.def.pig';
dsp = prepairDataGiven(dc,ds); --IMPORT '../03_prepair_data/prepdata.def.pig';

D =	createModelBuilderInput(dsp,dc,$DEF_NEIGHT);
WX = checkFold(D, 0, $DEF_NEIGHT);
F1 = calcWXF1(WX);
dump F1;
WX = checkFold(D, 1, $DEF_NEIGHT);
F1 = calcWXF1(WX);
dump F1;
WX = checkFold(D, 2, $DEF_NEIGHT);
F1 = calcWXF1(WX);
dump F1;
WX = checkFold(D, 3, $DEF_NEIGHT);
F1 = calcWXF1(WX);
dump F1;
WX = checkFold(D, 4, $DEF_NEIGHT);
F1 = calcWXF1(WX);
dump F1;

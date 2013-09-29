--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2013 ICM-UW
-- 
-- CoAnSys is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.

-- CoAnSys is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU Affero General Public License for more details.
-- 
-- You should have received a copy of the GNU Affero General Public License
-- along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
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

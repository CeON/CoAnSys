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

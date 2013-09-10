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
-------------------------------------------------------
-- register section
-------------------------------------------------------
REGISTER /usr/lib/zookeeper/zookeeper-3.4.3-cdh4.0.1.jar;
REGISTER /usr/lib/hbase/hbase.jar;
REGISTER /usr/lib/hbase/lib/guava-11.0.2.jar;
REGISTER '$commonJarsPath';
-------------------------------------------------------
-- import section
-------------------------------------------------------
IMPORT 'macros.pig';
-------------------------------------------------------
-- business code section
-------------------------------------------------------
docRaw = load_bwndata_metadata_hdfs('$bwndataMetadataInputPath');
docContirib = FOREACH docRaw GENERATE rowkey AS docId, FLATTEN(STRSPLIT(document#'contributorKeys','_')) AS contributorId;
contribPerson = LOAD '$contribPersonDir' AS (contributorId: chararray, personId: chararray);
docContribPersonJoin = JOIN docContirib BY contributorId, contribPerson BY contributorId;

docPerson = FOREACH docContribPersonJoin GENERATE docId, personId;
docPerson2 = get_copy(docPerson);

docPersonJoin = JOIN docPerson BY docId, docPerson2 BY docId;
docPersonPair = FOREACH docPersonJoin GENERATE docPerson::docContirib::docId AS docId, docPerson::contribPerson::personId AS personId1, docPerson2::contribPerson::personId AS personId2;
docPersonPairDiff = FILTER docPersonPair BY personId1 != personId2;

personPairDocsGroup = GROUP docPersonPairDiff BY (personId1, personId2);
personPairCount = FOREACH personPairDocsGroup GENERATE FLATTEN(group), COUNT(docPersonPairDiff) AS count;
STORE personPairCount INTO '$outputDir';

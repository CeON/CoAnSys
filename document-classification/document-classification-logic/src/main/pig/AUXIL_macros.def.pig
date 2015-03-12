--
-- This file is part of CoAnSys project.
-- Copyright (c) 2012-2015 ICM-UW
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
-- macros section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE getProtosFromHbase(tableName,auxil) RETURNS idProtoTab {
	$idProtoTab = LOAD 'hbase://$tableName' 
	  	USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('m:mproto','-loadKey true') 
	  	AS (id:chararray, proto:bytearray);
};

DEFINE getBWBWFromHDFS(localization,method) RETURNS val {
	$val = LOAD '$localization' USING $method('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') as (key:chararray, value:bytearray);
};

DEFINE getLocal(localization,auxil) RETURNS docsimOutput {
	$docsimOutput = LOAD $localization using PigStorage();
};

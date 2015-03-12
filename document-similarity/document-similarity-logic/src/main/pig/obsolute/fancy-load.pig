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

%default sample 0.5
%default parallel 32
%default mapredChildJavaOpts -Xmx8000m

%default inputPath 'full/hbase-dump/mproto-m*'
%default outputPath 'document-similarity-output'
%default commonJarsPath '../oozie/similarity/workflow/lib/*.jar'

REGISTER '$commonJarsPath'

IMPORT 'macros.pig';

doc = load_bwndata_metadata_hdfs('$inputPath', $sample);
doc_raw = foreach doc generate rowkey AS docId, document.title as title, document.keywords AS keywords, document.contributors.name as contributor_names;

store doc_raw into '$outputPath';


/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

%default commonJarPath '/usr/lib/zookeeper/zookeeper.jar'
REGISTER '$commonJarPath';

%default orcid_mapredChildJavaOpts '-Xmx4g'
SET mapred.child.java.opts $orcid_mapredChildJavaOpts
%default orcid_sample 1.0
%default orcid_parallel 40
set default_parallel $orcid_parallel
%default orcid_pool 'default'
SET mapred.fairscheduler.pool $orcid_pool
%default orcid_input 'orcid_input'
%default nrml_input 'nrml_output'
%default output 'nrml_output'

docsX = LOAD '$nrml_input' USING
        pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
        ('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') AS (kD:chararray,vD:bytearray);
docs = distinct docsX;
orcidX = LOAD '$orcid_input' USING
        pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
        ('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable') AS (kO:chararray,vO:bytearray);
orcid = distinct orcidX;
jnd = JOIN docs by kD, orcid by kO;
mtchd = FOREACH jnd GENERATE kD as k, vD as vD, vO as vO;
mrgd = FOREACH mtchd GENERATE FLATTEN(pl.edu.icm.coansys.disambiguation.author.pig.merger.MergeDocumentWithOrcid(k,vD,vO)) as (k:chararray, v:bytearray);
STORE mrgd INTO '$output' USING
        pl.edu.icm.coansys.commons.pig.udf.RichSequenceFileLoader
        ('org.apache.hadoop.io.Text', 'org.apache.hadoop.io.BytesWritable');

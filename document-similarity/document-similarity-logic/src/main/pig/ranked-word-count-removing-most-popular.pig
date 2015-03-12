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

%default jars '*.jar'
%default commonJarsPath 'lib/$jars'
REGISTER '$commonJarsPath'

%default removal_rate 0.99
%default outputPath 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/mhorst/documentssimilarity/chain/working_dir/results'
%default DOC_TERM_ALL '/term/all'
SET default_parallel 40
SET mapred.child.java.opts -Xmx8000m

%default wc '/word-count-ranked';
wc_ranked = load '$outputPath$wc' as (rank:long, term:chararray, count:long, docs{(docId:chararray)});
term_lower_tmp = filter wc_ranked by rank > (double)7600099*$removal_rate;
docs_lower = foreach term_lower_tmp generate FLATTEN(docs);
docs_distinct = distinct docs_lower;
docs_count_tmp = group docs_distinct all;
docs_count = foreach docs_count_tmp generate COUNT(docs_distinct); 
dump docs_count;

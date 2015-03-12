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
%default time '1'
--%default outputPath 'document-similarity-output/$time/'
SET default_parallel 40
SET mapred.child.java.opts -Xmx8000m

%default wc '/word-count-ranked4';
wc_ranked = load '$outputPath$wc' as (rankval:long, count:long, term:chararray,  docs:bag{(docId:chararray)});
wc2 = foreach wc_ranked  generate rankval,count;
wc1 = distinct wc2;
wc = order wc1 by rankval;
--wc1 = filter wc by rankval > 3;
dump wc1;

/***************
term_lower_tmp = filter wc_ranked by rankval < (double)7600099*$removal_rate;
tmp = foreach term_lower_tmp generate rankval, count*count as powed;
tmp2 = order tmp by powed;
dump tmp2;
**************/

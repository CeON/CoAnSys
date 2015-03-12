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



REGISTER ./analyse_oap_duplicates.py USING jython AS judf1;

%DEFAULT pre '0-0_'
%DEFAULT val '955'
%DEFAULT post '-80-1_0-12-40' 
%DEFAULT input '$pre$val$post'

E = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/similarity/normalizedleftdocs' as (k1:chararray,k2:chararray,sim:double);
E1 = foreach E generate k;
E2 = group E1 all;
E3 = foreach E2 generate COUNT(E1);
store E3 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/number_of_pairs' using PigStorage(',');

X = load 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/number_of_pairs' using PigStorage(',');
dump X;


/**********
E22 = order E21 by c desc;
E23 = limit E22 20;
dump E23;
E21 = limit E2 20;
dump E21;

E3 = foreach E2 generate group as k, E1.tfidf as tfidfs;
E4 = foreach E3 generate judf1.sortBagDesc(tfidfs);
store E4 into 'hdfs://hadoop-master.vls.icm.edu.pl:8020/user/pdendek/docsim-on-oap/results_oap_eval/$input/tfidf_vector_csv' using PigStorage(',');
*********/


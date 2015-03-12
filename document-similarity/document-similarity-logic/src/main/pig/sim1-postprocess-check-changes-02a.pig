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

%default parallel 40
%default tmpCompressionCodec gz
%default mapredChildJavaOpts -Xmx12g

%default jars '*.jar'
%default commonJarsPath 'lib/$jars'

REGISTER '$commonJarsPath'

SET default_parallel $parallel
SET mapred.child.java.opts $mapredChildJavaOpts
SET pig.tmpfilecompression true
SET pig.tmpfilecompression.codec $tmpCompressionCodec
%DEFAULT scheduler default
SET mapred.fairscheduler.pool $scheduler
--SET pig.noSplitCombination true;
IMPORT 'macros.pig';

-------------------------------------------------------
-- business code section
-------------------------------------------------------
%default inputPath 'docsim-on-oap/integrated_results/'
%default splitThreshold 0.999

E3 = load '$inputPath/final/diff_on_sims_with_vectors' as (k1, k2, t1, t2, prevSim, newSim, diffSim:double, vO1:{(term,tfidf)}, vN1:{(term,tfidf)},vO2:{(term,tfidf)},vN2:{(term,tfidf)});

F1 = foreach E3 generate COUNT(vO1) as c1, COUNT(vO2) as c2;
F2 = filter F1 by c1==c2;
F3 = group F2 by c1;
F4 = foreach F3 generate group as c, COUNT(F2) as occ;
dump F4;



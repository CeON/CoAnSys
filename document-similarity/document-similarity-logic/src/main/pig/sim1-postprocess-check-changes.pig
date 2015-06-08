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
SET mapreduce.map.java.opts $mapredChildJavaOpts
SET mapreduce.reduce.java.opts $mapredChildJavaOpts
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


-- read data from docsim
A1 = load '$inputPath/major/similarity/normalizedleftdocs' as (k1:chararray,k2:chararray,sim:double);
B1 = filter A1 by sim >= $splitThreshold;
B2 = load '$inputPath/recalc/recalcSims1' as (k1:chararray,k2:chararray,sim:double);

C = join B1 by (k1,k2), B2 by (k1,k2);
C1 = foreach C generate B1::k1 as k1, B1::k2 as k2, B1::sim as prevSim, B2::sim as newSim, (B1::sim - B2::sim) as diffSim;

D = load '$inputPath/major/raw' as (k:chararray,t:chararray,kw:chararray,categs:{(c:chararray)});

E = join C1 by k1, D by k;
describe E;

E1 = foreach E generate k1 as k1,k2 as k2,t as t1,prevSim as prevSim,newSim as newSim,diffSim as diffSim;
describe E1;

E2 = join E1 by k2,D by k;
describe E2;

E3X = foreach E2 generate k1,k2,t1, t as t2, prevSim,newSim,diffSim;
describe E3;

store E3X into '$inputPath/final/diff_on_sims';

E3 = load '$inputPath/final/diff_on_sims' as (k1,k2,t1, t as t2, prevSim,newSim,diffSim:double);

E4A = order E3 by diffSim desc;
E4B = order E3 by diffSim asc;

store E4A into '$inputPath/final/diff_on_sims_desc';
store E4B into '$inputPath/final/diff_on_sims_asc';


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


-- read data from docsim
A1 = load '$inputPath/major/similarity/normalizedleftdocs' as (k1:chararray,k2:chararray,sim:double);
B1x = filter A1 by (sim >= 0.8 and sim < 0.9);
B1 = limit B1x 200;


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

E3 = foreach E2 generate k1,k2,t1, t as t2, prevSim,newSim,diffSim;

F = load '$inputPath/major/tfidf/all-topn-tmp/' as (key, term,tfidf);
F1 = group F by key;
F2 = foreach F1 generate group as key, F.(term,tfidf) as vecOld;

G = load '$inputPath/recalc/vectors' as (key2, vecNew:{(key,term,tfidf)});
G2 = foreach G generate key2, vecNew.(term, tfidf) as vecNew;

H1 = join E3 by k1, F2 by key, G2 by key2;
H2 = foreach H1 generate k1 as k1, k2 as k2, t1 as t1, t2 as t2, prevSim as prevSim, newSim as newSim, diffSim as diffSim, vecOld as vO1, vecNew as vN1;

H3 = join H2 by k2, F2 by key, G2 by key2;
H4 = foreach H3 generate k1 as k1, k2 as k2, t1 as t1, t2 as t2, prevSim as prevSim, newSim as newSim, diffSim as diffSim, vO1 as vO1, vN1 as vN1, vecOld as vO2, vecNew as vN2;

store H4 into '$inputPath/final/diff_on_sims_with_vectors';


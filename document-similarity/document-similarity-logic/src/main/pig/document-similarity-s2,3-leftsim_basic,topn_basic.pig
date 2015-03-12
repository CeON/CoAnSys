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

%default sample 0.1

%default tfidfPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/tfidf-new9/weighted'
%default outputPath 'hdfs://hadoop-master:8020/user/akawa/full/similarity/docsim-11'
%default commonJarsPath 'lib/*.jar'
%default parallel 10
%default tmpCompressionCodec gz

REGISTER '$commonJarsPath';

DEFINE CosineSimilarity pl.edu.icm.coansys.similarity.pig.udf.CosineSimilarity();

-------------------------------------------------------
-- business code section
-------------------------------------------------------
set default_parallel $parallel
set pig.tmpfilecompression true
set pig.tmpfilecompression.codec $tmpCompressionCodec

t = LOAD '$tfidfPath' AS (docId: chararray, term: chararray, tfidf: double);
g1 = GROUP t BY docId;
g2 = FOREACH g1 GENERATE *;

tfidf_cross = FILTER(CROSS g1, g2 parallel $parallel) BY g1::group < g2::group;

-- measure cosine document similarity
similarity = FOREACH tfidf_cross {
		A = ORDER g1::t BY term;
		B = ORDER g2::t BY term;
		GENERATE CosineSimilarity(g1::group, A, g2::group, B) AS cosineTuple;
};

-- flatten cosine document similarity
sim = FOREACH similarity GENERATE FLATTEN(cosineTuple) AS (docId1, docId2, similarity);
sim2 = FOREACH sim GENERATE docId2 AS docId1, docId1 AS docId2, similarity;
sim_union = UNION sim, sim2;
sim_group = GROUP sim_union BY docId1;

--measure cosine document similarity
STORE sim_group INTO '$outputPath';

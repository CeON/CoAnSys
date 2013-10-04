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
-- -----------------------------------------------------
-- -----------------------------------------------------
-- macros section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE tfidf(data) RETURNS F{
	-- further processing: 
	-- * concatenate ti, abs and kw.
	-- * lowercase them, remove diacritics, 
	-- * remove non-alphanumerical data, remove stopwords, 
	-- * emit key-value pair, K:docId, V:stemmed(word)
	A1 = FOREACH $data GENERATE 
			FLATTEN(pl.edu.icm.coansys.classification.
			documents.pig.proceeders.STEMMED_PAIRS(key,data#'title',data#'abstract',data#'keywords')) 
			as (key:chararray,word:chararray);
	-- word count
	A2 = group A1 by (word, key);
	A = foreach A2 generate FLATTEN(group), COUNT(A1) as wc;
	-- doc word count
	B1 = group A by key;
	B = foreach B1 generate FLATTEN(A), SUM(A.wc) as wid;
	-- docs count
	C1 = group B by key;
	C2 = group C1 all;
	C = foreach C2 generate flatten(C1), COUNT(C1) as alldocs;
	D = foreach C generate flatten(B), alldocs;
	-- doc per word
	E1 = group D by word;
	E = foreach E1 generate flatten(D), COUNT(D) as docperword;
	-- tfidf
	$F = foreach E generate 
				flatten(pl.edu.icm.coansys.classification.
				documents.pig.proceeders.TFIDF(*)) 
				as (key:chararray, word:chararray, tfidf:double);
};


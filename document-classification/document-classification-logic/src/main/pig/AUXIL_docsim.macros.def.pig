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
DEFINE triangleMatrix(F) RETURNS CroZ{
	G = group $F by key;
	H = foreach G generate *;
	$CroZ = filter(cross G, H) by G::group < H::group;
};

DEFINE withoutDiagonal(F) RETURNS CroZ{
	G = group $F by key;
	H = foreach G generate *;
	$CroZ = filter(cross G, H) by G::group != H::group;
};
/*
{cosined::keyA: chararray,cosined::keyB: chararray, cosined::sim: double,
macro_enhanceWithClassif_extracted_X_0::key: chararray,
macro_enhanceWithClassif_extracted_X_0::categs: {()},
macro_enhanceWithClassif_extracted_X_0::categocc: int}
*/
/*
DEFINE enhanceWithClassif(cosined,raw,limnum) RETURNS enhanced{
	extracted_X = FOREACH $raw GENERATE 
		flatten(pl.edu.icm.coansys.classification.
		documents.pig.extractors.EXTRACT_KEY_CATEG_WHEN_LIM(*, $limnum))
		as (key:chararray, categs:bag{}, categocc:int);
	extracted = filter extracted_X by $0 is not null;
	enhancedA_X = join cosined by keyA, extracted by key;
	enhancedA = foreach enhancedA_X generate keyA, keyB, sim, categs as categsA, categocc as categoccA;
	enhancedB = join enhancedA by keyB, extracted by key;
	$enhanced = foreach enhancedB generate keyA, keyB, sim, categsA, categoccA, categs as categsB, categocc as categoccB;
};
*/

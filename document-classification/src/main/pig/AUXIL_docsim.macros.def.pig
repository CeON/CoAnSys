--
-- (C) 2010-2012 ICM UW. All rights reserved.
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

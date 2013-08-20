--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

-- -----------------------------------------------------
-- -----------------------------------------------------
-- macros section
-- -----------------------------------------------------
-- -----------------------------------------------------
DEFINE cosine(CroZ) RETURNS J{
	I = foreach $CroZ generate 
		flatten(pl.edu.icm.coansys.classification.
		documents.pig.proceeders.DOCSIM(*)) 
		as (keyA:chararray, keyB:chararray, sim:double);
	$J = filter I by keyA is not null;
--	$K = group J by keyA;
};

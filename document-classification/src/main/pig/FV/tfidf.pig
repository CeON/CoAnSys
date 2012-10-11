--
-- (C) 2010-2012 ICM UW. All rights reserved.
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


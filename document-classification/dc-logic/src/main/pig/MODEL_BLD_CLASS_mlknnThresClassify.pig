--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

DEFINE mlknnThresClassify(model,data) RETURNS ret{
	posX = filter $data by categB == categQ;  -- keyA, categB, categQ; 
	posXGR = group posX by (keyA,categB);
	pos = foreach posXGR generate group.keyA as key,group.categB as categ, COUNT(posX) as occ;

	C1 = join pos by categ, $model by categ; --key, categ, occ, categ, thres, f1;
	C2 = filter C1 by thres<=occ;
	C3 = group C2 by key;

	$ret = foreach C3 generate group as key, C2.M::categ as categs:bag{(categ:chararray)};
};

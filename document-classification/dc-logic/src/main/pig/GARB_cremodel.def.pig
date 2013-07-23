--
-- (C) 2010-2012 ICM UW. All rights reserved.
--

-- -----------------------------------------------------
-- -----------------------------------------------------
-- macros section
-- -----------------------------------------------------
-- -----------------------------------------------------

DEFINE calcWXF1(in) RETURNS F1{
	WXL = group $in all;
	TFPN = foreach WXL generate SUM($in.tp) as tp, SUM($in.tn) as tn, SUM($in.fp) as fp, SUM($in.fn) as fn;
	PR = foreach TFPN generate tp/(double)(tp+fp) as p, tp/(double)(tp+fn) as r;
	$F1 = foreach PR generate 2*p*r/(p+r) as f1;
};

DEFINE calcF1(in) RETURNS F1{
	TFPN = foreach $in generate SUM($in.tp) as tp, SUM($in.tn) as tn, SUM($in.fp) as fp, SUM($in.fn) as fn;
	PR = foreach TFPN generate tp/(tp+fp) as p, tp/(tp+fn) as r;
	$F1 = foreach PR generate 2*p*r/(p+r) as f1;
};

define checkFold(D, fold, DEF_NEIGHT) RETURNS WX{
	Dfold = filter $D by (part >= $fold and part < ($fold+1));
	model = createModel(Dfold,$DEF_NEIGHT);
	T = filter $D by part != 0;
	C = classify(model,T);
	
	CX = group C by key;
	TX = group T by (keyA,keyB);  -- (keyA, keyB), (keyA, keyB, categA, categB, part)
	TX2 = foreach TX{
		newCategA = distinct T.categA;
		newCategB = distinct T.categB;
 		generate group.keyA as keyA, group.keyB as keyB, newCategA as categA, newCategB as categB;
	}

	W = join TX2 by keyA, CX by group; -- keyA, keyB, categA, categB, key, categ;
	W0 = foreach W generate categA, categB, CX::C.categ as categ;
	$WX = foreach W0 generate flatten(pl.edu.icm.coansys.classification.
		documents.pig.proceeders.CHECK_CLASSIF(categA, categB, categ)) 
		as (tp:int,tn:int,fp:int,fn:int);
};

DEFINE classify(model,data) RETURNS ret{
	posX = filter $data by categB == categQ;  -- keyA, keyB, categB, categQ; 
	posXGR = group posX by (keyA,categB);
	pos = foreach posXGR generate group.keyA as key,group.categB as categ, COUNT(posX) as occ;

	classifX = join pos by categ, model by categ; --key, categ, occ, key, thres, f1;
	classifT = filter classifX by thres<=occ;
	$ret = foreach classifT generate key, model::categ;
};

DEFINE createModel(part,DEF_NEIGHT) RETURNS ret{
	split $part into 
		posX if (categA == categQ
				and categB == categQ), -- keyA, keyB, categA, categB, part, categQ; 
		negX if (categA != categQ
				and categB == categQ); -- keyA, keyB, categA, categB, part, categQ;

	pos = calcCategNeighDocs(posX); --(categ),{(categ,count,docscount)}
	neg = calcCategNeighDocs(negX);

	allX6 = join pos by $0,neg by $0; -- (categPOS),POS::{(categ,count,docscount)}, (categNEG),NEG::{(categ,count,docscount)}?
	$ret = foreach allX6 generate FLATTEN(pl.edu.icm.coansys.classification.
		documents.pig.proceeders.THRES_FOR_CATEG(*,$DEF_NEIGHT)) 
		as (categ:chararray, thres:int, f1:double);
};

DEFINE calcCategNeighDocs(tab) RETURNS ret{
	tabX2 = group $tab by (keyA, categA); -- (keyA,categA), {(keyA, keyB, categA, categB, tab, categQ)}; np. 3 sąsiadów ma categB==categA
	tabX3 = foreach tabX2 generate group.categA as categ, COUNT($tab) as neigh; -- categ, 3;
	tabX4 = group tabX3 by (categ,neigh); -- (categ,count),{(categ,count)}; 
	tabX5 = foreach tabX4 generate flatten(group), COUNT(tabX3) as docsocc; -- np kod 12345 wystąpił u 3 sąsiadów w 5 dokumentach
	tabX5A = order tabX5 by neigh asc;
	$ret = group tabX5 by categ; --(categ),{(categ,count,docscount)}
};

DEFINE createModelBuilderInput(ds,dc,DEF_NEIGHT) RETURNS ret{
	A = group $ds by keyA;
	B = foreach A{
		neigh = order $ds by sim desc;
		neighX = limit neigh $DEF_NEIGHT;
		generate flatten(neighX);
	}
	C = foreach B generate keyA, keyB, flatten(categA), flatten(categB), part;

	categX = foreach $dc generate categ;
	categZ = distinct categX;	

	DX = cross C, categZ; -- keyA, keyB, categA, categB, part, categQ;
	$ret = foreach DX generate $0 as keyA, $1 as keyB, $2 as categA, $3 as categB, $4 as part, $5 as categQ;
};

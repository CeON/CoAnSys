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

DEFINE mlknnThresClassify(model,data) RETURNS ret{
	posX = filter $data by categB == categQ;  -- keyA, categB, categQ; 
	posXGR = group posX by (keyA,categB);
	pos = foreach posXGR generate group.keyA as key,group.categB as categ, COUNT(posX) as occ;

	C1 = join pos by categ, $model by categ; --key, categ, occ, categ, thres, f1;
	C2 = filter C1 by thres<=occ;
	C3 = group C2 by key;

	$ret = foreach C3 generate group as key, C2.M::categ as categs:bag{(categ:chararray)};
};

/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToHashCode;

public class DisambiguationExtractor {

	static private PigNormalizer normalizers[] = {
		new ToEnglishLowerCase(), 
		new ToHashCode()
	};

	public static PigNormalizer[] getNormalizers() {
		return normalizers;
	}

	public static void setNormalizers(PigNormalizer[] normalizers) {
		DisambiguationExtractor.normalizers = normalizers;
	}

	static public Object normalizeExtracted( Object extracted ) {
		Object tmp = extracted;
		for ( PigNormalizer pn: normalizers ) {
			tmp = pn.normalize( tmp );
		}
		return tmp;
	}
	
	static public boolean isClassifCode(String str) {
		if (isMSc(str)) {
			return true;
		} else {
			return false;
		}
	}

	static public boolean isMSc(String str) {
		return str.toUpperCase().matches("[0-9][0-9][A-Z][0-9][0-9]");
	}
	
	//to re-implement in each extractor
	public String getId() {
		return null;
	}
}

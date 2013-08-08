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

import org.apache.pig.data.DataBag;

import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;

public abstract class DisambiguationExtractor {
	
	PigNormalizer normalizers[];
	
	public DisambiguationExtractor() {
		normalizers = new PigNormalizer[1];
		normalizers[0] = new ToEnglishLowerCase();
	}
	
	public String normalizeExtracted( String extracted ) {
		String tmp = extracted;
		for ( PigNormalizer pn: normalizers ) {
			tmp = pn.normalize( tmp );
		}
		return tmp;
	}
	
	public abstract DataBag extract( Object o, String lang );

	public DataBag extract( Object o ) {
		return extract( o, null );
	}
}

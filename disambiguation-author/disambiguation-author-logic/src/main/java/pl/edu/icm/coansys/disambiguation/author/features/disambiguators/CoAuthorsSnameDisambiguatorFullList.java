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

package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

/**
 * Disambiguator for contributors with the same sname. It requires placing their
 * own sname on the list of authors.
 * 
 * @author mwos
 * @version 1.0
 */
public class CoAuthorsSnameDisambiguatorFullList extends Disambiguator {

	public CoAuthorsSnameDisambiguatorFullList() {
		super();
	}

	public CoAuthorsSnameDisambiguatorFullList(double weight, double maxVal) {
		super(weight, maxVal);
		if (maxVal == 0) {
			throw new IllegalArgumentException("Max value cannot equal 0.");
		}
	}

	@Override
	public void setMaxVal(double maxVal) {
		if (maxVal == 0) {
			throw new IllegalArgumentException("Max value cannot equal 0.");
		}
		this.maxVal = maxVal;
	}
	
	@Override
	public double calculateAffinity(List<Object> f1, List<Object> f2) {

		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);

		// Because this cotributor is in sum and intersection for sure, but we
		// do not want to take himself as his co-author.
		// Also note that inf * 0 is indeterminate form (what gives NaN).
		int intersection = p.getKey() - 1;
		if (intersection <= 0) {
			return 0;
		}
		
		return (double) intersection / maxVal * weight;
	}
}

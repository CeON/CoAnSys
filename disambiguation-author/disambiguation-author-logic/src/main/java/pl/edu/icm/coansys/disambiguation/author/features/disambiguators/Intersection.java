/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public class Intersection extends Disambiguator {

	public Intersection() {
		super();
	}

	public Intersection(double weight, double maxVal) {
		//maxVal - unused
		super(weight, maxVal);
	}

	@Override
	public double calculateAffinity(List<Object> f1, List<Object> f2) {
		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);
		int intersection = p.getKey();
		
		// Note that inf * 0 is indeterminate form (what gives NaN)
		if ( intersection == 0 ) {
			return 0;
		}
		
		return (double) intersection * weight;
	}

}

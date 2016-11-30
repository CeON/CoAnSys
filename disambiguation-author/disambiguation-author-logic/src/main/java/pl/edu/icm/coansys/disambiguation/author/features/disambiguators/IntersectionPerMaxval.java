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
import java.util.Collection;
import java.util.List;

public class IntersectionPerMaxval extends Disambiguator {

	public IntersectionPerMaxval() {
		super();
	}

	public IntersectionPerMaxval(double weight, double maxVal) {
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
    @SuppressWarnings("unchecked")
     public double calculateAffinitySorted(List<Integer> f1, List<Integer> f2){
         return calculateAffinity((Collection<Object>) (List) f1,(Collection<Object>)(List) f2);
     }
    
	@Override
	public double calculateAffinity(Collection<Object> f1, Collection<Object> f2) {
		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);
		int intersection = p.getKey();
		
		// Note that inf * 0 is indeterminate form (what gives NaN)
		if ( intersection == 0 ) {
			return 0;
		}
		return (double) intersection / maxVal * weight;
	}

}

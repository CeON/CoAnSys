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

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class YearDisambiguator extends Disambiguator {

	public YearDisambiguator() {
		super();
	}
		
	public YearDisambiguator(double weight, double maxVal) {
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
	public double calculateAffinity(Collection<Object> f1, Collection<Object> f2) {
		
		if ( f1.isEmpty() || f2.isEmpty() ) {
			return 0;
		}
		Object first = f1.iterator().next();
		Object second = f2.iterator().next();
		
		if ( first == null || second == null ) {
			return 0;
		}
		
		int a = Integer.parseInt( first.toString() );
		int b = Integer.parseInt( second.toString() );

		int dif = Math.abs( b - a );
		
		double sq = (double) dif / maxVal;
		double func = (-sq * sq + 1.0);

		if ( func <= 0 ) {
			return 0;
		}
		
		return func * weight;
	}

}

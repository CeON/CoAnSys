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

package pl.edu.icm.coansys.disambiguation.features;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A heuristic for assessing whether two objects, described by two lists of
 * feature values, are similar or not.
 * 
 * @author pdendek
 * @author mwos
 * @version 1.1
 * @since 2012-08-07
 */
public class Disambiguator {

	/**
	 * O( f1.size + f2.size )
	 * 
	 * @param f1
	 *            list of feature values associated with the first owner
	 * @param f2
	 *            list of feature values associated with the second owner
	 * @return value reflecting affinity between owners. In the basic
	 *         implementation this value is a size of an intersection between
	 *         feature values' lists.
	 */
	public double calculateAffinity(List<Object> f1, List<Object> f2) {
		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);
		int intersection = p.getKey();
		int sum = p.getValue();
		
		if (sum <= 0) {
			//logger.warn("Negative or zero value of lists sum. Returning 0.");
			// TODO: ? 0 or 1
			return 0;
		}
		if (intersection < 0) {
			//should not get in here, because sum >= intersection
			//logger.warn("Negative value of intersection. Returning 0.");
			return 0;
		}
		
		//return (double) intersection / sum;
		return (double) intersection;
	}

	// O( f1.size() + f2.size() )
	public SimpleEntry<Integer, Integer> intersectionAndSum(
			List<Object> f1, List<Object> f2) {
		
		Set <Object> all = new HashSet<Object>( f1.size() + f2.size() );
		
		all.addAll( f1 );
		int sum = f1.size(), intersection = 0;
		
		for ( Object o : f2 ) {
			if ( all.add( o ) ) {
				sum++;
			} else {
				intersection++;
			}
		}
		return new SimpleEntry<Integer, Integer>(intersection, sum);
	}
}

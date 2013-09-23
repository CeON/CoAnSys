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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
	 * O( n log n ) where n = max f1.size f2.size
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

	// O(n log n), n = max f1.size f2.size
	public SimpleEntry<Integer, Integer> intersectionAndSum(
			List<Object> f1, List<Object> f2) {
		
		//O(n log n)
		List<Integer> a = toSortedHashArrayList(f1);
		List<Integer> b = toSortedHashArrayList(f2);
		int sum = 0, intersection = 0;
		int ia = 0, ib = 0;
		
		//O( min f1.size f2.size )
		while (ia < a.size() && ib < b.size()) {
			if (a.get(ia).equals(b.get(ib))) {
				intersection++;
				ia++;
				ib++;
			} else if (a.get(ia) < b.get(ib)) {
				ia++;
			} else {
				ib++;
			}
			sum++;
		}

		sum += (a.size() - ia) + (b.size() - ib);

		return new SimpleEntry<Integer, Integer>(intersection, sum);
	}

	// O( n log n )
	public List<Integer> toSortedHashArrayList(List<Object> l) {
		List<Integer> res = new ArrayList<Integer>();
		Iterator<Object> it = l.iterator();
		while (it.hasNext()) {
			res.add(it.next().hashCode());
		}
		Collections.sort(res);
		return res;
	}

	/**
	 * 
	 * @return {@link Disambiguator} id.
	 */
	public String getName() {
		return Disambiguator.class.getSimpleName();
	}
}

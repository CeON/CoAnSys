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

import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.features.Disambiguator;

/**
 * Disambiguator for contributors with the same sname. It requires placing their
 * own sname on the list of authors.
 * 
 * @author mwos
 * @version 1.0
 * @since 2012-08-07
 */
public class CoAuthorsSnameDisambiguatorFullList extends Disambiguator {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(CoAuthorsSnameDisambiguatorFullList.class);

	@Override
	public String getName() {
		return CoAuthorsSnameDisambiguatorFullList.class.getSimpleName();
	}

	@Override
	public double calculateAffinity(List<Object> f1, List<Object> f2) {

		SimpleEntry<Integer, Integer> p = intersectionAndSum(f1, f2);

		// because this cotributor is in sum and intersection for sure, but we
		// do not want to take him as his co-author.
		int intersection = p.getKey() - 1;
		int sum = p.getValue() - 1;

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
}

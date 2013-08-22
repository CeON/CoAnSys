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

import pl.edu.icm.coansys.disambiguation.features.Disambiguator;

/**
 * Disambiguator for contributors with the same sname. It requires placing their 
 * own sname on the list of authors.
 * @author mwos
 * @version 1.0
 * @since 2012-08-07
 */
public class CoAuthorsSnameDisambiguatorFullList extends Disambiguator{

	@Override
	public String getName() {
		return CoAuthorsSnameDisambiguatorFullList.class.getSimpleName();
	}
	
	@Override
	public double calculateAffinity( List<Object> f1, List<Object> f2 ) {
		f1.retainAll(f2);
		int size = f1.size();
		return ( size > 0 ) ? (size-1) : 0;
	}
}

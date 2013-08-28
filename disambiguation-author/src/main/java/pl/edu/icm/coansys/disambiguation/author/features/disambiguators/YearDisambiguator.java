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
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class YearDisambiguator extends Disambiguator{

	@Override
	public String getName() {
		return YearDisambiguator.class.getSimpleName();
	}
	
	@Override
	public double calculateAffinity( List<Object> f1, List<Object> f2 ) {
		
		if ( f1.isEmpty() || f2.isEmpty() ) {
			return 0;
		}
		Object first = f1.get(0);
		Object second = f2.get(0);
		
		int a = Integer.parseInt( first.toString() );
		int b = Integer.parseInt( second.toString() );
		int dif = Math.abs( b - a );
		
		//TODO funcion(int x) = something like 1 / Math.abs( b - a );
		
		return ( a == b ) ? 1 : 0; //for now
	}

}

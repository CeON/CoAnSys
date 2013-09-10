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

import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.features.Disambiguator;

/**
 * Disambiguator for contributors with the same sname. It requires placing their 
 * own sname on the list of authors.
 * @author mwos
 * @version 1.0
 * @since 2012-08-07
 */
public class CoAuthorsSnameDisambiguatorFullList extends Disambiguator{

    private static final org.slf4j.Logger logger 
    	= LoggerFactory.getLogger( CoAuthorsSnameDisambiguatorFullList.class );
    
	@Override
	public String getName() {
		return CoAuthorsSnameDisambiguatorFullList.class.getSimpleName();
	}
	
	@Override
	public double calculateAffinity( List<Object> f1, List<Object> f2 ) {
		//(-2) because in both lists there is contributor name for whom we are 
		//calculating similarity
		double sum = f1.size() + f2.size() - 2;
		
		
		f1.retainAll(f2);
		//because this cotributor is in intersection for sure, but we do not want
		//to take him as co-author.
		double intersection = f1.size() - 1;
		
		if ( sum <= 0 ) {
			logger.warn( "Negative or zero value of lists sum." );
			return 0;
		}
		
		double resoult = intersection / sum;
		
		if ( resoult < 0 ) {
			logger.warn( "Negative value of intersection." );
			return 0;
		}
		
		return resoult;
	}
}

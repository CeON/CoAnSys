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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class Disambiguator {

	public abstract double calculateAffinity(Collection<Object> f1, Collection<Object> f2);
	protected double weight = 1;
	protected double maxVal = 1;
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setMaxVal(double maxVal) {
		this.maxVal = maxVal;
	}
	
	public Disambiguator () {}
	
	public Disambiguator( double weight, double maxVal ) {
		this.weight = weight;
		this.maxVal = maxVal;
	}

	// O( f1.size() + f2.size() )
	public SimpleEntry<Integer, Integer> intersectionAndSum(
			Collection<Object> f1, Collection<Object> f2) {
		
		if(f1==null || f2==null){
			int size = 0;
			if(f1!=null){
				size = f1.size();
			}else if(f2!=null){
				size = f2.size();
			}
			return new SimpleEntry<Integer, Integer>(0,size);
		}
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

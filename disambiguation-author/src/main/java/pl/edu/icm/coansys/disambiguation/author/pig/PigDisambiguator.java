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

package pl.edu.icm.coansys.disambiguation.author.pig;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.disambiguation.features.Disambiguator;

/**
 * A heuristic for assessing whether two objects, described by two lists of feature values,
 * are similar or not.    
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class PigDisambiguator extends Disambiguator{
	private Disambiguator d = null;
	
	public PigDisambiguator(Disambiguator d) {
		this.d = d;
	}
	
	public double calculateAffinity(Object f1, Object f2) throws ExecException {
		if(f1 instanceof Tuple && f2 instanceof Tuple ){
			return calculateAffinity((Tuple) f1, (Tuple) f2);
		}else if(f1 instanceof DataBag && f2 instanceof DataBag ){
			return calculateAffinity((DataBag) f1, (DataBag) f2);
		}else if(f1 instanceof String && f2 instanceof String ){
			return calculateAffinity((String) f1, (String) f2);
		}else{
			throw new IllegalArgumentException("data type "+ f1.getClass()+" unsupported in calculateAffinity");
		}
	}
	
	public double calculateAffinity(Tuple f1, Tuple f2) throws ExecException {
		LinkedList<String> fl1 = new LinkedList<String>();
		fl1.add( (String) f1.get(0) );
		LinkedList<String> fl2 = new LinkedList<String>();
		fl2.add( (String) f2.get(0) );
		
		return d.calculateAffinity(fl1, fl2);
	}
	

	public double calculateAffinity(DataBag f1, DataBag f2) {
		return d.calculateAffinity(ToList.execute(f1), ToList.execute(f2));
	}
	
	public double calculateAffinity(String f1, String f2) {
		List <String> fl1 = Arrays.asList( f1.split(" ") );
		List <String> fl2 = Arrays.asList( f2.split(" ") );
		return d.calculateAffinity( fl1, fl2 );
	}
	
	/**
	 * 
	 * @return {@link PigDisambiguator} id.
	 */
    @Override
	public String getName(){
		return d.getName();
	}
}

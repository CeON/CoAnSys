/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.features;

import java.util.List;

/**
 * A heuristic for assessing whether two objects, described by two lists of feature values,
 * are similar or not.    
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public abstract class Disambiguator {
	
	/**
	 * 
	 * @param f1 list of feature values associated with the first owner
	 * @param f2 list of feature values associated with the second owner
	 * @return value reflecting affinity between owners. 
	 * In the basic implementation this value is a size of an intersection between feature values' lists 
	 */
	public double calculateAffinity(List<String> f1, List<String> f2) {
		f1.retainAll(f2);
		return f1.size();
	}
	
	/**
	 * 
	 * @return {@link Disambiguator} id.
	 */
	public abstract String getName();
}

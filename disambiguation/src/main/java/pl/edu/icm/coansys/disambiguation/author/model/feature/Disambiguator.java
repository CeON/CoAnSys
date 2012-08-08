/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature;

import java.util.List;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public abstract class Disambiguator {
	
	public double calculateAffinity(List<String> f1, List<String> f2) {
		f1.retainAll(f2);
		return f1.size();
	}
	
	public abstract String getName();
}

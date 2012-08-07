/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature;

import java.util.List;

/**
 * 
 * @author pdendek
 *
 */
public abstract class Feature {
	
	public double calculateAffinity(List<String> f1, List<String> f2) {
		f1.retainAll(f2);
		return f1.size();
	}
	
	public abstract String getName();
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.features;

import java.util.List;

/**
 * The interface for extracting a list of feature values from an input object.  
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public interface Extractor<Input> {
	/**
	 * 
	 * @param input object to examine.
	 * @param auxil information for filtering input data for specific part, e.g. an email associated with a concrete author.
	 * @return list of feature values.
	 */
	public List<String> extract(Input input, String... auxil);
}

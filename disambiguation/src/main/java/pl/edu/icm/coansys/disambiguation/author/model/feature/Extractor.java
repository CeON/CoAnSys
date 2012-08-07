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
public interface Extractor<Input> {
	public List<String> extract(Input input, String... auxil);
}

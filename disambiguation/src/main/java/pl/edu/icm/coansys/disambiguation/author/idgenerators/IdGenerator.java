/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.idgenerators;

import java.util.List;

/**
 * Method to generate an id string from a list of strings.
 *  
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */

public interface IdGenerator {
	String genetareId(List<String> args);
}

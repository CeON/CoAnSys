/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.idgenerator;

import java.util.List;

/**
 * 
 * @author pdendek
 *
 */

public interface IdGenerator {
	String genetareId(List<String> args);
}

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
	 List<String> extract(Input input, String... auxil);
}

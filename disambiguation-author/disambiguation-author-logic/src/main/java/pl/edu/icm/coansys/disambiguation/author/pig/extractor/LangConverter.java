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

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import com.beust.jcommander.IStringConverter;

/* converter for JCommander */
public class LangConverter implements IStringConverter<String> {
	@Override
	public String convert(String lng) {
		return parseLng( lng );
	}

	public static String parseLng(String lng) {
		if (lng == null || lng.equalsIgnoreCase("all")
				|| lng.equalsIgnoreCase("null") || lng.equals("")) {
			return null;
		}
		return lng;
	}
}
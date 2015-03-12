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
package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;


public class EX_PERSON_COANSYS_ID extends EX_PERSON_ID {

	private static PigNormalizer[] new_normalizers = {};
	
	public EX_PERSON_COANSYS_ID() {
		super(new_normalizers, "coansys/disambiguation-author");
	}

	@Override
	public String getId() {
		return "8.1";
	}
}

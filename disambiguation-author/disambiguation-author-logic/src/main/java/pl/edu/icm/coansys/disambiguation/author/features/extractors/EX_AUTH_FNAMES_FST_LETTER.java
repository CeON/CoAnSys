/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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


import pl.edu.icm.coansys.disambiguation.author.normalizers.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.author.normalizers.FirstLetter;
import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.normalizers.ToHashCode;
import pl.edu.icm.coansys.disambiguation.author.normalizers.ToLowerCase;

public class EX_AUTH_FNAMES_FST_LETTER extends EX_AUTH_FNAMES {
	private static PigNormalizer[] new_normalizers = new PigNormalizer[] {
		new FirstLetter(),
		new DiacriticsRemover(), 
		new ToLowerCase(),
		new ToHashCode()
	};

	public EX_AUTH_FNAMES_FST_LETTER() {
		super(new_normalizers);
	}

	public EX_AUTH_FNAMES_FST_LETTER(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}

	@Override
	public String getId() {
		return "5";
	}
}

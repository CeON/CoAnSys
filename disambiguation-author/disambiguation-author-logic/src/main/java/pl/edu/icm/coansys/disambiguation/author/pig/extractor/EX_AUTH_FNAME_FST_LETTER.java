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

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.pig.normalizers.ToEnglishLowerCase;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_AUTH_FNAME_FST_LETTER extends DisambiguationExtractorAuthor {

	public EX_AUTH_FNAME_FST_LETTER() {
		super();
	}

	public EX_AUTH_FNAME_FST_LETTER(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}

	@Override
	public DataBag extract(Object o, int fakeIndex, String lang) {
		TupleFactory tf = TupleFactory.getInstance();
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		ToEnglishLowerCase TELC = new ToEnglishLowerCase();

		Author a = dm.getBasicMetadata().getAuthor(fakeIndex);
		String fnames = a.getForenames();
		if (fnames.isEmpty()) {
			return db;
		}
		String normalized_fnames = (String) TELC.normalize(fnames);
		if (normalized_fnames == null) {
			return db;
		}
		Tuple t = tf.newTuple();
		String fst_letter = normalized_fnames.substring(0, 1);
		Object normalized_fst_letter = normalizeExtracted(fst_letter);
		t.append(normalized_fst_letter);
		db.add(t);

		return db;
	}

	@Override
	public String getId() {
		return "D";
	}
}

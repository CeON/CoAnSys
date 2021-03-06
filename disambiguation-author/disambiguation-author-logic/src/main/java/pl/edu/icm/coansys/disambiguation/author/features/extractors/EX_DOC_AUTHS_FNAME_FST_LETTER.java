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

import java.util.ArrayList;
import java.util.Collection;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.normalizers.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_DOC_AUTHS_FNAME_FST_LETTER extends DisambiguationExtractorDocument {

	public EX_DOC_AUTHS_FNAME_FST_LETTER() {
		super();
	}

	public EX_DOC_AUTHS_FNAME_FST_LETTER(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}

	@Override
	public Collection<Integer> extract(Object o, String lang) {
		TupleFactory tf = TupleFactory.getInstance();
		DocumentMetadata dm = (DocumentMetadata) o;
		ArrayList<Integer> ret=new ArrayList<Integer>();

		for (Author a : dm.getBasicMetadata().getAuthorList()) {
			if (a == null) {
				continue;
			}
			String fname = a.getForenames();
			if (fname == null || fname.isEmpty()) {
				continue;
			}
			Object normalized_fname = (new DiacriticsRemover())
					.normalize(fname);

			if (normalized_fname == null) {
				continue;
			}

			String str_normalized_fname = (String) normalized_fname;

			String initial = str_normalized_fname.substring(0, 1);
			Tuple t = tf.newTuple();

			Integer normalized = normalizeExtracted(initial);
			if (normalized == null) {
				continue;
			}
            ret.add(normalized);
		}

		return ret;
	}

	@Override
	public String getId() {
		return "C";
	}
}

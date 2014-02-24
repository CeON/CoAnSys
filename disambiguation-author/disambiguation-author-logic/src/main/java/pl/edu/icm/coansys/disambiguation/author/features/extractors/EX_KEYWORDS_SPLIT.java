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

import java.util.HashSet;
import java.util.Set;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;
import pl.edu.icm.coansys.disambiguation.author.normalizers.DiacriticsRemover;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;

public class EX_KEYWORDS_SPLIT extends DisambiguationExtractorDocument {

	public EX_KEYWORDS_SPLIT() {
		super();
	}

	public EX_KEYWORDS_SPLIT(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}

	@Override
	public DataBag extract(Object o, String lang) {

		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		Set<Object> set = new HashSet<Object>();
		DiacriticsRemover DR = new DiacriticsRemover();

		for (KeywordsList k : dm.getKeywordsList()) {
			if (lang != null && !k.getLanguage().equalsIgnoreCase(lang)) {
				continue;
			}

			for (String keyphrase : k.getKeywordsList()) {
				if (keyphrase.isEmpty() || isClassifCode(keyphrase)) {
					continue;
				}
				String normalized_keyphrase = (String) DR
						.normalize(keyphrase);
				if (normalized_keyphrase == null) {
					continue;
				}
				for (String word : normalized_keyphrase.split("[\\W]+")) {
					if (word.isEmpty()) {
						continue;
					}
					Object normalized = normalizeExtracted(word);
					if (normalized != null) {
						set.add(normalized);
					}
				}
			}
		}

		for (Object single : set.toArray()) {
			db.add(TupleFactory.getInstance().newTuple(single));
		}

		return db;
	}

	@Override
	public String getId() {
		return "6";
	}
}

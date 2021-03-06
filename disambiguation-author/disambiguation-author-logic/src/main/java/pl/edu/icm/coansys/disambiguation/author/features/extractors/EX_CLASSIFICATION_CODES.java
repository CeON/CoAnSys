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
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;

public class EX_CLASSIFICATION_CODES extends DisambiguationExtractorDocument {

	public EX_CLASSIFICATION_CODES() {
		super();
	}

	public EX_CLASSIFICATION_CODES(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}

	@Override
	public Collection<Integer> extract(Object o, String lang) {
		DocumentMetadata dm = (DocumentMetadata) o;
		ArrayList<Integer> ret=new ArrayList<Integer>();

		// classification codes:
		for (ClassifCode cc : dm.getBasicMetadata().getClassifCodeList()) {
			for (String s : cc.getValueList()) {
				if ( s != null && !s.isEmpty() ) {
					Integer normalized = normalizeExtracted(s);
					if (normalized != null) {
						ret.add(normalized);
					}
				}
			}
		}

		// classification codes from keywords
		for (KeywordsList k : dm.getKeywordsList()) {
			if (lang == null || k.getLanguage().equalsIgnoreCase(lang)) {
				for (String s : k.getKeywordsList()) {
					if ( s != null && !s.isEmpty() && isClassifCode(s)) {
						Integer normalized = normalizeExtracted(s);
						if (normalized != null) {
							ret.add(normalized);
						}
					}
				}
			}
		}

		return ret;
	}

	@Override
	public String getId() {
		return "1";
	}
}

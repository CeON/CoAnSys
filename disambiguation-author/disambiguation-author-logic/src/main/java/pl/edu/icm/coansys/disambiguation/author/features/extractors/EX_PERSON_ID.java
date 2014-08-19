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

import java.util.List;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorAuthor;
import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;

//Note that we do not use normalization for that one.
public class EX_PERSON_ID extends DisambiguationExtractorAuthor {

	static protected List<String> PERSON_ID_KEY_NAME;// = "pbnPersonId";
	static{
		PERSON_ID_KEY_NAME.add("pbnPersonId");
		PERSON_ID_KEY_NAME.add("orcid");
	}
	
	
	public EX_PERSON_ID() {
		super();
	}

	public EX_PERSON_ID(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}
	
	protected EX_PERSON_ID(PigNormalizer[] new_normalizers, String person_id_key_name) {
		super(new_normalizers);
		PERSON_ID_KEY_NAME.add(person_id_key_name);
	}
	
	@Override
	public DataBag extract(Object o, int fakeIndex, String lang) {
		TupleFactory tf = TupleFactory.getInstance();
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		Tuple t = tf.newTuple();

		Author a = dm.getBasicMetadata().getAuthor(fakeIndex);
		for (KeyValue kv : a.getExtIdList()) {
			if (PERSON_ID_KEY_NAME.contains(kv.getKey())) {
				if ( kv.getValue() == null || kv.getValue().isEmpty() ) {
					continue;
				}
				t.append(kv.getValue());
				db.add(t);
				break;
			}
		}
		return db;
	}

	@Override
	public String getId() {
		return "8";
	}
}

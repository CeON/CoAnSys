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
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_AUTH_SNAMES extends DisambiguationExtractorDocument {
	
	public EX_AUTH_SNAMES() {
		super();
	}

	public EX_AUTH_SNAMES(PigNormalizer[] new_normalizers) {
		super(new_normalizers);
	}

	@Override
	public DataBag extract( Object o, String lang ){
		TupleFactory tf = TupleFactory.getInstance();
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		
		for ( Author a : dm.getBasicMetadata().getAuthorList() ){
			if ( a == null ) {
				continue;
			}
			Tuple t = tf.newTuple();
			String sname = a.getSurname();
			if ( sname == null || sname.isEmpty() ) {
				continue;
			}
			Object normalized = normalizeExtracted( sname );
			if ( normalized == null ) {
				continue;
			}
			t.append( normalized );
			db.add(t);
		}
		
		return db;
	}

	@Override
	public String getId() {
		return "0";
	}
}

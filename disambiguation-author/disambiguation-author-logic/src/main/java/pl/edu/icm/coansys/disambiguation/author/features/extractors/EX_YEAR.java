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

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DisambiguationExtractorDocument;
import pl.edu.icm.coansys.disambiguation.author.normalizers.PigNormalizer;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_YEAR extends DisambiguationExtractorDocument {
	
	public EX_YEAR() {
		super();
	}

	public EX_YEAR(PigNormalizer[] new_normalizers) {
		// Normalizers unused
		super(new_normalizers);
	}

	@Override
	public DataBag extract( Object o, String lang ) {
		DocumentMetadata dm = (DocumentMetadata) o;
		
		DataBag db = new DefaultDataBag();
		String year = dm.getBasicMetadata().getYear();
		if ( year == null || year.isEmpty() ) {
			return db;
		}
		
		int intYear;
		
		try {
			intYear = Integer.parseInt( year );
		} catch( NumberFormatException e ) {
			return db;
		}
		
		Tuple t = TupleFactory.getInstance().newTuple( intYear );
		db.add( t );
		
		return db;
	}

	@Override
	public String getId() {
		return "B";
	}
}

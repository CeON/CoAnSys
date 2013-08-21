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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;

public class EX_PERSON_ID extends DisambiguationExtractorAuthor{
	
	private static final Logger logger = LoggerFactory.getLogger( EX_PERSON_ID.class );
	public static final String PERSON_ID_KEY_NAME = "personPbnId";
	
	@Override
	public DataBag extract( Object o, int fakeIndex, String lang ){
		TupleFactory tf = TupleFactory.getInstance();
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		Tuple t = tf.newTuple();
		
		Author a = dm.getBasicMetadata().getAuthor(fakeIndex);
		
		for(KeyValue kv : a.getExtIdList()){
			if(kv.getKey().equals(PERSON_ID_KEY_NAME)){
				t.append(kv.getValue());
				db.add(t);
				break;
			}
		}
		
		if(t.size()>0) return db;
		logger.info("no person id for the contributor "+a.getDocId()+"#"+a.getPositionNumber());
		t.append(a.getDocId()+"#"+a.getPositionNumber());
		db.add(t);
		return db;
	}
}

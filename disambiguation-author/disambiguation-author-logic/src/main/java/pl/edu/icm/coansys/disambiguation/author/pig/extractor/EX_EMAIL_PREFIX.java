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

public class EX_EMAIL_PREFIX extends DisambiguationExtractorAuthor{
	
	private static final Logger logger = LoggerFactory
			.getLogger(EX_EMAIL_PREFIX.class);
	
	@Override
	public DataBag extract( Object o, int fakeindex, String lang ){
		DataBag retBag = new DefaultDataBag();
		try{
			DocumentMetadata dm = (DocumentMetadata) o;
			Author a = dm.getBasicMetadata().getAuthor(fakeindex);
			String email = a.getEmail();
			
			if(email == null) return retBag;
			email = email.replaceAll("@.+", "");
			if(email.length()>0){
				Tuple t = TupleFactory.getInstance().newTuple();
				t.append(email);
				retBag.add(t);
			}
		}catch(Exception e){
			logger.error("Problem with email extraction",e);
		}
		return retBag;
	}
	

	@Override
	public String getId() {
		return "777";
	}
}

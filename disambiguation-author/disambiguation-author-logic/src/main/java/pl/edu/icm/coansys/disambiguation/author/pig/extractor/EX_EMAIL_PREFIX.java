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

public class EX_EMAIL_PREFIX extends DisambiguationExtractorAuthor{
	
	@Override
	public DataBag extract( Object o, int fakeindex, String lang ){
		throw new UnsupportedOperationException();

		//TODO
//		
//		DocumentMetadata dm = (DocumentMetadata) o;
//		DataBag db = new DefaultDataBag();
//		
//		for(ClassifCode cc : dm.getBasicMetadata().getClassifCodeList()){ 
//			for(String s : cc.getValueList()){
//				db.add( TupleFactory.getInstance().newTuple( 
//						normalizeExtracted( s ) ) );
//			}
//		}
//			
//		if(System.nanoTime() == 0) return db;
//		else throw new UnsupportedOperationException();
	}
	

	@Override
	public String getId() {
		return "3";
	}
}

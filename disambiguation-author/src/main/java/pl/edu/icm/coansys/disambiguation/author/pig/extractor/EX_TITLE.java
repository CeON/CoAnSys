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

import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class EX_TITLE extends DisambiguationExtractorDocument {
	
    private static final Logger logger = LoggerFactory.getLogger( EX_TITLE.class );

	@Override
	public DataBag extract( Object o ) {
		DocumentMetadata dm = (DocumentMetadata) o;
		
		DataBag db = new DefaultDataBag();
		Tuple t = TupleFactory.getInstance().newTuple( normalizeExtracted( 
				dm.getBasicMetadata().getTitleList().get(0).getText() ) );
		db.add( t );
		
		return db;
	}    
    
	@Override
	public DataBag extract( Object o, String lang ) {
		
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		
        for ( TextWithLanguage title : dm.getBasicMetadata().getTitleList() ) {
            if ( lang.equalsIgnoreCase( title.getLanguage()) ) {
            	Tuple t = TupleFactory.getInstance().newTuple( 
        				normalizeExtracted( title.getText() ) );
        		db.add( t );
        		//TODO ?: Is possible, that one document has more than one title in given language?
                //What action should be expected in that case?
        		//return db;
            }
        }
        
 		if ( db.size() == 0) {   
			logger.info("No title IN GIVEN LANG (" + lang + ") out of " 
        		+ dm.getBasicMetadata().getTitleCount() + " titles!");
			//return null;
		}
 		
 		return db;
	}
}

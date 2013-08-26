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
import org.apache.pig.data.TupleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;

public class EX_KEYWORDS extends DisambiguationExtractorDocument  {
	
    private static final Logger logger = LoggerFactory.getLogger( EX_KEYWORDS.class );
	
	@Override
	public DataBag extract( Object o ){
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		
		for ( KeywordsList k : dm.getKeywordsList() ){
			for ( String s : k.getKeywordsList() ){
				if ( !isClassifCode( s ) ) {
					db.add(TupleFactory.getInstance().newTuple(
							normalizeExtracted( s ) ));
				}
			}
		}
			
		return db;		
	}
	
	@Override
	public DataBag extract( Object o, String lang ) {
		
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		
		
		for ( KeywordsList k : dm.getKeywordsList() ){
			if ( k.getLanguage().equalsIgnoreCase( lang ) ) {
				for ( String s : k.getKeywordsList() ){
					if ( !isClassifCode( s ) ) {
						db.add(TupleFactory.getInstance().newTuple(
								normalizeExtracted( s ) ));
					}
				}
				//return db;
			}
		}
        
		if ( db.size() == 0) {
			logger.info("No keywords IN GIVEN LANG (" + lang + ") out of " 
					+ dm.getKeywordsCount() + " keywords!");
			//return null;
		}
		return db;
	}
}

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.TupleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;

public class EX_KEYWORDS extends DisambiguationExtractor  {
	
    private static final Logger logger = LoggerFactory.getLogger( EX_KEYWORDS.class );
	
	@Override
	public DataBag extract( Object o ){
		DocumentMetadata dm = (DocumentMetadata) o;
		DataBag db = new DefaultDataBag();
		
		for ( KeywordsList k : dm.getKeywordsList() ){
			for ( String s : k.getKeywordsList() ){
				db.add(TupleFactory.getInstance().newTuple(s));
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
					db.add(TupleFactory.getInstance().newTuple(s));
				}
				return db;
			}
		}
        
		logger.info("No keywords IN GIVEN LANG (" + lang + ") out of " 
				+ dm.getKeywordsCount() + " keywords!");
		
		return null;
	}
}

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.LoggerFactory;

public class DisambiguationExtractorFactory {


	private static final org.slf4j.Logger logger = LoggerFactory.getLogger( DisambiguationExtractorFactory.class );
	
	private Map<String,String> nameToId;
	private Map<String,String> idToName;
	
	
	public DisambiguationExtractorFactory() throws Exception {
		
		nameToId = new HashMap<String,String>();
		idToName = new IdentityHashMap<String,String>();
		
		String thisPackage = new DisambiguationExtractor().getClass().getPackage().getName();

		Reflections reflections = new Reflections( thisPackage );    
		
		Set<Class<? extends DisambiguationExtractor>> classes 
			= reflections.getSubTypesOf( DisambiguationExtractor.class );
		
		Class<? extends DisambiguationExtractor>[] ar = classes.toArray( new Class[classes.size()] );
		
		String name,eid;
		for ( Class<? extends DisambiguationExtractor> c : ar ) {
			name = c.getSimpleName();
			
			//if this is not extractor
			if ( !name.startsWith("EX_" ) ) {
				continue;
			}
			
			DisambiguationExtractor e =	c.newInstance();
			eid = e.getId();
			
			if ( eid == null ) {
				String m = "Creating extractor: " + name + " with no id value given: " + eid + ".";
				logger.error( m );
				throw new Exception( m );
			}
			
			nameToId.put( name, eid );

			//checking, if every extractors has unique id
			if ( idToName.containsKey( eid ) ) {
				String m = "Some extractors have the same id: " + eid + ": " + name + ", " + idToName.get( eid ) + ".";
				logger.error( m );
				throw new Exception( m );
			}
			idToName.put( eid, name );
		}
	}

	/*
	 * Converting extractor name/id to opposite one.
	 */
	public String convertExtractorName( String feature ) {
		if ( feature.length() == 1 ) {
			return convertToExName( feature );
		} else {
			return convertToExId( feature );
		}
	}

	public String convertToExId( String extractorName ) {
		return nameToId.get( extractorName );
	}

	public String convertToExName( String extractorId ) {
		return idToName.get( extractorId );
	}
	
	
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.author.jobs.DisambiguationJob_Toy;
import pl.edu.icm.coansys.disambiguation.author.model.feature.Extractor;
import pl.edu.icm.coansys.disambiguation.author.model.feature.indicator.AuthorBased;
import pl.edu.icm.coansys.importers.model.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata;

/**
 * 
 * @author pdendek
 *
 */
public class DocumentProto2EmailPrefixExtractor implements Extractor<DocumentMetadata>, AuthorBased {
	
	private static Logger logger = Logger.getLogger(new DisambiguationJob_Toy().getClass());
	
	public List<String> extract(DocumentMetadata input,  String... auxil) {
		String authId = auxil[0];
		DocumentMetadata dm = (DocumentMetadata) input;
		ArrayList<String> ret = new ArrayList<String>();
		
		for(Author a : dm.getAuthorList()){
			if(a.getKey() == authId){
				String email = a.getEmail();
	    		Matcher matcher = Pattern.compile("[^a-zA-Z]_\\.\\-0-9").matcher(email);
	    		if(matcher.find()){
	    			ret.add(email.substring(0, matcher.start()));
	    			return ret;  
	    		}else{
	    			logger.error("Email \""+a.getEmail()+"\" could not be parsed");
	    		}
			}
		}
		return ret;
		
	}
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.author.jobs.DisambiguationJob_Toy;
import pl.edu.icm.coansys.disambiguation.author.model.feature.Extractor;
import pl.edu.icm.coansys.disambiguation.author.model.feature.extractor.indicator.AuthorBased;
import pl.edu.icm.coansys.importers.model.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DocumentProto2EmailExtractor implements Extractor<DocumentMetadata>, AuthorBased{
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(new DisambiguationJob_Toy().getClass());

	@Override
	public List<String> extract(DocumentMetadata input, String... auxil) {
		String authId = auxil[0];
		ArrayList<String> ret = new ArrayList<String>(); 
		
		DocumentMetadata dm = (DocumentMetadata) input;
		for(Author a : dm.getAuthorList()){
			if(a.getKey() == authId){
				ret.add(a.getEmail());
				break;
			}
		}
		return ret;
		
	}
}

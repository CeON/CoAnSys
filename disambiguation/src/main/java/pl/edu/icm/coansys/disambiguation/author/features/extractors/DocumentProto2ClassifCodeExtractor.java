/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.author.features.Extractor;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.AuthorBased;
import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DocumentBased;
import pl.edu.icm.coansys.disambiguation.author.jobs.DisambiguationJob_Toy;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

/**
 * The {@link Extractor} from {@link DocumentProtos.DocumentMetadata} (commit 33ad120f11eb430d450) to a feature value list. 
 * Please read the description of the {@link DocumentBased} interface.   
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DocumentProto2ClassifCodeExtractor implements Extractor<DocumentMetadata>,DocumentBased{

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(new DisambiguationJob_Toy().getClass());
	
	public List<String> extract(DocumentMetadata input, String... auxil) {
		DocumentMetadata dm = (DocumentMetadata) input;
		ArrayList<String> ret = new ArrayList<String>();
		for(ClassifCode cc : dm.getClassifCodeList()){
			ret.add(cc.getValue());
		}
		return ret;
	}
}

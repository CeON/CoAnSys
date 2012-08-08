/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.model.feature.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.author.jobs.DisambiguationJob_Toy;
import pl.edu.icm.coansys.disambiguation.author.model.feature.Extractor;
import pl.edu.icm.coansys.disambiguation.author.model.feature.extractor.indicator.DocumentBased;
import pl.edu.icm.coansys.importers.model.DocumentProtos;
import pl.edu.icm.coansys.importers.model.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.model.DocumentProtos.DocumentMetadata;

/**
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

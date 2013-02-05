/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DocumentBased;
import pl.edu.icm.coansys.disambiguation.author.jobs.hdfs.DisambiguationJob_Toy;
import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;

/**
 * The {@link Extractor} from {@link DocumentProtos.DocumentMetadata} (commit 33ad120f11eb430d450) to a feature value list. 
 * Please read the description of the {@link DocumentBased} interface.
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DocumentProto2KeyWordExtractor implements Extractor<DocumentMetadata>, DocumentBased{

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(new DisambiguationJob_Toy().getClass());

	@Override
	public List<String> extract(DocumentMetadata input, String... auxil) {
		DocumentMetadata dm = (DocumentMetadata) input;
		ArrayList<String> ret = new ArrayList<String>();
		for(TextWithLanguage kw : dm.getKeywordList()){
			ret.addAll(Arrays.asList(kw.getText().split(" ")));
		}
		return ret;
	}
}

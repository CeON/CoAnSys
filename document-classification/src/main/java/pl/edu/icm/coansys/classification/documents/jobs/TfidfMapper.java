/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class TfidfMapper extends Mapper<TextArrayWritable, StringListIntListWritable, Text, StringListIntListWritable> {

	private static Logger logger = LoggerFactory.getLogger(LoggingInDisambiguation.class);
    
    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId + word, V: the number of word occurences in the document + no. of all words in doc
     * (OUT) emit key-value pairs containing K:word, V: docId + number of word occurences in the document + no. of all words in doc  
     */
    protected void map(TextArrayWritable docIdAndWord, StringListIntListWritable wcAndDocWc, Context context) { 
        
    	StringListIntListWritable slilw = new StringListIntListWritable();
    	slilw.addAllInt(wcAndDocWc.getIntList());
    	slilw.addString(docIdAndWord.toStringList().get(0));
    	
    	try {
			context.write(new Text(docIdAndWord.toStringList().get(1)), 
					slilw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
	protected void logAllFeaturesExtractedForOneAuthor(String authId,
			TextTextArrayMapWritable featureName2FeatureValuesMap) {
		logger.debug("MAPPER: output key: " + authId);
		logger.debug("MAPPER: output value: "+featureName2FeatureValuesMap);
	}
}

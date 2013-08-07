/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordPerDocCountMapper extends Mapper<TextArrayWritable, IntWritable, Text, StringListIntListWritable> {

    private static Logger logger = LoggerFactory.getLogger(WordPerDocCountMapper.class);

    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId + word from this
     * document, V: the number of word occurrences (OUT) emit key-value pairs
     * containing K:docId, V: word + its number of occurrences in the document
     */
    protected void map(TextArrayWritable docIdAndWord, IntWritable wc, Context context) {

        StringListIntListWritable slilw = new StringListIntListWritable();
        slilw.addInt(wc.get());
        slilw.addString(docIdAndWord.toStringList().get(1));

        try {
            context.write(new Text(docIdAndWord.toStringList().get(0)),
                    slilw);
        } catch (IOException e) {
            logger.error("Cought exception:", e);
        } catch (InterruptedException e) {
            logger.error("Cought exception:", e);
        }
    }

    protected void logAllFeaturesExtractedForOneAuthor(String authId,
            TextTextArrayMapWritable featureName2FeatureValuesMap) {
        logger.debug("MAPPER: output key: " + authId);
        logger.debug("MAPPER: output value: " + featureName2FeatureValuesMap);
    }
}

/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;

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
public class TfidfMapper extends Mapper<TextArrayWritable, StringListIntListWritable, Text, StringListIntListWritable> {

    private static Logger logger = LoggerFactory.getLogger(TfidfMapper.class);

    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId + word, V: the number of
     * word occurrences in the document + no. of all words in doc (OUT) emit
     * key-value pairs containing K:word, V: docId + number of word occurrences
     * in the document + no. of all words in doc
     */
    protected void map(TextArrayWritable docIdAndWord, StringListIntListWritable wcAndDocWc, Context context) throws IOException, InterruptedException {

        StringListIntListWritable slilw = new StringListIntListWritable();
        slilw.addAllInt(wcAndDocWc.getIntList());
        slilw.addString(docIdAndWord.toStringList().get(0));

        context.write(new Text(docIdAndWord.toStringList().get(1)), slilw);

    }

    protected void logAllFeaturesExtractedForOneAuthor(String authId,
            TextTextArrayMapWritable featureName2FeatureValuesMap) {
        logger.debug("MAPPER: output key: " + authId);
        logger.debug("MAPPER: output value: " + featureName2FeatureValuesMap);
    }
}

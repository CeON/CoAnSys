/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordPerDocCountReducer extends Reducer<Text, StringListIntListWritable, TextArrayWritable, StringListIntListWritable> {

    private static Logger logger = LoggerFactory.getLogger(WordPerDocCountReducer.class);

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
    }

    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId, V: word + its number of
     * occurrences in the document (OUT) emit key-value pairs containing K:docId
     * + word, V: the number of word occurrences in the document + no. of all
     * words in doc
     */
    public void reduce(Text key, Iterable<StringListIntListWritable> values, Context context) {


        int dwc = 0;
        for (StringListIntListWritable slilw : values) {
            dwc += slilw.getIntList().get(0);
        }

        for (StringListIntListWritable slilw : values) {
            StringListIntListWritable out_slilw = new StringListIntListWritable();
            out_slilw.addInt(slilw.getIntList().get(0));
            out_slilw.addInt(dwc);
            try {
                context.write(
                        new TextArrayWritable(new Text[]{key,
                    new Text(slilw.getStringList().get(0))}),
                        out_slilw);
            } catch (IOException e) {
                logger.error("Cought exception:", e);
            } catch (InterruptedException e) {
                logger.error("Cought exception:", e);
            }
        }
    }
}

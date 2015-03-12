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
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class TfidfReducer extends Reducer<Text, StringListIntListWritable, TextArrayWritable, DoubleWritable> {

    private int docs_num = 1;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {

        Configuration conf = context.getConfiguration();
        docs_num = Integer.parseInt(conf.get("DOCS_NUM"));
    }

    @Override
    /**
     * (IN) accepts key-value pairs containing K:word, V: docId + number of word
     * occurrences in the document + no. of all words in doc (OUT) emit
     * key-value pairs containing K:docId + word, V: tfidf
     */
    public void reduce(Text key, Iterable<StringListIntListWritable> values, Context context) throws IOException, InterruptedException {

        int docsWithTerm = 0;
        for (Iterator<StringListIntListWritable> it = values.iterator(); it.hasNext();) {
            docsWithTerm++;
        }

        double idf = Math.log(docs_num / (double) docsWithTerm);

        for (final StringListIntListWritable v : values) {
            double tf = (double) (v.getIntList().get(0)) / (double) (v.getIntList().get(1));
            context.write(new TextArrayWritable(new Text[]{new Text(v.getStringList().get(0)), key}), new DoubleWritable(tf * idf));
        }
    }
}

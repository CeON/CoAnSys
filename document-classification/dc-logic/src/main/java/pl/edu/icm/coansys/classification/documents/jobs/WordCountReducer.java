/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordCountReducer extends Reducer<TextArrayWritable, IntWritable, TextArrayWritable, IntWritable> {

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
    }

    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId + word from this
     * document, V: value 1 (PROCESS) count occurrences of the word (OUT) emits
     * key-value pairs containing K:docId + word from this document, V: the
     * number of word occurrences
     */
    public void reduce(TextArrayWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int wc = 0;
        for (Iterator it = values.iterator(); it.hasNext();) {
            wc++;
        }
        context.write(key, new IntWritable(wc));
       
    }
}

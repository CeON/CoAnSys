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

package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import pl.edu.icm.coansys.commons.java.StopWordsRemover;

public class StopWordFilter extends EvalFunc<Boolean> {

    @Override
    public Boolean exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return false;
        }
        try {
            String word = (String) input.get(0);
            return !StopWordsRemover.isAnEnglishStopWords(word);
                
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

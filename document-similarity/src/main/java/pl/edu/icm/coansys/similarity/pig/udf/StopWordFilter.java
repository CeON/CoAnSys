/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import pl.edu.icm.coansys.similarity.documents.auxil.StopWordsRemover;

public class StopWordFilter extends EvalFunc<Boolean> {

    @Override
    public Boolean exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            String word = (String) input.get(0);
            return !StopWordsRemover.isAnEnglishStopWords(word);
                
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

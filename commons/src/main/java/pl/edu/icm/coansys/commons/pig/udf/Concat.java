/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

/**
 *
 * @author akawa
 */
public class Concat extends EvalFunc<String> {

    private static final int INITIAL_CAPACITY = 1000;

    @Override
    public String exec(Tuple input) throws IOException {
        if (input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(INITIAL_CAPACITY);
        for (Object item : input.getAll()) {
            sb.append(item);
        }
        return sb.toString();
    }
}

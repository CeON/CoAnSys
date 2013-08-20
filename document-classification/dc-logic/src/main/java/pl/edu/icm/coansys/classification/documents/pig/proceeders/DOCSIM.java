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

package pl.edu.icm.coansys.classification.documents.pig.proceeders;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class DOCSIM extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(DOCSIM.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.DOUBLE);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            String keyA = (String) input.get(0);
            HashMap<String, Double> hmA = new HashMap<String, Double>();
            HashSet<String> hsA = new HashSet<String>();
            double denominatorA = 0;
            for (Tuple t : (DataBag) input.get(1)) {
                String word = (String) t.get(1);
                double val = (Double) t.get(2);
                denominatorA += val * val;
                hmA.put(word, val);
                hsA.add(word);
            }


            String keyB = (String) input.get(2);
            HashMap<String, Double> hmB = new HashMap<String, Double>();
            HashSet<String> hsB = new HashSet<String>();
            double denominatorB = 0;
            for (Tuple t : (DataBag) input.get(3)) {
                String word = (String) t.get(1);
                double val = (Double) t.get(2);
                denominatorB += val * val;
                hmB.put(word, val);
                hsB.add(word);
            }

            hsA.retainAll(hsB);

            double numerator = 0;

            for (String s : hsA) {
                numerator += hmA.get(s) * hmB.get(s);
            }

            double denominator = Math.sqrt(denominatorA) * Math.sqrt(denominatorB);
            double retVal = numerator / denominator;

            if (retVal > 0) {
                Object[] to = new Object[]{keyA, keyB, retVal};
                return TupleFactory.getInstance().newTuple(Arrays.asList(to));
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

public class CosineSimilarity extends EvalFunc<Tuple> {

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.DOUBLE);
        } catch (FrontendException e) {
            throw new IllegalStateException(e);
        }
    }

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
            throw new IOException("Caught exception processing input row ", e);
        }
    }

    private Map<String, Double> extractToMap(Tuple input, int bagIndex, int keyIndex, int valIndex) throws ExecException {
        HashMap<String, Double> hm = new HashMap<String, Double>();
        for (Tuple t : (DataBag) input.get(bagIndex)) {
            hm.put((String) t.get(keyIndex), (Double) t.get(valIndex));
        }
        return hm;
    }
}

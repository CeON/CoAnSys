/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.proceeders;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class POS_NEG extends EvalFunc<Tuple> {

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.INTEGER, DataType.INTEGER);
        } catch (FrontendException e) {
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
            String keyB = (String) input.get(1);
            DataBag categsA = (DataBag) input.get(2);
            DataBag categsB = (DataBag) input.get(3);
            String categQ = (String) input.get(4);

            if (keyA == null || keyB == null || categsA == null || categsB == null || categQ == null) {
                return null;
            }

            boolean inA = false;
            boolean inB = false;

            for (Tuple t : categsA) {
                if (categQ.equals((String) (t.get(0)))) {
                    inA = true;
                    break;
                }
            }

            for (Tuple t : categsB) {
                if (categQ.equals((String) (t.get(0)))) {
                    inB = true;
                    break;
                }
            }

            if (!inB) {
                Object[] to = new Object[]{keyA, categQ, 0, 0};
                return TupleFactory.getInstance().newTuple(Arrays.asList(to));
            } else {
                Object[] to = new Object[]{keyA, categQ, inA ? 1 : 0, (!inA) ? 1 : 0};
                return TupleFactory.getInstance().newTuple(Arrays.asList(to));
            }

        } catch (Exception e) {
            throw new IOException("Caught exception processing input row:\t"
                    + StackTraceExtractor.getStackTrace(e).replace("\n", "\t"));
        }
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.proceeders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class CATEGOCC extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(CATEGOCC.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.INTEGER, DataType.INTEGER);
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

            Tuple group = (Tuple) input.get(0);

            boolean noCategList = false;
            boolean posCateg = false;
            boolean negCateg = false;
            int numberOfCategs = 0;

            for (Tuple outerTuple : (DataBag) input.get(1)) {
                String q = (String) outerTuple.get(0);
                if (noCategList) {
                    for (Tuple t : (DataBag) outerTuple.get(4)) {
                        String categ = (String) t.get(0);
                        if (!(posCateg || negCateg) && categ.equals(q)) {
                            posCateg = true;
                        }
                    }
                    if (!posCateg) {
                        negCateg = true;
                    }
                    noCategList = false;
                }

                for (Tuple t : (DataBag) outerTuple.get(4)) {
                    if (((String) t.get(0)).equals(q)) {
                        numberOfCategs++;
                    }
                }
            }

            if (posCateg == false && negCateg == false) {
                return null;
            }

            Object[] to = new Object[]{group.get(0), posCateg ? numberOfCategs : 0, negCateg ? numberOfCategs : 0};
            return TupleFactory.getInstance().newTuple(Arrays.asList(to));

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

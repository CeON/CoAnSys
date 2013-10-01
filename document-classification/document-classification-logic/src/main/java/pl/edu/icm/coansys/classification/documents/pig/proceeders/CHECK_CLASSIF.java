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
public class CHECK_CLASSIF extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(CHECK_CLASSIF.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER);
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
            DataBag categReal = (DataBag) input.get(0);
            DataBag categNeigh = (DataBag) input.get(1);
            DataBag categClassif = (DataBag) input.get(2);

            List<String> T = new ArrayList<String>();
            List<String> P = new ArrayList<String>();
            List<String> toChoose = new ArrayList<String>();

            List<String> F = null;
            List<String> N = null;
            int tp, tn, fp, fn;

            for (Tuple t : categReal) {
                T.add((String) t.get(0));
            }
            for (Tuple t : categNeigh) {
                toChoose.add((String) t.get(0));
            }
            for (Tuple t : categClassif) {
                P.add((String) t.get(0));
            }

            F = cloneArrayList(toChoose);
            F.removeAll(T);
            N = cloneArrayList(toChoose);
            N.removeAll(P);

            tp = intersectSize(T, P);
            fp = intersectSize(F, P);
            tn = intersectSize(T, N);
            fn = intersectSize(F, N);

            Object[] to = new Object[]{tp, tn, fp, fn};
            return TupleFactory.getInstance().newTuple(Arrays.asList(to));

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }

    private int intersectSize(List<String> X, List<String> Y) {
        List<String> local = cloneArrayList(X);
        local.retainAll(Y);
        return local.size();
    }

    private List<String> cloneArrayList(List<String> in) {
        List<String> ret = new ArrayList<String>();
        for (String s : in) {
            ret.add(s);
        }
        return ret;
    }
}

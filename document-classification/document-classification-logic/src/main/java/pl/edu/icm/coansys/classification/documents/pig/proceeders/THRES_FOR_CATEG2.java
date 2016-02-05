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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class THRES_FOR_CATEG2 extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(THRES_FOR_CATEG2.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.INTEGER, DataType.DOUBLE);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }

    //C1: {categ: chararray,count: long,occ_pos: long,occ_neg: long}
    @Override
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            Integer num = (Integer) input.get(0);
            String categ = (String) input.get(1);
            DataBag db = (DataBag) input.get(2);

            long[] pos = new long[num + 1];//no of neighbours +1 for 0 count
            long[] neg = new long[num + 1];
            Arrays.fill(pos, 0);
            Arrays.fill(neg, 0);
            for (Tuple t : db) {
                int i = (Integer) t.get(1);
                long i1 = (Long) t.get(2);
                long i2 = (Long) t.get(3);

                pos[i] = i1;
                neg[i] = i2;
            }

            int thres = -1;
            double bestF1 = 0;

            for (int i = 1; i < num; i++) {
                int TP = countLess(i, pos);
                int FP = countLess(i, neg);
                int FN = countEqMore(i, neg);
                double F1 = countF1(TP, FP, FN);
                if (F1 > bestF1) {
                    thres = i;
                    bestF1 = F1;
                }
            }
            logger.info("Calculated the best threshold");
            if (thres != -1) {
                Object[] to = new Object[]{categ, thres, bestF1};
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

    private double countF1(int tp, int fp, int fn) {
        int denominator = Math.max(0, 2 * Math.max(0, tp) + Math.max(0, fn) + Math.max(0, fp));
        return denominator != 0 ? (double) (2 * tp) / (double) denominator : 0;
    }

    private int countEqMore(int curr, long[] posc) {
        int ret = 0;
        for (int i = curr; i < posc.length; i++) {
            ret += posc[i];
        }
        return ret;
    }

    private int countLess(int curr, long[] posc) {
        int ret = 0;
        for (int i = 0; i < curr; i++) {
            ret += posc[i];
        }
        return ret;
    }
}

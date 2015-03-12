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
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class THRES_FOR_CATEG extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(THRES_FOR_CATEG.class);

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

    @Override
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            Object o0 = input.get(0);
            if (o0 == null) {
                return null;
            }
            String categA = (String) input.get(0); //categPOS
            Object o1 = input.get(1);
            DataBag pos = o1 == null ? new DefaultDataBag() : (DataBag) input.get(1);

            Object o2 = input.get(2);
            if (o2 == null) {
                return null;
            }
            String categB = (String) input.get(2); //categPOS
            Object o3 = input.get(3);
            DataBag neg = o3 == null ? new DefaultDataBag() : (DataBag) input.get(3);

            //num of neighbours (1,2,3,...) + "null" neigh cell
            Integer neight_max = Integer.parseInt(input.get(4).toString()) + 1;

            String categ = "".equals(categA) ? categB : categA;

            int[] posc = new int[neight_max];
            int[] negc = new int[neight_max];
            Arrays.fill(posc, 0);
            Arrays.fill(negc, 0);

            logger.info("Start");
            for (Tuple t : pos) {
                long neigh = (Long) t.get(1);
                long dococc = (Long) t.get(2);
                posc[(int) neigh] = (int) dococc;
            }
            logger.info("Constructed pos array");
            for (Tuple t : neg) {
                long neigh = (Long) t.get(1);
                long dococc = (Long) t.get(2);
                negc[(int) neigh] = (int) dococc;
            }
            logger.info("Constructed neg array");
            int thres = -1;
            double bestF1 = 0;


            for (int i = 1; i < neight_max; i++) {
                int TP = countLess(i, posc);
                int FP = countLess(i, negc);
                int FN = countEqMore(i, negc);
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

    private int countEqMore(int curr, int[] posc) {
        int ret = 0;
        for (int i = curr; i < posc.length; i++) {
            ret += posc[i];
        }
        return ret;
    }

    private int countLess(int curr, int[] posc) {
        int ret = 0;
        for (int i = 0; i < curr; i++) {
            ret += posc[i];
        }
        return ret;
    }
}

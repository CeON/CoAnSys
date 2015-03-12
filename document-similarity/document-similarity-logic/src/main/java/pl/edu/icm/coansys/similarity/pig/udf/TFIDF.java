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

package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.Tuple;

public class TFIDF extends EvalFunc<Double> {

    private static final String WEIGTHED = "weighted";
    private static final String NORMAL = "normal";
    private String type = null;

    public TFIDF() {
        type = NORMAL;
    }

    public TFIDF(String type) {
        this.type = type;
    }

    @Override
    public Double exec(Tuple input) throws IOException {

        if (type.equals(NORMAL)) {
            return getTFIDF(input);
        } else if (type.equals(WEIGTHED)) {
            return getWeightedTFIDF(input);
        }

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private Double getTFIDF(Tuple tuple) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Double getWeightedTFIDF(Tuple tuple) throws ExecException {

        double keywordTfidfWeight = getZeroIfNull((Double) tuple.get(0));
        double keywordTfidf = getZeroIfNull((Double) tuple.get(1));
        double titleTfidfWeight = getZeroIfNull((Double) tuple.get(2));
        double titleTfidf = getZeroIfNull((Double) tuple.get(3));
        double abstractTfidfWeight = getZeroIfNull((Double) tuple.get(4));
        double abstractTfidf = getZeroIfNull((Double) tuple.get(5));

        return (keywordTfidf * keywordTfidfWeight) + (titleTfidf * titleTfidfWeight) + (abstractTfidf * abstractTfidfWeight);
    }
    
    private double getZeroIfNull(Double d) {
        return d == null ? 0d : d;
    }
}

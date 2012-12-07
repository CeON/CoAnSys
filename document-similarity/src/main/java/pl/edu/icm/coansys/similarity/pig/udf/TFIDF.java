/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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

        throw new RuntimeException("Unsupported type: " + type);
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

        double weightedTFIDF = (keywordTfidf * keywordTfidfWeight) + (titleTfidf * titleTfidfWeight) + (abstractTfidf * abstractTfidfWeight);
        return weightedTFIDF;
    }
    
    private double getZeroIfNull(Double d) {
        return (d == null ? 0d : d);
    }
}

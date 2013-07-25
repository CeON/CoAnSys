/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.clustering.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class SingleLinkageHACStrategy_OnlyMax extends SingleLinkageHACStrategy {

    private static final Logger log = LoggerFactory.getLogger(SingleLinkageHACStrategy_OnlyMax.class);

    public static void main(String[] args) {
        double[][] in = {{}, {15}, {-46, -3}, {-2, -18, -20}, {-100, -100, -3, -200}};
        int[] out = new SingleLinkageHACStrategy_OnlyMax().clusterize(in);
        StringBuilder sb = new StringBuilder("");
        for (int i : out) {
            sb.append(i).append("\t");
        }
        sb.append("\n");
        log.info(sb.toString());
    }

    @Override
    protected double SIM(double a, double b) {
        return Math.max(a, b);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new SingleLinkageHACStrategy_OnlyMax();
    }
}
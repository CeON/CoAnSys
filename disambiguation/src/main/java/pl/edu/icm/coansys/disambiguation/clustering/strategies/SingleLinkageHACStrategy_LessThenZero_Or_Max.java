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

package pl.edu.icm.coansys.disambiguation.clustering.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class SingleLinkageHACStrategy_LessThenZero_Or_Max extends SingleLinkageHACStrategy implements Cloneable {

    private static final Logger log = LoggerFactory.getLogger(SingleLinkageHACStrategy_LessThenZero_Or_Max.class);

    public static void main(String[] args) {
        double[][] in = {{}, {15}, {46, 3}, {2, -18, -20}, {-100, -100, 3, -200}};
        int[] out = new SingleLinkageHACStrategy_LessThenZero_Or_Max().clusterize(in);
        StringBuilder sb = new StringBuilder("");
        for (int i : out) {
            sb.append(i).append("\t");
        }
        sb.append("\n");
        log.info(sb.toString());
    }

    @Override
    protected double SIM(double a, double b) {
        return minMax(a, b);
    }

    protected double minMax(double a, double b) {
        if (a < 0 || b < 0) {
            return Math.min(a, b);
        } else {
            return Math.max(a, b);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new SingleLinkageHACStrategy_LessThenZero_Or_Max();
    }
}
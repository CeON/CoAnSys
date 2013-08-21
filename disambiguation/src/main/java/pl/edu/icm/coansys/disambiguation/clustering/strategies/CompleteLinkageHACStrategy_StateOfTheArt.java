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
 * @author pdendek
 * version 1.0, since 2012-08-07
 *
 * version 1.1, since 2013-08-20
 * changes: corrections in clustering element matrix and priority queues handling. 
 */
public class CompleteLinkageHACStrategy_StateOfTheArt extends CompleteLinkageHACStrategy implements Cloneable {

    private static final Logger log = LoggerFactory.getLogger(CompleteLinkageHACStrategy_StateOfTheArt.class);

    public static void main(String[] args) throws Exception {
        float[][] in = {{}, {15}, {-46, -3}, {-2, -18, -20}, {-100, -100, -3, -200}};
        int[] out = new CompleteLinkageHACStrategy_StateOfTheArt().clusterize(in);
        StringBuilder sb = new StringBuilder("");
        for (int i : out) {
            sb.append(i).append("\t");
        }
        sb.append("\n");
        log.info(sb.toString());
        //1	3	4	4	4
    }

    @Override
    protected float SIM(float a, float b) {
        return Math.min(a, b);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new CompleteLinkageHACStrategy_StateOfTheArt();
    }
}
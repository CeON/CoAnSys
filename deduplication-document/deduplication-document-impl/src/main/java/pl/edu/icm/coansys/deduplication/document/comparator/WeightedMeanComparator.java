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
package pl.edu.icm.coansys.deduplication.document.comparator;

import java.util.List;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 *
 */
public class WeightedMeanComparator extends AbstractWorkComparator {
    
    @Override
    protected boolean calculateResult(List<Float> probabilities, List<Float> weights) {
        double weightsSum = 0.0;
        double probabilitiesSum = 0.0;
        
        int count = probabilities.size();
        if (count != weights.size()) {
            throw new IllegalArgumentException("Number of probabilities " + count + " doesn't match number of weights " + weights.size());
        }
        
        for (int i = 0; i < count; i++) {
            probabilitiesSum += probabilities.get(i);
            weightsSum += weights.get(i);
        }
        return (weightsSum > 0) && (probabilitiesSum / weightsSum > 0.5);
    }
}

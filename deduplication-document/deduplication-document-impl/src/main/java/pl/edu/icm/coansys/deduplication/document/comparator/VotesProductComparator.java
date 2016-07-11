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
package pl.edu.icm.coansys.deduplication.document.comparator;

import java.util.List;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 *
 */
public class VotesProductComparator extends AbstractWorkComparator {

    private float minVotersWeightRequired;
    private float probabilityTreshold;
    private float tresholdIncreasingVotersRequired;

    
    @Override
    protected boolean calculateResult(List<Float> probabilities, List<Float> weights, StringBuilder debugOutputBuilder) {
        double localVotersWeightRequired = minVotersWeightRequired;
        
        double probabilitiesProduct = 1.0;
        for (int i = 0; i < probabilities.size(); i++) {
            probabilitiesProduct *= probabilities.get(i);
        }
        double weightsSum=0.0;
        for (float f:weights) {
            weightsSum+=f;
        }
        
        if (probabilitiesProduct <= tresholdIncreasingVotersRequired) {
            localVotersWeightRequired+=0.5;
        }
        debugOutputBuilder.append("##PROBABILITIES_PRODUCT=").append(probabilitiesProduct);
        return weightsSum >= localVotersWeightRequired && probabilitiesProduct > probabilityTreshold;
    }

    //******************** SETTERS ********************
    public void setMinVotersWeightRequired(float minVotersRequired) {
        this.minVotersWeightRequired = minVotersRequired;
    }

    public void setProbabilityTreshold(float probabilityTreshold) {
        this.probabilityTreshold = probabilityTreshold;
    }

    public void setTresholdIncreasingVotersRequired(float tresholdIncreasingVotersRequired) {
        this.tresholdIncreasingVotersRequired = tresholdIncreasingVotersRequired;
    }
}

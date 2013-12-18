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
public class VotesProductComparator extends AbstractWorkComparator {

    private int minVotersRequired;
    private float probabilityTreshold;

    
    @Override
    protected boolean calculateResult(List<Float> probabilities, List<Float> weights) {
        if (probabilities.size() < minVotersRequired) {
            return false;
        }
        
        double probabilitiesProduct = 1.0;
        for (int i = 0; i < probabilities.size(); i++) {
            probabilitiesProduct *= probabilities.get(i);
        }
        return probabilitiesProduct > probabilityTreshold;
    }

    //******************** SETTERS ********************
    public void setMinVotersRequired(int minVotersRequired) {
        this.minVotersRequired = minVotersRequired;
    }

    public void setProbabilityTreshold(float probabilityTreshold) {
        this.probabilityTreshold = probabilityTreshold;
    }

}

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

package pl.edu.icm.coansys.disambiguation.author.features.disambiguators;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author dtkaczyk, pdendek
 */
@SuppressWarnings("boxing")
public class CosineSimilarity extends Disambiguator {

	public CosineSimilarity() {
		super();
	}

	public CosineSimilarity(double weight, double maxVal) {
		//maxVal - unused
		super(weight, maxVal);
	}

	@Override
	public double calculateAffinity(Collection<Object> f1, Collection<Object> f2) {
        Map<Object, Integer> v1 = calculateVector(f1);
        Map<Object, Integer> v2 = calculateVector(f2);
        if(v1.size()==0 && v2.size()==0){
        	return 0;
        }
        double cossim = dotProduct(v1, v2) / (vectorLength(v1) * vectorLength(v2));
        // Note that inf * 0 is indeterminate form (what gives NaN)
        if ( cossim == 0 ) {
        	return 0;
        }
        return cossim * weight;
    }

    private Map<Object, Integer> calculateVector(Collection<Object> tokens) {
        HashMap<Object, Integer> vector = new HashMap<Object, Integer>();
        if(tokens==null){
        	return vector;
        }
        for (Object token : tokens) {
        	//TODO that could be done faster (without one of the already three cost operation on map)
        	// Moreover we are iterating through this later, array of pairs would be better (one sort only at the beginning)
        	// Yeees.. Hash map map has const operation, but first of const with (only) high probability, and second - with pretty big 
        	// const as for such small amount of data we have here.
            if (vector.containsKey(token)) {
                vector.put(token, vector.get(token) + 1);
            } else {
                vector.put(token, 1);
            }
        }
        return vector;
    }

    private double vectorLength(Map<Object, Integer> vector) {
        double ret = 0.0;
        for (Entry<Object, Integer> entry : vector.entrySet()) {
            ret += entry.getValue() * entry.getValue();
        }
        return Math.sqrt(ret);
    }

	private double dotProduct(Map<Object, Integer> vector1, Map<Object, Integer> vector2) {
        double ret = 0.0;
        for (Entry<Object, Integer> entry : vector1.entrySet()) {
            // same story - could be done with smaller number of cost operation on map
        	if (vector2.containsKey(entry.getKey())) {
                ret += entry.getValue() * vector2.get(entry.getKey());
            }
        }
        return ret;
    }	
}

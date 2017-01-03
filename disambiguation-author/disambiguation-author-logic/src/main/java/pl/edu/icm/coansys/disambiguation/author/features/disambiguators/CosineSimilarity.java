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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import pl.edu.icm.coansys.commons.java.Pair;

/**
 * @author dtkaczyk, pdendek
 */
@SuppressWarnings("boxing")
public class CosineSimilarity extends Disambiguator {

    public static class CosineSimilarityList {
        List<Pair<Integer, Integer>> counts;
        double length;
        List<Integer> origList;
        public CosineSimilarityList(List<Integer> li){
            origList=li;
            counts=calculateCounts(li);
            length=vectorLength(counts);
        }

        public List<Integer> getOrigList() {
            return origList;
        }
        
    }
    
	public CosineSimilarity() {
		super();
	}

	public CosineSimilarity(double weight, double maxVal) {
		//maxVal - unused
		super(weight, maxVal);
	}

    
    @Override
    @SuppressWarnings("unchecked")
     public double calculateAffinitySorted(List<Integer> f1, List<Integer> f2){
          List<Pair<Integer, Integer>> v1 = calculateCounts(f1);
       List<Pair<Integer, Integer>> v2 = calculateCounts(f2);
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
     public double calculateAffinitySorted(CosineSimilarityList f1, CosineSimilarityList f2){
        
        if(f1.counts.size()==0 && f1.counts.size()==0){
        	return 0;
        }
        double cossim = dotProduct(f1.counts, f2.counts) / (f1.length * f2.length);
        // Note that inf * 0 is indeterminate form (what gives NaN)
        if ( cossim == 0 ) {
        	return 0;
        }
        return cossim * weight;
         
         
         
     }
     
     
    static  List<Pair<Integer, Integer>> calculateCounts(List<Integer> li) {
        ArrayList<Pair<Integer, Integer>> ret = new ArrayList<>();
        if (li.isEmpty()) {
            return ret;
        }
        int i = 0;
        int akt = li.get(0);
        int count = 1;
        while (i < li.size()) {
            int c = li.get(i);
            if (c != akt) {
                ret.add(new Pair<>(akt, count));
                akt = c;
                count = 1;
            } else {
                count++;
            }
            i++;

        }
        ret.add(new Pair<>(akt, count));
        return ret;

    }
     
    static private double vectorLength(List<Pair<Integer, Integer>> vector) {
        double ret = 0.0;
        for (Pair<Integer, Integer> entry : vector) {
            ret += entry.getY() * entry.getY();
        }
        return Math.sqrt(ret);
    }

	private double dotProduct(List<Pair<Integer, Integer>> vector1, List<Pair<Integer, Integer>> vector2) {
        double ret = 0.0;
        int i1=0;
        int i2=0;
        
        while (i1<vector1.size() && i2<vector2.size()) {
            Pair<Integer,Integer> p1=vector1.get(i1);
            Pair<Integer,Integer> p2=vector2.get(i2);
            if (Objects.equals(p1.getX(), p2.getX())) {
                 ret +=  p1.getY()* p2.getY();
                 i1++;
                 i2++;
            } else {
                if (p1.getX()<p2.getX()) {
                    i1++;
                } else {
                    i2++;
                }
            }
        }
        
        
        
        return ret;
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

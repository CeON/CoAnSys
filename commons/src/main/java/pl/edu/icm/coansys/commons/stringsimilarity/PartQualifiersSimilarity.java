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

package pl.edu.icm.coansys.commons.stringsimilarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class PartQualifiersSimilarity extends SimilarityCalculator {
    
    private List<String> partNames = Arrays.asList("PART", "CZESC", "CZ");

    @Override
    protected float doCalculate(String s1, String s2) {
        
        List<String> partQualifiersS1 = getPartQualifications(s1);
        List<String> partQualifiersS2 = getPartQualifications(s2);
 
        return partQualifiersS1.equals(partQualifiersS2) ? 1.0f : 0.0f;        
    }
    
    private List<String> getPartQualifications(String value) {
        List<String> result = new ArrayList<String>();
        String[] tokens = value.split(" ");
        for (int i = 0; i < tokens.length - 1; i++) {
            if (partNames.contains(tokens[i].toUpperCase())) {
                String partQ = tokens[i+1];
                if (i+2 < tokens.length && tokens[i+2].length() == 1) {
                    partQ += tokens[i+2];
                }
                result.add(partQ);
            }
        }
        return result;
    }
}

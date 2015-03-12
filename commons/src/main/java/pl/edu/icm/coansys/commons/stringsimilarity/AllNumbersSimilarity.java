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
import java.util.List;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AllNumbersSimilarity extends SimilarityCalculator {

    @Override
    protected float doCalculate(String s1, String s2) {
        
        List<String> numbersS1 = getNumbers(s1);
        List<String> numbersS2 = getNumbers(s2);
 
        return numbersS1.equals(numbersS2) ? 1.0f : 0.0f;        
    }
    
    private List<String> getNumbers(String value) {
        List<String> result = new ArrayList<String>();
        for (String token : value.split(" ")) {
            String digits = token.replaceAll("\\D+","");
            if (!digits.isEmpty()) {
                result.add(digits);
            }
        }
        return result;
    }
}

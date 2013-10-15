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

package pl.edu.icm.coansys.commons.stringsimilarity;

import pl.edu.icm.coansys.commons.java.StringTools;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class TrailingNumbersSimilarity extends SimilarityCalculator {

    @Override
    protected float doCalculate(String s1, String s2) {
        
        String s1TrailingInteger = StringTools.getTrailingInteger(s1);
        String s2TrailingInteger = StringTools.getTrailingInteger(s2);
        
        if (s1TrailingInteger != null && s2TrailingInteger != null) {
            return s1TrailingInteger.equals(s2TrailingInteger) ? 1.0f : 0.0f;
        } else {
            return (s1TrailingInteger == null && s2TrailingInteger == null) ? 1.0f : 0.0f;
        }
    }
}

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

/**
 *
 * @author acz
 */
public class EqualityChecker extends SimilarityCalculator {

    /**
     * Checks if both strings are equals
     *
     * @param s1
     * @param s2
     * @return
     */
    @Override
    protected float doCalculate(String s1, String s2) {
        if (s1.equals(s2)) {
            return 1.0f;
        } else {
            return 0.0f;
        }
    }

}

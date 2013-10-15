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

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public abstract class SimilarityCalculator {

    /**
     * Compares two strings and determines their similarity
     *
     * @param s1 first string to compare
     * @param s2 second string to compare
     * @return x (0.0 <= x <= 1.0) which represents the similarity of strings. Greatest number represents more similar
     * strings.
     */
    public final float calculateSimilarity(String s1, String s2) {
        checkArgs(s1, s2);
        return doCalculate(s1, s2);
    }

    protected void checkArgs(String s1, String s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException();
        }
    }

    protected abstract float doCalculate(String s1, String s2);
}

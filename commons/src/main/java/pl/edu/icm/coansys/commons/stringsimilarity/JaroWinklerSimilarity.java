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
 * This code is derived from Fine-Grained Record Integration and Linkage Tool
 * written by Pawel Jurczyk (http://fril.sourceforge.net/)
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class JaroWinklerSimilarity extends SimilarityCalculator {

    private int maxPrefLength = 4;
    private float weight = 0.1f;

    @Override
    protected float doCalculate(String s1, String s2) {
        String _s1 = s1.toLowerCase();
        String _s2 = s2.toLowerCase();
        float dist = score(_s1, _s2);
        dist = dist + commonPrefix(_s1, _s2, maxPrefLength) * weight * (1.0f - dist);
        if (dist < 0) {
            dist = 0;
        }
        if (dist > 1.0f) {
            dist = 1.0f;
        }
        return dist;
    }

    private float score(String s1, String s2) {

        int limit = (s1.length() > s2.length()) ? s2.length() / 2 + 1 : s1.length() / 2 + 1;

        String c1 = commonChars(s1, s2, limit);
        String c2 = commonChars(s2, s1, limit);

        if ((c1.length() != c2.length()) || c1.length() == 0 || c2.length() == 0) {
            return 0;
        }
        int transpositions = transpositions(c1, c2);
        return (c1.length() / ((float) s1.length()) + c2.length() / ((float) s2.length()) + (c1.length() - transpositions) / ((float) c1.length())) / 3.0f;
    }

    private String commonChars(String s1, String s2, int limit) {

        StringBuilder common = new StringBuilder();
        StringBuilder copy = new StringBuilder(s2);

        for (int i = 0; i < s1.length(); i++) {
            char ch = s1.charAt(i);
            boolean foundIt = false;
            for (int j = Math.max(0, i - limit); !foundIt && j < Math.min(i + limit, s2.length()); j++) {
                if (copy.charAt(j) == ch) {
                    foundIt = true;
                    common.append(ch);
                    copy.setCharAt(j, '*');
                }
            }
        }
        return common.toString();
    }

    private int transpositions(String c1, String c2) {
        int transpositions = 0;
        for (int i = 0; i < c1.length(); i++) {
            if (c1.charAt(i) != c2.charAt(i)) {
                transpositions++;
            }
        }
        return transpositions / 2;
    }

    private static int commonPrefix(String c1, String c2, int maxPref) {
        int n = Math.min(maxPref, Math.min(c1.length(), c2.length()));
        for (int i = 0; i < n; i++) {
            if (c1.charAt(i) != c2.charAt(i)) {
                return i;
            }
        }
        return n;
    }
}

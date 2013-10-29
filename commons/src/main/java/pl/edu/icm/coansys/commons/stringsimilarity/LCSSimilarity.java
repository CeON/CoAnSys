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
public class LCSSimilarity extends SimilarityCalculator {

    @Override
    protected float doCalculate(String s1, String s2) {
        int lcs = lcs(s1, s2);
        float sim = 2.0f * lcs / Math.min(s1.length(), s2.length()) - 1;
        if (sim < 0.0f) {
            return 0.0f;
        } else if (sim > 1.0f) {
            return 1.0f;
        } else {
            return sim;
        }
    }
    
    private int lcs(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] lcs = new int[m+1][n+1];
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    lcs[i+1][j+1] = lcs[i][j] + 1;
                } else {
                    lcs[i+1][j+1] = Math.max(lcs[i+1][j], lcs[i][j+1]);
                }
            }
        }
        return lcs[m][n];
    }    
}

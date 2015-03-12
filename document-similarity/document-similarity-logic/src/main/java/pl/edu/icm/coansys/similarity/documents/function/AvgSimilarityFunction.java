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

package pl.edu.icm.coansys.similarity.documents.function;

import java.util.List;

/**
 *
 * @author akawa
 */
public class AvgSimilarityFunction implements SimilarityFunction {
    
    private static final double EMPTY_VALUE = 0d;

    @Override
    public double getDocumentsKeywordSimilarity(String keyword, String doc1key, double doc1keywordWeight, String doc2key, double doc2keywordWeight) {
        return (doc1keywordWeight + doc2keywordWeight) / 2;
    }

    @Override
    public double getDocumentsTotalSimilarity(String doc1key, String doc2key, List<Double> keywordsSimilarities) {
        if (keywordsSimilarities == null || keywordsSimilarities.isEmpty()) {
            return EMPTY_VALUE;
        }
        
        double similarity = 1d;
        for (int i = 0; i < keywordsSimilarities.size(); ++i) {
            similarity += keywordsSimilarities.get(i);
        }
        
        similarity /= (double) keywordsSimilarities.size();
        return similarity;
    }
}

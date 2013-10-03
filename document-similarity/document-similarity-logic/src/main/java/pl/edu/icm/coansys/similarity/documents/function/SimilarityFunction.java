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

package pl.edu.icm.coansys.similarity.documents.function;

import java.util.List;

/**
 *
 * @author akawa
 */
public interface SimilarityFunction {
    double getDocumentsKeywordSimilarity(String keyword, String doc1key, double doc1keywordWeight, String doc2key, double doc2keywordWeight);
    double getDocumentsTotalSimilarity(String doc1key, String doc2key, List<Double> keywordsSimilarities);
}

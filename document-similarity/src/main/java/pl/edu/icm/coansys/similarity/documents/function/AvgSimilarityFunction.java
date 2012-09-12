package pl.edu.icm.coansys.similarity.documents.function;

import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author akawa
 */
public class AvgSimilarityFunction implements SimilarityFunction {
    
    private static double EMPTY_VALUE = 0d;

    @Override
    public double getDocumentsKeywordSimilarity(String keyword, byte[] doc1key, double doc1keywordWeight, byte[] doc2key, double doc2keywordWeight) {
        return doc1keywordWeight * doc2keywordWeight;
    }

    @Override
    public double getDocumentsTotalSimilarity(byte[] doc1key, byte[] doc2key, List<Double> keywordsSimilarities) {
        if (keywordsSimilarities == null || keywordsSimilarities.isEmpty()) {
            return EMPTY_VALUE;
        }
        
        double similarity = keywordsSimilarities.get(0);
        for (int i = 1; i < keywordsSimilarities.size(); ++i) {
            similarity *= keywordsSimilarities.get(i);
        }
        
        similarity /= keywordsSimilarities.size();
        return similarity;
    }
}
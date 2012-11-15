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
        return doc1keywordWeight * doc2keywordWeight;
    }

    @Override
    public double getDocumentsTotalSimilarity(String doc1key, String doc2key, List<Double> keywordsSimilarities) {
        if (keywordsSimilarities == null || keywordsSimilarities.isEmpty()) {
            return EMPTY_VALUE;
        }
        
        double similarity = 1d;
        for (int i = 0; i < keywordsSimilarities.size(); ++i) {
            similarity *= keywordsSimilarities.get(i);
        }
        
        similarity /= (double) keywordsSimilarities.size();
        return similarity;
    }
}
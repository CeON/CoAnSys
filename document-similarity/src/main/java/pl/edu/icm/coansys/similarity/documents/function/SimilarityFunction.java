package pl.edu.icm.coansys.similarity.documents.function;

import java.util.List;

/**
 *
 * @author akawa
 */
public interface SimilarityFunction {
    double getDocumentsKeywordSimilarity(String keyword, byte[] doc1key, double doc1keywordWeight, byte[] doc2key, double doc2keywordWeight);
    double getDocumentsTotalSimilarity(byte[] doc1key, byte[] doc2key, List<Double> keywordsSimilarities);
}

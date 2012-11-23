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

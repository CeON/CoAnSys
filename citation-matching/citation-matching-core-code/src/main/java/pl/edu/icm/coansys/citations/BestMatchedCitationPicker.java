package pl.edu.icm.coansys.citations;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;

import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.SimilarityMeasurer;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import scala.Tuple2;

/**
 * Picker of best matched citation among (citation, document) pairs.
 * 
 * @author madryk
 */
public class BestMatchedCitationPicker implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    private static final double MIN_SIMILARITY = 0.5;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Picks best matched citations among every (citation, document) pair sharing the same citation.
     * Best matched citation is picked based on a similarity between its citation and document.
     * Similarity is calculated using {@link SimilarityMeasurer#similarity(MatchableEntity, MatchableEntity)}.
     * To be included in final results, similarity must be equal or greater than 0.5
     * 
     * @return Rdd with keys being citations and values being {@link Text}s composed of similarity and document id separated by colon
     */
    public JavaPairRDD<TextWithBytesWritable, Text> pickBest(JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> citDocPairs) {
        
        JavaPairRDD<TextWithBytesWritable, Text> bestMatchedCitations = citDocPairs
                .mapPartitionsToPair(citAndDocParitition -> {
                    
                    SimilarityMeasurer similarityMeasurer = new SimilarityMeasurer(SimilarityMeasurer.advancedFvBuilder());
                    
                    return calculateSimilarityAndFilter(similarityMeasurer, citAndDocParitition);
                })
                .reduceByKey((docWithSim1, docWithSim2) -> (docWithSim1.getSimilarity() > docWithSim2.getSimilarity()) ? docWithSim1 : docWithSim2)
                .mapValues(docWithSim -> new Text(docWithSim.getSimilarity() + ":" + docWithSim.getId()));

        return bestMatchedCitations;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<Tuple2<TextWithBytesWritable, IdWithSimilarity>> calculateSimilarityAndFilter(SimilarityMeasurer similarityMeasurer, Iterator<Tuple2<TextWithBytesWritable, TextWithBytesWritable>> citAndDocIterable) {
        
        List<Tuple2<TextWithBytesWritable, IdWithSimilarity>> citDocIdPairsWithSimilarity = Lists.newArrayList();
        
        
        while(citAndDocIterable.hasNext()) {
            
            Tuple2<TextWithBytesWritable, TextWithBytesWritable> citAndDoc = citAndDocIterable.next();
            
            double similarity = calculateSimilarity(similarityMeasurer, citAndDoc._1, citAndDoc._2);
            
            if (similarity >= MIN_SIMILARITY) {
                citDocIdPairsWithSimilarity.add(createCitDocWithSimilarityPair(citAndDoc._1, citAndDoc._2, similarity));
            }
        }
        
        return citDocIdPairsWithSimilarity;
    }
    
    private Tuple2<TextWithBytesWritable, IdWithSimilarity> createCitDocWithSimilarityPair(
            TextWithBytesWritable citationWritable, TextWithBytesWritable documentWritable, double similarity) {
        
        return new Tuple2<TextWithBytesWritable, IdWithSimilarity>(
                citationWritable, 
                new IdWithSimilarity(documentWritable.text().toString(), similarity));
    }
    
    private double calculateSimilarity(SimilarityMeasurer similarityMeasurer,
            TextWithBytesWritable citationWritable, TextWithBytesWritable documentWritable) {
        
        MatchableEntity citation = MatchableEntity.fromBytes(citationWritable.bytes().copyBytes());
        MatchableEntity document = MatchableEntity.fromBytes(documentWritable.bytes().copyBytes());
        
        double similarity = similarityMeasurer.similarity(citation, document);
        
        return similarity;
    }

    
    private static class IdWithSimilarity {
        
        private String id;
        private double similarity;
        
        public IdWithSimilarity(String id, double similarity) {
            this.id = id;
            this.similarity = similarity;
        }

        public double getSimilarity() {
            return similarity;
        }

        public String getId() {
            return id;
        }
        
    }
}

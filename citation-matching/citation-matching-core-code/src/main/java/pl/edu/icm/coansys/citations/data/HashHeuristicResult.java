package pl.edu.icm.coansys.citations.data;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;

/**
 * Result of CitationHashHeuristicMatcher.
 * It contains matched citations and documents id pairs and
 * citations that was unmatched.
 * 
 * @author madryk
 *
 */
public class HashHeuristicResult {

    private JavaPairRDD<Text, Text> citDocIdPairs;
    
    private JavaPairRDD<Text, BytesWritable> unmatchedCitations;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public HashHeuristicResult(JavaPairRDD<Text, Text> citDocIdPairs, JavaPairRDD<Text, BytesWritable> unmatchedCitations) {
        this.citDocIdPairs = citDocIdPairs;
        this.unmatchedCitations = unmatchedCitations;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public JavaPairRDD<Text, Text> getCitDocIdPairs() {
        return citDocIdPairs;
    }
    
    public JavaPairRDD<Text, BytesWritable> getUnmatchedCitations() {
        return unmatchedCitations;
    }
}

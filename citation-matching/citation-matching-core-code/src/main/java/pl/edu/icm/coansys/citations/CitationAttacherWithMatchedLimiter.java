package pl.edu.icm.coansys.citations;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;

import com.google.common.collect.MinMaxPriorityQueue;

import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import pl.edu.icm.coansys.citations.util.misc;
import scala.Tuple2;
import scala.collection.JavaConversions;

/**
 * Attacher of citation into (citation_id, document) pairs with limiter of
 * pairs with the same citation_id
 * 
 * @author madryk
 */
public class CitationAttacherWithMatchedLimiter implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    private final static int DEFAULT_SAME_CITATIONS_LIMIT = 20;
    
    
    private int sameCitationsLimit;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public CitationAttacherWithMatchedLimiter() {
        this(DEFAULT_SAME_CITATIONS_LIMIT);
    }
    
    public CitationAttacherWithMatchedLimiter(int sameCitationsLimit) {
        this.sameCitationsLimit = sameCitationsLimit;
    }
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Attaches citation into (citation_id, document) pairs.
     * Additionally it limits produced (citation, document) pairs with
     * the same citation to {@link #getSameCitationsLimit()} records.
     * Method limits records based on number of mutual tokens.
     */
    public JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> attachCitationsAndLimitDocs(JavaPairRDD<Text, TextWithBytesWritable> citIdDocPairs, JavaPairRDD<Text, BytesWritable> citations) {
        
        JavaPairRDD<Text, TextWithBytesWritable> citationsTextWithBytes = citations.mapToPair(x -> new Tuple2<Text, TextWithBytesWritable>(x._1, new TextWithBytesWritable(x._1, x._2)));
        
        JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> citDocPairs = citIdDocPairs
                .groupByKey()
                .join(citationsTextWithBytes)
                .flatMapToPair(docsWithCit -> {
                    
                    MatchableEntity citation = MatchableEntity.fromBytes(docsWithCit._2._2.bytes().copyBytes());
                    
                    Collection<BytesWritableWithSimilarity> queue = calculateSimilarityAndLimitDocs(citation, docsWithCit._2._1);
                    
                    return queue.stream()
                            .map(x -> new Tuple2<TextWithBytesWritable, TextWithBytesWritable>(docsWithCit._2._2, x.getBytesWritable()))
                            .collect(Collectors.toList());
                });
        
        return citDocPairs;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public int getSameCitationsLimit() {
        return sameCitationsLimit;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private Collection<BytesWritableWithSimilarity> calculateSimilarityAndLimitDocs(MatchableEntity citation, Iterable<TextWithBytesWritable> documentsWritable) {
        
        MinMaxPriorityQueue<BytesWritableWithSimilarity> queue = MinMaxPriorityQueue
                .orderedBy(new BytesWritableWithSimilarityComparator())
                .maximumSize(sameCitationsLimit)
                .create();
        
        for (TextWithBytesWritable documentWritable : documentsWritable) {
            
            MatchableEntity document = MatchableEntity.fromBytes(documentWritable.bytes().copyBytes());
            
            double similarity = calculateTokenSimilarity(citation, document);
            
            queue.add(new BytesWritableWithSimilarity(similarity, documentWritable));
            
        }
        
        return queue;
    }
    
    private double calculateTokenSimilarity(MatchableEntity citation, MatchableEntity document) {
        
        Set<String> srcTokens = JavaConversions.asJavaSet(misc.niceTokens(citation.toReferenceString()));
        Set<String> dstTokens = JavaConversions.asJavaSet(misc.niceTokens(document.toReferenceString()));
        
        long mutualTokensCount = srcTokens.stream().filter(x -> dstTokens.contains(x)).count();
        
        double similarity = 2.0 * mutualTokensCount / (srcTokens.size() + dstTokens.size());
        
        return similarity;
    }
    
    
    
    private static class BytesWritableWithSimilarity {
        
        private double similarity;
        private TextWithBytesWritable bytesWritable;
        
        public BytesWritableWithSimilarity(double similarity, TextWithBytesWritable bytesWritable) {
            this.similarity = similarity;
            this.bytesWritable = bytesWritable;
        }

        public double getSimilarity() {
            return similarity;
        }

        public TextWithBytesWritable getBytesWritable() {
            return bytesWritable;
        }
        
    }
    
    private static class BytesWritableWithSimilarityComparator implements Comparator<BytesWritableWithSimilarity> {
        
        @Override
        public int compare(BytesWritableWithSimilarity o1, BytesWritableWithSimilarity o2) {
            return -Double.compare(o1.getSimilarity(), o2.getSimilarity());
        }
        
    }
}

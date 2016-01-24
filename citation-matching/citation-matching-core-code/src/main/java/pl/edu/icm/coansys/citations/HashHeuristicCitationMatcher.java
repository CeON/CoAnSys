package pl.edu.icm.coansys.citations;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;

import pl.edu.icm.coansys.citations.data.HashHeuristicResult;
import pl.edu.icm.coansys.citations.data.InvalidHashExtractor;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import scala.Tuple2;

/**
 * Citation matcher based on citation and document hash equality.
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class HashHeuristicCitationMatcher {

    private InvalidHashExtractor invalidHashExtractor = new InvalidHashExtractor();
    
    private MatchableEntityHasher citationHasher;
    
    private MatchableEntityHasher documentHasher;
    
    private long maxHashBucketSize;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public HashHeuristicCitationMatcher(MatchableEntityHasher citationHasher, MatchableEntityHasher documentHasher, long maxHashBucketSize) {
        this.citationHasher = citationHasher;
        this.documentHasher = documentHasher;
        this.maxHashBucketSize = maxHashBucketSize;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public HashHeuristicResult matchCitations(JavaPairRDD<Text, BytesWritable> citations, JavaPairRDD<Text, BytesWritable> documents, 
            boolean needUnmatched) {
        
        JavaPairRDD<String, String> citationHashIdPairs = generateHashIdPairs(citations, citationHasher);
        JavaPairRDD<String, String> documentHashIdPairs = generateHashIdPairs(documents, documentHasher);

        // remove invalid hashes
        JavaPairRDD<String, Long> invalidHashes = invalidHashExtractor.extractInvalidHashes(citationHashIdPairs, documentHashIdPairs, maxHashBucketSize);

        citationHashIdPairs = citationHashIdPairs.subtractByKey(invalidHashes);
        documentHashIdPairs = documentHashIdPairs.subtractByKey(invalidHashes);
        
        // join citationIds to documentIds by hash
        JavaPairRDD<String, String> citationDocumentIdPairs = citationHashIdPairs.join(documentHashIdPairs).mapToPair(cd->cd._2()).distinct();
        
        // find unmatched citations
        JavaPairRDD<Text, BytesWritable> unmatchedCitations = null;
        if (needUnmatched) {
            JavaPairRDD<String, BytesWritable> citationIdBytes = citations.mapToPair(c->new Tuple2<String, BytesWritable>(c._1().toString(), c._2()));
            unmatchedCitations = citationIdBytes.subtractByKey(citationDocumentIdPairs).mapToPair(c->new Tuple2<Text, BytesWritable>(new Text(c._1()), c._2()));
            
        }

        // convert string pairs to text pairs
        JavaPairRDD<Text, Text> textCitDocIdPairs = citationDocumentIdPairs.mapToPair(strCitDocId -> new Tuple2<Text, Text>(new Text(strCitDocId._1()), new Text(strCitDocId._2())));
        
        return new HashHeuristicResult(textCitDocIdPairs, unmatchedCitations);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JavaPairRDD<String, String> generateHashIdPairs(JavaPairRDD<Text, BytesWritable> matchableEntityBytes, MatchableEntityHasher hasher) {
        
        JavaPairRDD<String, String> hashIdPairs = matchableEntityBytes.flatMapToPair((Tuple2<Text, BytesWritable> keyValue)-> {
            MatchableEntity matchableEntity = MatchableEntity.fromBytes(keyValue._2().copyBytes());
            return hasher.hashEntity(matchableEntity);
        });
        
        return hashIdPairs;
    }
    
}

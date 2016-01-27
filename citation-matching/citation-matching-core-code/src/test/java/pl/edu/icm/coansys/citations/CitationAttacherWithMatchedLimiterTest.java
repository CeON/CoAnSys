package pl.edu.icm.coansys.citations;

import static org.testng.Assert.*;
import static pl.edu.icm.coansys.citations.MatchableEntityDataProvider.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.util.misc;
import scala.Tuple2;
import scala.collection.JavaConversions;

/**
 * @author madryk
 */
public class CitationAttacherWithMatchedLimiterTest {

    private CitationAttacherWithMatchedLimiter citationAttacher = new CitationAttacherWithMatchedLimiter(3);
    
    private JavaSparkContext sparkContext;
    
    
    @BeforeMethod
    public void before() {
        
        SparkConf conf = new SparkConf().setMaster("local").setAppName("CitationAttacherWithMatchedLimiterTest")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        
        sparkContext = new JavaSparkContext(conf);
        
    }
    
    
    @AfterMethod
    public void after() {
        
        if (sparkContext != null) {
            sparkContext.close();
            sparkContext = null;
        }
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void attachCitationsAndLimitDocs() {
        
        
        // given
        
        JavaPairRDD<String, MatchableEntity> citIdDocPairs = sparkContext.parallelizePairs(ImmutableList.of(
                new Tuple2<>(citation1.id(), document1),
                new Tuple2<>(citation1.id(), document2),
                new Tuple2<>(citation2.id(), document3)));
        
        JavaPairRDD<String, MatchableEntity> citations = sparkContext.parallelizePairs(generateIdWithEntityTuples(
                Lists.newArrayList(citation1, citation2, citation3, citation4, citation5)));
        
        
        
        // execute
        
        JavaPairRDD<MatchableEntity, MatchableEntity> actualCitDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
        
        
        // assert
        List<Tuple2<MatchableEntity, MatchableEntity>> expectedCitDocPairs = ImmutableList.of(
                new Tuple2<>(citation1, document1),
                new Tuple2<>(citation1, document2),
                new Tuple2<>(citation2, document3));
        
        assertCitDocPairsEquals(actualCitDocPairs.collect(), expectedCitDocPairs);
        
    }
    
    @Test
    public void attachCitationsAndLimitDocs_LIMIT_REACHED() {
        
        
        // given
        
        JavaPairRDD<String, MatchableEntity> citIdDocPairs = sparkContext.parallelizePairs(ImmutableList.of(
                new Tuple2<>(citation1.id(), document1),        // similarity: 0.25806451612903225
                new Tuple2<>(citation1.id(), document2),        // similarity: 0.7741935483870968
                new Tuple2<>(citation1.id(), document3v2),      // similarity: 0.16
                new Tuple2<>(citation1.id(), document3),        // similarity: 0.16
                new Tuple2<>(citation1.id(), document4),        // similarity: 0.0625
                new Tuple2<>(citation2.id(), document5)));      // similarity: 0
        
        JavaPairRDD<String, MatchableEntity> citations = sparkContext.parallelizePairs(generateIdWithEntityTuples(
                Lists.newArrayList(citation1, citation2, citation3, citation4, citation5)));
        
        System.out.println("TOKEN SIM: " + citation1.id() + " -> " + document1.id() + ": " + calculateTokenSimilarity(citation1, document1));
        System.out.println("TOKEN SIM: " + citation1.id() + " -> " + document2.id() + ": " + calculateTokenSimilarity(citation1, document2));
        System.out.println("TOKEN SIM: " + citation1.id() + " -> " + document3.id() + ": " + calculateTokenSimilarity(citation1, document3));
        System.out.println("TOKEN SIM: " + citation1.id() + " -> " + document4.id() + ": " + calculateTokenSimilarity(citation1, document4));
        System.out.println("TOKEN SIM: " + citation2.id() + " -> " + document5.id() + ": " + calculateTokenSimilarity(citation2, document5));
        
        // execute
        
        JavaPairRDD<MatchableEntity, MatchableEntity> actualCitDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
        
        
        // assert
        List<Tuple2<MatchableEntity, MatchableEntity>> expectedCitDocPairs = ImmutableList.of(
                new Tuple2<>(citation1, document1),
                new Tuple2<>(citation1, document2),
                new Tuple2<>(citation1, document3),
                new Tuple2<>(citation2, document5));
        
        assertCitDocPairsEquals(actualCitDocPairs.collect(), expectedCitDocPairs);
        
    }
    
    private double calculateTokenSimilarity(MatchableEntity citation, MatchableEntity document) {
        
        Set<String> citTokens = JavaConversions.asJavaSet(misc.niceTokens(citation.toReferenceString()));
        Set<String> docTokens = JavaConversions.asJavaSet(misc.niceTokens(document.toReferenceString()));
        
        long mutualTokensCount = citTokens.stream().filter(x -> docTokens.contains(x)).count();
        
        double similarity = 2.0 * mutualTokensCount / (citTokens.size() + docTokens.size());
        
        System.out.println("M/A: " + mutualTokensCount + "/" + citTokens.size() + "+" + docTokens.size() + "            " + (citTokens.size() + docTokens.size()));
        
        return similarity;
    }
    
    @Test
    public void attachCitationsAndLimitDocs_NO_CITATIONS() {
        
        
        // given
        
        JavaPairRDD<String, MatchableEntity> citIdDocPairs = sparkContext.parallelizePairs(ImmutableList.of(
                new Tuple2<>(citation1.id(), document1),
                new Tuple2<>(citation1.id(), document2),
                new Tuple2<>(citation2.id(), document4)));
        
        JavaPairRDD<String, MatchableEntity> citations = sparkContext.parallelizePairs(Lists.newArrayList());
        
        
        
        // execute
        
        JavaPairRDD<MatchableEntity, MatchableEntity> actualCitDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
        
        
        // assert
        
        assertEquals(actualCitDocPairs.count(), 0);
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertCitDocPairsEquals(List<Tuple2<MatchableEntity, MatchableEntity>> actualCitDocPairs, List<Tuple2<MatchableEntity, MatchableEntity>> expectedCitDocPairs) {
        
        assertEquals(actualCitDocPairs.size(), expectedCitDocPairs.size());
        
        for (Tuple2<MatchableEntity, MatchableEntity> actualCitDocPair : actualCitDocPairs) {
            assertTrue(isInCitDocPairs(expectedCitDocPairs, actualCitDocPair));
        }
        
    }
    
    
    private boolean isInCitDocPairs(List<Tuple2<MatchableEntity, MatchableEntity>> citDocPairs, Tuple2<MatchableEntity, MatchableEntity> citDocPairToFind) {
        
        String citIdToFind = citDocPairToFind._1.id();
        String docIdToFind = citDocPairToFind._2.id();
        
        for (Tuple2<MatchableEntity, MatchableEntity> citDocPair : citDocPairs) {
            String citId = citDocPair._1.id();
            String docId = citDocPair._2.id();
            
            if (citId.equals(citIdToFind) && docId.equals(docIdToFind)) {
                return Arrays.equals(citDocPair._1.data().toByteArray(), citDocPairToFind._1.data().toByteArray()) &&
                        Arrays.equals(citDocPair._2.data().toByteArray(), citDocPairToFind._2.data().toByteArray());
            }
        }
        
        return false;
        
    }
}


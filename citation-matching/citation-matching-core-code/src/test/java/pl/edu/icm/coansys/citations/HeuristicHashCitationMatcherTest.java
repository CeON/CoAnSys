
package pl.edu.icm.coansys.citations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.HeuristicHashCitationMatcher;
import pl.edu.icm.coansys.citations.MatchableEntityHasher;
import pl.edu.icm.coansys.citations.data.HeuristicHashMatchingResult;
import pl.edu.icm.coansys.citations.hashers.CitationNameYearHashGenerator;
import pl.edu.icm.coansys.citations.hashers.CitationNameYearPagesHashGenerator;
import pl.edu.icm.coansys.citations.hashers.DocumentNameYearHashGenerator;
import pl.edu.icm.coansys.citations.hashers.DocumentNameYearNumNumHashGenerator;
import pl.edu.icm.coansys.citations.hashers.DocumentNameYearPagesHashGenerator;
import pl.edu.icm.coansys.citations.hashers.DocumentNameYearStrictHashGenerator;
import pl.edu.icm.coansys.citations.hashers.HashGenerator;
import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import scala.Tuple2;


/**
 * @author Łukasz Dumiszewski
 * @author madryk
 */
public class HeuristicHashCitationMatcherTest {
    
    
    private JavaSparkContext sparkContext;
    
    
    @BeforeMethod
    public void before() {
        
        SparkConf conf = new SparkConf().setMaster("local").setAppName("HeuristicHashCitationMatcherTest")
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
    public void matchCitations_1() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/citations";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        HeuristicHashCitationMatcher heuristicMatcher = new HeuristicHashCitationMatcher(
                createMatchableEntityHasher(new CitationNameYearPagesHashGenerator()),
                createMatchableEntityHasher(new DocumentNameYearPagesHashGenerator()), 10000);
        
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.sequenceFile(citationPath, Text.class, BytesWritable.class);
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.sequenceFile(documentPath, Text.class, BytesWritable.class);
        
        
        
        // execute
        
        HeuristicHashMatchingResult matchedResult = heuristicMatcher.matchCitations(citations, documents, true);
        
        
        // assert
        
        assertMatchedCitations(matchedResult.getCitDocIdPairs().collect(), "src/test/resources/heuristic/heuristic-out-1");
        
        assertUnmatchedCitations(matchedResult.getUnmatchedCitations().collect(), "src/test/resources/heuristic/unmatched-1");
        
    }

    
    @Test
    public void matchCitations_2() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/unmatched-1";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        HeuristicHashCitationMatcher heuristicMatcher = new HeuristicHashCitationMatcher(
                createMatchableEntityHasher(new CitationNameYearPagesHashGenerator()), 
                createMatchableEntityHasher(new DocumentNameYearNumNumHashGenerator()), 10000);
        
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.sequenceFile(citationPath, Text.class, BytesWritable.class);
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.sequenceFile(documentPath, Text.class, BytesWritable.class);
        
        
        
        // execute
        
        HeuristicHashMatchingResult matchedResult = heuristicMatcher.matchCitations(citations, documents, true);
        
        
        // assert
        
        assertMatchedCitations(matchedResult.getCitDocIdPairs().collect(), "src/test/resources/heuristic/heuristic-out-2");
        
        assertUnmatchedCitations(matchedResult.getUnmatchedCitations().collect(), "src/test/resources/heuristic/unmatched-2");
        
    }
    
    
    @Test
    public void matchCitations_3() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/unmatched-2";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        HeuristicHashCitationMatcher heuristicMatcher = new HeuristicHashCitationMatcher(
                createMatchableEntityHasher(new CitationNameYearHashGenerator()), 
                createMatchableEntityHasher(new DocumentNameYearStrictHashGenerator()), 10000);
        
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.sequenceFile(citationPath, Text.class, BytesWritable.class);
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.sequenceFile(documentPath, Text.class, BytesWritable.class);
        
        
        // execute
        
        HeuristicHashMatchingResult matchedResult = heuristicMatcher.matchCitations(citations, documents, true);
        
        
        // assert
        
        assertMatchedCitations(matchedResult.getCitDocIdPairs().collect(), "src/test/resources/heuristic/heuristic-out-3");
        
        assertUnmatchedCitations(matchedResult.getUnmatchedCitations().collect(), "src/test/resources/heuristic/unmatched-3");
        
    }
    
    
    @Test
    public void matchCitations_4() throws IOException {
        
        
        // given
        
        String citationPath = "src/test/resources/heuristic/unmatched-3";
        
        String documentPath = "src/test/resources/heuristic/documents";
        
        HeuristicHashCitationMatcher heuristicMatcher = new HeuristicHashCitationMatcher(
                createMatchableEntityHasher(new CitationNameYearHashGenerator()), 
                createMatchableEntityHasher(new DocumentNameYearHashGenerator()), 10000);
        
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.sequenceFile(citationPath, Text.class, BytesWritable.class);
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.sequenceFile(documentPath, Text.class, BytesWritable.class);
        
        
        // execute
        
        HeuristicHashMatchingResult matchedResult = heuristicMatcher.matchCitations(citations, documents, false);
        
        
        // assert
        
        assertMatchedCitations(matchedResult.getCitDocIdPairs().collect(), "src/test/resources/heuristic/heuristic-out-4");
        
        assertNull(matchedResult.getUnmatchedCitations());
        
    }



    
    
    //------------------------ PRIVATE --------------------------

    
    private void assertUnmatchedCitations(List<Tuple2<Text, BytesWritable>> actualUnmatchedCitations, String expectedUnmatchedCitationDirPath) throws IOException {
        
        List<Pair<Text, BytesWritable>> expectedUnmatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedUnmatchedCitationDirPath), Text.class, BytesWritable.class);
        
        assertEquals(expectedUnmatchedCitations.size(), actualUnmatchedCitations.size());
        
        for (Tuple2<Text, BytesWritable> actualUnmatchedCitation : actualUnmatchedCitations) {
            assertTrue(isInUnmatchedCitations(expectedUnmatchedCitations, actualUnmatchedCitation));
        }
        
    }
    
    
    private void assertMatchedCitations(List<Tuple2<Text, Text>> actualMatchedCitations, String expectedMatchedCitationDirPath) throws IOException {
        
        List<Pair<Text, Text>> expectedMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedMatchedCitationDirPath), Text.class, Text.class);
        
        assertEquals(expectedMatchedCitations.size(), actualMatchedCitations.size());
        
        for (Tuple2<Text, Text> actualCitationDocIdPair : actualMatchedCitations) {
            assertTrue(isInMatchedCitations(expectedMatchedCitations, actualCitationDocIdPair));
        }
    }
    
    
    private boolean isInMatchedCitations(List<Pair<Text, Text>> citations, Tuple2<Text, Text> citationDocIdPair) {
        
        for (Pair<Text, Text> citDocIdPair : citations) { 
            if (citDocIdPair.getKey().equals(citationDocIdPair._1) && (citDocIdPair.getValue().equals(citationDocIdPair._2))) {
                return true;
            }
        }
        
        return false;
        
    }

    private boolean isInUnmatchedCitations(List<Pair<Text, BytesWritable>> citations, Tuple2<Text, BytesWritable> citation) {
        
        for (Pair<Text, BytesWritable> cit : citations) {
            if (cit.getKey().equals(citation._1)) {
                return Arrays.equals(cit.getValue().copyBytes(), citation._2.copyBytes());
            }
                
        }
        
        return false;
        
    }
    
    private MatchableEntityHasher createMatchableEntityHasher(HashGenerator hashGenerator) {
        MatchableEntityHasher matchableEntityHasher = new MatchableEntityHasher();
        matchableEntityHasher.setHashGenerator(hashGenerator);
        return matchableEntityHasher;
    }


    
    
    
}

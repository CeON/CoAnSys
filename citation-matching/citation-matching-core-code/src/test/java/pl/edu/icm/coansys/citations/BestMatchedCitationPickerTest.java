package pl.edu.icm.coansys.citations;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;
import static pl.edu.icm.coansys.citations.MatchableEntityDataProvider.*;

import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import scala.Tuple2;

/**
 * @author madryk
 */
public class BestMatchedCitationPickerTest {

    private final static double DOUBLE_EPSILON = 1e-5;
    
    
    private BestMatchedCitationPicker bestMatchedCitationPicker = new BestMatchedCitationPicker();
    
    private JavaSparkContext sparkContext;
    
    
    @BeforeMethod
    public void before() {
        
        SparkConf conf = new SparkConf().setMaster("local").setAppName("BestMatchedCitationPickerTest")
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
    public void pickBest() {
        
        
        // given
        
        JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> citDocPairs = sparkContext.parallelizePairs(generateCitDocPairs(
                Lists.newArrayList(citation1, citation1, citation1, citation2, citation4, citation4),
                Lists.newArrayList(document1, document2, document3, document4, document4, document5)));
        
        
        
        // execute
        
        JavaPairRDD<TextWithBytesWritable, Text> actualMatchedCitations = bestMatchedCitationPicker.pickBest(citDocPairs);
        
        
        // assert
        
        List<Tuple2<TextWithBytesWritable, Text>> expectedMatchedCitations = generateMatchedCitations(
                Lists.newArrayList(citation1, citation4),
                Lists.newArrayList(document2, document5),
                Lists.newArrayList(0.748069, 0.739753));
        
        assertMatchedEquals(actualMatchedCitations.collect(), expectedMatchedCitations);
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertMatchedEquals(List<Tuple2<TextWithBytesWritable, Text>> actualMatchedCitations, List<Tuple2<TextWithBytesWritable, Text>> expectedMatchedCitations) {
        
        assertEquals(actualMatchedCitations.size(), expectedMatchedCitations.size());
        
        for (Tuple2<TextWithBytesWritable, Text> expectedMatchedCitation : expectedMatchedCitations) {
            
            String expectedCitId = expectedMatchedCitation._1.text().toString();
            String expectedDocId = expectedMatchedCitation._2.toString().substring(expectedMatchedCitation._2.toString().indexOf(":") + 1);
            
            Tuple2<TextWithBytesWritable, Text> actualMatchedCitation = findMatchedCitation(actualMatchedCitations, expectedCitId, expectedDocId);
            if (actualMatchedCitation == null) {
                fail("Expected matched citation not found (" + expectedCitId + ", " + expectedDocId + ")");
            }
            
            assertMatchedCitationEquals(expectedMatchedCitation, actualMatchedCitation);
        }
        
    }
    
    private Tuple2<TextWithBytesWritable, Text> findMatchedCitation(List<Tuple2<TextWithBytesWritable, Text>> matchedCitations, String citIdToFind, String docIdToFind) {
        
        for (Tuple2<TextWithBytesWritable, Text> matchedCitation : matchedCitations) {
            
            String citId = matchedCitation._1.text().toString();
            String docId = matchedCitation._2.toString().substring(matchedCitation._2.toString().indexOf(":") + 1);
            
            if (citId.equals(citIdToFind) && docId.equals(docIdToFind)) {
                return matchedCitation;
            }
            
        }
        return null;
        
    }
    
    private void assertMatchedCitationEquals(Tuple2<TextWithBytesWritable, Text> expectedMatchedCitation, Tuple2<TextWithBytesWritable, Text> actualMatchedCitation) {
        
        double expectedSimilarity = Double.parseDouble(expectedMatchedCitation._2.toString().split(":")[0]);
        byte[] expectedCitationBytes = expectedMatchedCitation._1.bytes().copyBytes();
        
        double actualSimilarity = Double.parseDouble(actualMatchedCitation._2.toString().split(":")[0]);
        byte[] actualCitationBytes = actualMatchedCitation._1.bytes().copyBytes();
        
        assertEquals(expectedCitationBytes, actualCitationBytes);
        assertEquals(expectedSimilarity, actualSimilarity, DOUBLE_EPSILON);
    }
    
}

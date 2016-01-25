package pl.edu.icm.coansys.citations;

import static org.testng.Assert.*;
import static pl.edu.icm.coansys.citations.MatchableEntityDataProvider.*;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
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
public class DocumentAttacherTest {
    
    private DocumentAttacher documentAttacher = new DocumentAttacher();
    
    private JavaSparkContext sparkContext;
    
    
    @BeforeMethod
    public void before() {
        
        SparkConf conf = new SparkConf().setMaster("local").setAppName("DocumentAttacherTest")
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
    public void attachDocuments() {
        
        
        // given
        
        JavaPairRDD<Text, Text> matchedCitations = sparkContext.parallelizePairs(generateCitIdDocIdPairs(
                Lists.newArrayList(citation1, citation1, citation4, citation4),
                Lists.newArrayList(document1, document2, document2, document5)));
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.parallelizePairs(generateEntitiesWritable(
                Lists.newArrayList(document1, document2, document3, document4, document5)));
        
        
        
        // execute
        
        JavaPairRDD<Text, TextWithBytesWritable> actualCitIdDocPairs = documentAttacher.attachDocuments(matchedCitations, documents);
        
        
        // assert
        List<Tuple2<Text, TextWithBytesWritable>> expectedCitIdDocPairs = generateCitIdDocPairs(
                Lists.newArrayList(citation1, citation1, citation4, citation4),
                Lists.newArrayList(document1, document2, document2, document5));
        
        assertDocAttachedMatchedCitations(actualCitIdDocPairs.collect(), expectedCitIdDocPairs);
        
    }
    
    @Test
    public void attachDocuments_EMPTY_DOCS() {
        
        
        // given
        
        JavaPairRDD<Text, Text> matchedCitations = sparkContext.parallelizePairs(generateCitIdDocIdPairs(
                Lists.newArrayList(citation1, citation1, citation4),
                Lists.newArrayList(document1, document2, document5)));
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.parallelizePairs(generateEntitiesWritable(
                Lists.newArrayList()));
        
        
        
        // execute
        
        JavaPairRDD<Text, TextWithBytesWritable> actualCitIdDocPairs = documentAttacher.attachDocuments(matchedCitations, documents);
        
        
        // assert
        
        assertEquals(actualCitIdDocPairs.count(), 0);
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertDocAttachedMatchedCitations(List<Tuple2<Text, TextWithBytesWritable>> actualCitIdDocPairs, List<Tuple2<Text, TextWithBytesWritable>> expectedCitIdDocPairs) {
        
        assertEquals(actualCitIdDocPairs.size(), expectedCitIdDocPairs.size());
        
        for (Tuple2<Text, TextWithBytesWritable> actualCitIdDocPair : actualCitIdDocPairs) {
            assertTrue(isInCitIdDocPairs(expectedCitIdDocPairs, actualCitIdDocPair));
        }
    }
    
    
    private boolean isInCitIdDocPairs(List<Tuple2<Text, TextWithBytesWritable>> citIdDocPairs, Tuple2<Text, TextWithBytesWritable> citIdDocPairToFind) {
        
        Text citIdToFind = citIdDocPairToFind._1;
        Text docIdToFind = citIdDocPairToFind._2.text();
        
        for (Tuple2<Text, TextWithBytesWritable> citIdDocPair : citIdDocPairs) {
            
            Text citId = citIdDocPair._1;
            Text docId = citIdDocPair._2.text();
            
            if (citId.equals(citIdToFind) && docId.equals(docIdToFind)) {
                return Arrays.equals(citIdDocPair._2.bytes().copyBytes(), citIdDocPairToFind._2.bytes().copyBytes());
            }
        }
        
        return false;
        
    }
}

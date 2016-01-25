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
        
        JavaPairRDD<Text, TextWithBytesWritable> citIdDocPairs = sparkContext.parallelizePairs(generateCitIdDocPairs(
                Lists.newArrayList(citation1, citation1, citation2),
                Lists.newArrayList(document1, document2, document3)));
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.parallelizePairs(generateEntitiesWritable(
                Lists.newArrayList(citation1, citation2, citation3, citation4, citation5)));
        
        
        
        // execute
        
        JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> actualCitDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
        
        
        // assert
        List<Tuple2<TextWithBytesWritable, TextWithBytesWritable>> expectedCitDocPairs = generateCitDocPairs(
                Lists.newArrayList(citation1, citation1, citation2),
                Lists.newArrayList(document1, document2, document3));
        
        assertCitDocPairsEquals(actualCitDocPairs.collect(), expectedCitDocPairs);
        
    }
    
    @Test
    public void attachCitationsAndLimitDocs_LIMIT_REACHED() {
        
        
        // given
        
        JavaPairRDD<Text, TextWithBytesWritable> citIdDocPairs = sparkContext.parallelizePairs(generateCitIdDocPairs(
                Lists.newArrayList(citation1, citation1, citation1, citation1, citation2),
                Lists.newArrayList(document1, document2, document3, document4, document5)));
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.parallelizePairs(generateEntitiesWritable(
                Lists.newArrayList(citation1, citation2, citation3, citation4, citation5)));
        
        
        
        // execute
        
        JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> actualCitDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
        
        
        // assert
        List<Tuple2<TextWithBytesWritable, TextWithBytesWritable>> expectedCitDocPairs = generateCitDocPairs(
                Lists.newArrayList(citation1, citation1, citation1, citation2),
                Lists.newArrayList(document1, document2, document3, document5));
        
        assertCitDocPairsEquals(actualCitDocPairs.collect(), expectedCitDocPairs);
        
    }
    
    @Test
    public void attachCitationsAndLimitDocs_NO_CITATIONS() {
        
        
        // given
        
        JavaPairRDD<Text, TextWithBytesWritable> citIdDocPairs = sparkContext.parallelizePairs(generateCitIdDocPairs(
                Lists.newArrayList(citation1, citation1, citation2),
                Lists.newArrayList(document1, document2, document4)));
        
        JavaPairRDD<Text, BytesWritable> citations = sparkContext.parallelizePairs(Lists.newArrayList());
        
        
        
        // execute
        
        JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> actualCitDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
        
        
        // assert
        
        assertEquals(actualCitDocPairs.count(), 0);
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertCitDocPairsEquals(List<Tuple2<TextWithBytesWritable, TextWithBytesWritable>> actualCitDocPairs, List<Tuple2<TextWithBytesWritable, TextWithBytesWritable>> expectedCitDocPairs) {
        
        assertEquals(actualCitDocPairs.size(), expectedCitDocPairs.size());
        
        for (Tuple2<TextWithBytesWritable, TextWithBytesWritable> actualCitDocPair : actualCitDocPairs) {
            assertTrue(isInCitDocPairs(expectedCitDocPairs, actualCitDocPair));
        }
        
    }
    
    
    private boolean isInCitDocPairs(List<Tuple2<TextWithBytesWritable, TextWithBytesWritable>> citDocPairs, Tuple2<TextWithBytesWritable, TextWithBytesWritable> citDocPairToFind) {
        
        Text citIdToFind = citDocPairToFind._1.text();
        Text docIdToFind = citDocPairToFind._2.text();
        
        for (Tuple2<TextWithBytesWritable, TextWithBytesWritable> citDocPair : citDocPairs) {
            Text citId = citDocPair._1.text();
            Text docId = citDocPair._2.text();
            
            if (citId.equals(citIdToFind) && docId.equals(docIdToFind)) {
                return Arrays.equals(citDocPair._1.bytes().copyBytes(), citDocPairToFind._1.bytes().copyBytes()) &&
                        Arrays.equals(citDocPair._2.bytes().copyBytes(), citDocPairToFind._2.bytes().copyBytes());
            }
        }
        
        return false;
        
    }
}


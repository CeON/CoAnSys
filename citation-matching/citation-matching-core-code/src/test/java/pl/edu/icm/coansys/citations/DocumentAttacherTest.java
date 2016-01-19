package pl.edu.icm.coansys.citations;

import static org.junit.Assert.assertEquals;
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

import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import pl.edu.icm.coansys.commons.hadoop.LocalSequenceFileUtils;
import scala.Tuple2;

/**
 * @author madryk
 */
public class DocumentAttacherTest {
    
    private DocumentAttacher documentAttacher = new DocumentAttacher();
    
    private JavaSparkContext sparkContext;
    
    
    @BeforeMethod
    public void before() {
        
        SparkConf conf = new SparkConf().setMaster("local").setAppName("HashHeuristicCitationMatcherTest")
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
    public void attachDocuments() throws IOException {
        
        
        // given
        
        String matchedPath = "src/test/resources/doc_attacher/input/matched_citations";
        
        String documentPath = "src/test/resources/doc_attacher/input/documents";
        
        
        JavaPairRDD<Text, Text> matchedCitations = sparkContext.sequenceFile(matchedPath, Text.class, Text.class);
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.sequenceFile(documentPath, Text.class, BytesWritable.class);
        
        
        
        // execute
        
        JavaPairRDD<Text, TextWithBytesWritable> actualDocAttachedMatchedCit = documentAttacher.attachDocuments(matchedCitations, documents);
        
        
        // assert
        
        assertDocAttachedMatchedCitations(actualDocAttachedMatchedCit.collect(), "src/test/resources/doc_attacher/expected_output/matched_with_docs");
        
    }
    
    @Test
    public void attachDocuments_EMPTY_DOCS() throws IOException {
        
        
        // given
        
        String matchedPath = "src/test/resources/doc_attacher/input/matched_citations";
        
        String documentPath = "src/test/resources/doc_attacher/input/documents_empty";
        
        
        JavaPairRDD<Text, Text> matchedCitations = sparkContext.sequenceFile(matchedPath, Text.class, Text.class);
        
        JavaPairRDD<Text, BytesWritable> documents = sparkContext.sequenceFile(documentPath, Text.class, BytesWritable.class);
        
        
        
        // execute
        
        JavaPairRDD<Text, TextWithBytesWritable> actualDocAttachedMatchedCit = documentAttacher.attachDocuments(matchedCitations, documents);
        
        
        // assert
        
        assertEquals(0, actualDocAttachedMatchedCit.count());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertDocAttachedMatchedCitations(List<Tuple2<Text, TextWithBytesWritable>> actualMatchedCitations, String expectedMatchedCitationDirPath) throws IOException {
        
        List<Pair<Text, TextWithBytesWritable>> expectedMatchedCitations = LocalSequenceFileUtils.readSequenceFile(new File(expectedMatchedCitationDirPath), Text.class, TextWithBytesWritable.class);
        
        assertEquals(expectedMatchedCitations.size(), actualMatchedCitations.size());
        
        for (Tuple2<Text, TextWithBytesWritable> actualCitationIdDocPair : actualMatchedCitations) {
            assertTrue(isInMatchedCitations(expectedMatchedCitations, actualCitationIdDocPair));
        }
    }
    
    
    private boolean isInMatchedCitations(List<Pair<Text, TextWithBytesWritable>> citations, Tuple2<Text, TextWithBytesWritable> citationIdDocPair) {
        
        for (Pair<Text, TextWithBytesWritable> citIdDocPair : citations) { 
            if (citIdDocPair.getKey().equals(citationIdDocPair._1) && (citIdDocPair.getValue().text().equals(citationIdDocPair._2.text()))) {
                return Arrays.equals(citIdDocPair.getValue().bytes().copyBytes(), citationIdDocPair._2.bytes().copyBytes());
            }
        }
        
        return false;
        
    }
}

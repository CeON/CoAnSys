package pl.edu.icm.coansys.citations;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import scala.Tuple2;

/**
 * 
 * @author madryk
 *
 */
public class DocumentAttacherTest {

    private DocumentAttacher documentAttacher = new DocumentAttacher();
    
    @Mock
    private JavaPairRDD<Text, Text> matchedCitations;
    
    @Mock
    private JavaPairRDD<Text, BytesWritable> documents;
    
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<Text, Text>, Text, Text>> captureSwapFunction;
    @Mock
    private JavaPairRDD<Text, Text> swappedMatchedCitations;
    
    @Mock
    private JavaPairRDD<Text, Tuple2<Text, BytesWritable>> matchedWithJoinedDocuments;
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<Text,Tuple2<Text,BytesWritable>>, Text, TextWithBytesWritable>> captureDiscardDocIdFunction;
    @Mock
    private JavaPairRDD<Text, TextWithBytesWritable> matchedWithDocuments;
    
    
    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    
    @Test
    public void attachDocuments() throws Exception {
        
        // given
        
        doReturn(swappedMatchedCitations).when(matchedCitations).mapToPair(any());
        doReturn(matchedWithJoinedDocuments).when(swappedMatchedCitations).join(documents);
        doReturn(matchedWithDocuments).when(matchedWithJoinedDocuments).mapToPair(any());
        
        
        // execute
        
        JavaPairRDD<Text, TextWithBytesWritable> retMatchedWithDocuments = documentAttacher.attachDocuments(matchedCitations, documents);
        
        
        // assert
        assertTrue(retMatchedWithDocuments == matchedWithDocuments);
        
        verify(matchedCitations).mapToPair(captureSwapFunction.capture());
        assertSwapFunction(captureSwapFunction.getValue());
        
        verify(swappedMatchedCitations).join(documents);
        
        verify(matchedWithJoinedDocuments).mapToPair(captureDiscardDocIdFunction.capture());
        assertDiscardDocIdFunction(captureDiscardDocIdFunction.getValue());
        
    }
    
    
    private void assertSwapFunction(PairFunction<Tuple2<Text, Text>, Text, Text> function) throws Exception {
        
        Text keyText = new Text("key text");
        Text valueText = new Text("value text");
        
        Tuple2<Text, Text> ret = function.call(new Tuple2<Text, Text>(keyText, valueText));
        
        assertEquals(ret._1, valueText);
        assertEquals(ret._2, keyText);
    }
    
    private void assertDiscardDocIdFunction(PairFunction<Tuple2<Text,Tuple2<Text,BytesWritable>>, Text, TextWithBytesWritable> function) throws Exception {
        
        Text docId = new Text("docId");
        Text citId = new Text("citId");
        BytesWritable doc = new BytesWritable("some bytes".getBytes());
        Tuple2<Text, BytesWritable> matched = new Tuple2<Text, BytesWritable>(citId, doc);
        
        Tuple2<Text, TextWithBytesWritable> retMatched = function.call(new Tuple2<Text, Tuple2<Text, BytesWritable>>(docId, matched));
        
        assertEquals(retMatched._1.toString(), "citId");
        assertEquals(retMatched._2.text().toString(), "docId");
        assertEquals(retMatched._2.bytes(), "some bytes".getBytes());
    }
    
}

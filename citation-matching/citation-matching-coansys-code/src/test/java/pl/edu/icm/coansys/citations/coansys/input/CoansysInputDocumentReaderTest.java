package pl.edu.icm.coansys.citations.coansys.input;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class CoansysInputDocumentReaderTest {

    
    @InjectMocks
    private CoansysInputDocumentReader coansysInputDocumentReader = new CoansysInputDocumentReader();
    
    @Mock
    private BytesWritableConverter bytesWritableConverter;
    
    @Mock
    private JavaSparkContext sparkContext;
    
    @Mock
    private JavaPairRDD<Writable, BytesWritable> rawDocuments;
    
    @Mock
    private JavaPairRDD<String, DocumentWrapper> docWrappers;
    
    @Mock
    private Tuple2<Writable, BytesWritable> bw;
   
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<Writable, BytesWritable>, String, DocumentWrapper>> convertToDocumentWrapperTuple2Function;
    
   
    @BeforeTest
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void readDocuments_empty() {
        
        // execute
        
        coansysInputDocumentReader.readDocuments(" ", 12);
    }
    
    
    @Test
    public void readDocuments() throws Exception {
        
        // given
        
        String inputDocumentPath = "/path";

        when(sparkContext.sequenceFile(inputDocumentPath, Writable.class, BytesWritable.class, 10)).thenReturn(rawDocuments);
        doReturn(docWrappers).when(rawDocuments).mapToPair(Mockito.any());
        
        
        // execute
        
        JavaPairRDD<String, DocumentWrapper> retDocWrappers = coansysInputDocumentReader.readDocuments(inputDocumentPath, 10);
        
        
        // assert
        
        assertTrue(docWrappers == retDocWrappers);
        
        verify(sparkContext).sequenceFile(inputDocumentPath, Writable.class, BytesWritable.class, 10);
        
        verify(rawDocuments).mapToPair(convertToDocumentWrapperTuple2Function.capture());
        assertConvertToDocumentWrapperTuple2Function(convertToDocumentWrapperTuple2Function.getValue());
        
    }
    

    
    //------------------------ PRIVATE --------------------------
    
    private void assertConvertToDocumentWrapperTuple2Function(PairFunction<Tuple2<Writable, BytesWritable>, String, DocumentWrapper> function) throws Exception {
        
        // given
        
        BytesWritable bytesWritable = mock(BytesWritable.class);
        Mockito.when(bw._2()).thenReturn(bytesWritable);
        
        DocumentWrapper docWrapper = DocumentWrapper.newBuilder().setRowId("111").build();
        
        Tuple2<String, DocumentWrapper> docWrapperTuple2 = new Tuple2<>("KEY", docWrapper);
        when(bytesWritableConverter.convertToDocumentWrapperTuple2(bytesWritable)).thenReturn(docWrapperTuple2);
        
        
        // execute
        
        Tuple2<String, DocumentWrapper> retDocWrapperTuple2 = function.call(bw);
        
        
        // assert
        
        assertTrue(docWrapperTuple2 == retDocWrapperTuple2);
        
        verify(bytesWritableConverter).convertToDocumentWrapperTuple2(bytesWritable);
        
        
    }
    
}

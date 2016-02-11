package pl.edu.icm.coansys.citations.coansys.input;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class CoansysInputCitationReaderTest {

    
    @InjectMocks
    private CoansysInputCitationReader coansysInputCitationReader = new CoansysInputCitationReader();
    
    private int maxSupportedCitationLength = 2000;
    
    @Mock
    private JavaSparkContext sparkContext;
    
    @Mock
    private BytesWritableConverter bytesWritableConverter;

    @Mock
    private ReferenceExtractor inputReferenceExtractor;

    @Mock
    private JavaPairRDD<Writable, BytesWritable> rawCitations;
    
    @Mock
    private JavaRDD<DocumentWrapper> docWrappers;
    
    @Mock
    private JavaPairRDD<String, ReferenceMetadata> docReferences;
    
    @Mock
    private Tuple2<Writable, BytesWritable> bw;
    
    @Mock
    private List<Tuple2<String, ReferenceMetadata>> references;
    
    @Captor
    private ArgumentCaptor<Function<Tuple2<Writable, BytesWritable>, DocumentWrapper>> convertMatchedCitationFunction;
    
    @Captor
    private ArgumentCaptor<PairFlatMapFunction<DocumentWrapper, String, ReferenceMetadata>> extractReferencesFunction;

    
    
    @BeforeTest
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        coansysInputCitationReader.setMaxSupportedCitationLength(maxSupportedCitationLength);
    }
    
    
    @Test
    public void readCitations() throws Exception {
        
        // given
        
        String inputPath = "/this/is/a/path";
        
        
        when(sparkContext.sequenceFile(inputPath, Writable.class, BytesWritable.class, 10)).thenReturn(rawCitations);
        doReturn(docWrappers).when(rawCitations).map(Mockito.any());
        doReturn(docReferences).when(docWrappers).flatMapToPair(Mockito.any());
        
        // execute
        
        JavaPairRDD<String, ReferenceMetadata> citReferences = coansysInputCitationReader.readCitations(sparkContext, inputPath, 10);
        
        
        // assert
        
        assertTrue(docReferences == citReferences);
        
        verify(sparkContext).sequenceFile(inputPath, Writable.class, BytesWritable.class, 10);
        
        verify(rawCitations).map(convertMatchedCitationFunction.capture());
        assertConvertToDocumentWrapperFunction(convertMatchedCitationFunction.getValue());
        
        verify(docWrappers).flatMapToPair(extractReferencesFunction.capture());
        assertExtractReferencesFunction(extractReferencesFunction.getValue());
        
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void assertConvertToDocumentWrapperFunction(Function<Tuple2<Writable, BytesWritable>, DocumentWrapper> function) throws Exception {
        
        BytesWritable bytesWritable = mock(BytesWritable.class);
        Mockito.when(bw._2()).thenReturn(bytesWritable);
        
        DocumentWrapper docWrapper = DocumentWrapper.newBuilder().setRowId("111").build();
        
        when(bytesWritableConverter.convertToDocumentWrapper(bytesWritable)).thenReturn(docWrapper);
        
        DocumentWrapper retDocWrapper = function.call(bw);
        
        assertTrue(docWrapper == retDocWrapper);
        
        verify(bytesWritableConverter).convertToDocumentWrapper(bytesWritable);
        
        
    }
    
    
    private void assertExtractReferencesFunction(PairFlatMapFunction<DocumentWrapper, String, ReferenceMetadata> function) throws Exception {
        
        DocumentWrapper docWrapper = DocumentWrapper.newBuilder().setRowId("111").build();
        
        when(inputReferenceExtractor.extractReferences(docWrapper, maxSupportedCitationLength)).thenReturn(references);
        
        Iterable<Tuple2<String, ReferenceMetadata>> retReferences = function.call(docWrapper);
        
        assertTrue(retReferences == references);
        
        verify(inputReferenceExtractor).extractReferences(docWrapper, maxSupportedCitationLength);
    }
    
    
}

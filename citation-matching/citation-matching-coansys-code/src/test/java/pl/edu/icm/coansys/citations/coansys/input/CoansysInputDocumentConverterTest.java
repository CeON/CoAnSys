package pl.edu.icm.coansys.citations.coansys.input;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.converters.DocumentMetadataToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class CoansysInputDocumentConverterTest {

    
    @InjectMocks
    private CoansysInputDocumentConverter coansysInputDocumentConverter = new CoansysInputDocumentConverter();
    
    @Mock
    private DocumentMetadataToEntityConverter documentToMatchableEntityConverter;
    
    @Mock
    private JavaPairRDD<String, DocumentWrapper> inputDocuments;
    
    @Mock
    private JavaPairRDD<String, MatchableEntity> matchableEntities;
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<String, DocumentWrapper>, String, MatchableEntity>> convertToMatchableEntityFunction;
    
    
    @Mock
    private Tuple2<String, DocumentWrapper> docWrapperTuple2;
    
    @Mock
    private Tuple2<String, MatchableEntity> entityTuple2;
    
    
    @BeforeTest
    public void beforeTest() {
        
        MockitoAnnotations.initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertDocuments_null() {
        
        // execute
        coansysInputDocumentConverter.convertDocuments(null);
        
    }
    
    
    @Test
    public void convertDocuments() throws Exception {

        // given
        
        Mockito.doReturn(matchableEntities).when(inputDocuments).mapToPair(Mockito.any());
        
        
        // execute
        
        JavaPairRDD<String, MatchableEntity> retEntities = coansysInputDocumentConverter.convertDocuments(inputDocuments);

        
        // assert
        
        assertTrue(retEntities == matchableEntities);
        
        verify(inputDocuments).mapToPair(convertToMatchableEntityFunction.capture());
        assertConvertToMatchableEntityFunction(convertToMatchableEntityFunction.getValue());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertConvertToMatchableEntityFunction(PairFunction<Tuple2<String, DocumentWrapper>, String, MatchableEntity> function) throws Exception {

        // given
        
        MatchableEntity matchableEntity = mock(MatchableEntity.class);
        
        BasicMetadata basicMetadata = BasicMetadata.newBuilder().build();
        DocumentMetadata docMetadata = DocumentMetadata.newBuilder().setKey("111").setBasicMetadata(basicMetadata).build();
        DocumentWrapper docWrapper = DocumentWrapper.newBuilder().setRowId("233").setDocumentMetadata(docMetadata).build();
                
        when(docWrapperTuple2._2()).thenReturn(docWrapper);
        
        when(documentToMatchableEntityConverter.convert(docMetadata)).thenReturn(matchableEntity);
        
        when(matchableEntity.id()).thenReturn("XZC");
        
        
        // execute
        
        Tuple2<String, MatchableEntity> retEntityTuple2 = function.call(docWrapperTuple2);
        
        
        // assert
        
        assertTrue(retEntityTuple2._2() == matchableEntity);
        assertEquals(retEntityTuple2._1(), "XZC");
        
        
        
    }
    
}

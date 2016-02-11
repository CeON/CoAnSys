package pl.edu.icm.coansys.citations.coansys.input;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.converters.RawReferenceToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class CoansysInputCitationConverterTest {

    @InjectMocks
    private CoansysInputCitationConverter coansysInputCitationConverter = new CoansysInputCitationConverter();
    
    @Mock
    private RawReferenceToEntityConverterFactory rawReferenceToEntityConverterFactory;
    
    @Mock
    private ReferenceMetadataConverter referenceMetadataConverter;
    
    @Mock
    private RawReferenceToEntityConverter rawReferenceToEntityConverter;
    
    
    @Mock
    private JavaPairRDD<String, ReferenceMetadata> references;
    
    @Mock
    private JavaPairRDD<String, MatchableEntity> citations;
    
    @Mock
    private Iterator<Tuple2<String, ReferenceMetadata>> docIdReferenceIterator;
    
    @Mock
    List<Tuple2<String,MatchableEntity>> docIdMatchableEntities;
    
    @Captor
    private ArgumentCaptor<PairFlatMapFunction<Iterator<Tuple2<String, ReferenceMetadata>>, String, MatchableEntity>> convertToMatchableEntitiesFunction;

    
    @BeforeTest
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    
    @Test
    public void convertCitations() throws Exception {
        
        // given
        
        
        
        doReturn(citations).when(references).mapPartitionsToPair(Mockito.any());
        
        // execute
        
        JavaPairRDD<String, MatchableEntity> convertedCitations = coansysInputCitationConverter.convertCitations(references);
        
        
        // assert
        
        assertTrue(citations == convertedCitations);
        
        verify(references).mapPartitionsToPair(convertToMatchableEntitiesFunction.capture());
        assertConvertToMatchableEntitiesFunction(convertToMatchableEntitiesFunction.getValue());
        
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void assertConvertToMatchableEntitiesFunction(PairFlatMapFunction<Iterator<Tuple2<String, ReferenceMetadata>>, String, MatchableEntity> function) throws Exception {
        
        
        // given 
        
        RawReferenceToEntityConverter rawReferenceToEntityConverter = mock(RawReferenceToEntityConverter.class);
        
        when(rawReferenceToEntityConverterFactory.createRawReferenceToEntityConverter()).thenReturn(rawReferenceToEntityConverter);
        
        when(referenceMetadataConverter.convertToMatchableEntities(docIdReferenceIterator)).thenReturn(docIdMatchableEntities);
        
        
        // execute
        
        Iterable<Tuple2<String,MatchableEntity>> retDocIdMatchableEntities = function.call(docIdReferenceIterator);
        
        
        // assert
        
        assertTrue(retDocIdMatchableEntities == docIdMatchableEntities);
        
        verify(referenceMetadataConverter).setRawReferenceToEntityConverter(rawReferenceToEntityConverter);
 
        
        
    }
    
    
   
    
}

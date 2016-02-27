package pl.edu.icm.coansys.citations.coansys.output;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
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

import pl.edu.icm.coansys.citations.data.IdWithSimilarity;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.coansys.models.PICProtos.Reference;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class CoansysOutputConverterTest {
    
    @InjectMocks
    private CoansysOutputConverter coansysOutputConverter = new CoansysOutputConverter(); 

    @Mock
    private MatchableEntityWithSimilarityConverter matchableEntityWithSimilarityConverter;
       
    @Mock
    private ReferenceToPicOutConverter referenceToPicOutConverter;
    
    @Mock
    private JavaPairRDD<MatchableEntity, IdWithSimilarity> matchedCitations;
    
    @Mock
    private JavaPairRDD<String, Reference> matchedReferences;
    
    @Mock
    private JavaPairRDD<String,Iterable<Reference>> docIdReferences;

    @Mock
    private JavaPairRDD<String, PicOut> docIdWithPicOut;
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<MatchableEntity, IdWithSimilarity>, String, Reference>> convertToReferenceFunction;
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<String, Iterable<Reference>>, String, PicOut>> convertToPicOutFunction;
    
    @Mock
    private Tuple2<MatchableEntity, IdWithSimilarity> entityTuple2;
    
    @Mock
    private Tuple2<String, Reference> docIdReferenceTuple2;
    
    @Mock
    private Tuple2<String, Iterable<Reference>> docIdReferenceIterableTuple2;
    
    @Mock
    private Tuple2<String, PicOut> docIdPicOutTuple2;
    
    
    @BeforeTest
    public void beforeTest() {
        
        MockitoAnnotations.initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertMatchedCitations_NULL() {
        
        // execute
        
        coansysOutputConverter.convertMatchedCitations(null);
        
    }
    
    @Test
    public void convertMatchedCitations() throws Exception {
        
        // given
        
        doReturn(matchedReferences).when(matchedCitations).mapToPair(Mockito.any());
        doReturn(docIdReferences).when(matchedReferences).groupByKey();
        doReturn(docIdWithPicOut).when(docIdReferences).mapToPair(Mockito.any());
        
        
        // execute
        
        JavaPairRDD<String, PicOut> retDocIdWithPicOut = coansysOutputConverter.convertMatchedCitations(matchedCitations);
        
        
        // assert
        
        assertTrue(docIdWithPicOut == retDocIdWithPicOut);
        
        verify(matchedCitations).mapToPair(convertToReferenceFunction.capture());
        assertConvertToReferenceFunction(convertToReferenceFunction.getValue());
        
        verify(docIdReferences).mapToPair(convertToPicOutFunction.capture());
        assertConvertToPicOutFunction(convertToPicOutFunction.getValue());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    
    private void assertConvertToReferenceFunction(PairFunction<Tuple2<MatchableEntity, IdWithSimilarity>, String, Reference> function) throws Exception {
    
        // given
        
        doReturn(docIdReferenceTuple2).when(matchableEntityWithSimilarityConverter).convertToReference(entityTuple2);
        
        // execute
        
        Tuple2<String, Reference> retDocIdReferenceTuple2 = function.call(entityTuple2);
        
        // assert
        
        assertTrue(retDocIdReferenceTuple2 == docIdReferenceTuple2);
        verify(matchableEntityWithSimilarityConverter).convertToReference(entityTuple2);
        
    }

    
    private void assertConvertToPicOutFunction(PairFunction<Tuple2<String, Iterable<Reference>>, String, PicOut> function) throws Exception {
        
        // given
        
        doReturn(docIdPicOutTuple2).when(referenceToPicOutConverter).convertToPicOut(docIdReferenceIterableTuple2);
        
        // execute
        
        Tuple2<String, PicOut> retDocIdPicOutTuple2 = function.call(docIdReferenceIterableTuple2);
        
        // assert
        
        assertTrue(retDocIdPicOutTuple2 == docIdPicOutTuple2);
        verify(referenceToPicOutConverter).convertToPicOut(docIdReferenceIterableTuple2);
        
    }

     
}

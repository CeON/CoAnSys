package pl.edu.icm.coansys.citations.coansys.output;

import static org.testng.Assert.assertEquals;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.citations.data.CitationMatchingProtos.MatchableEntityData;
import pl.edu.icm.coansys.citations.data.IdWithSimilarity;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.PICProtos.Reference;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class MatchableEntityWithSimilarityConverterTest {

    
    private MatchableEntityWithSimilarityConverter matchableEntityWithSimilarityConverter = new MatchableEntityWithSimilarityConverter();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToReference_NULL() {
        
        // execute
        
        matchableEntityWithSimilarityConverter.convertToReference(null);
        
    }
    
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToReference_NULL_idWithSimilarity() {
        
        // given
        
        MatchableEntity entity = Mockito.mock(MatchableEntity.class);
        Tuple2<MatchableEntity, IdWithSimilarity> entityWithId = new Tuple2<>(entity, null);
        
        // execute
        
        matchableEntityWithSimilarityConverter.convertToReference(entityWithId);
        
    }
    
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToReference_NULL_matchableEntity() {
        
        // given
        
        IdWithSimilarity idWithSimilarity = Mockito.mock(IdWithSimilarity.class);
        Tuple2<MatchableEntity, IdWithSimilarity> entityWithId = new Tuple2<>(null, idWithSimilarity);
        
        // execute
        
        matchableEntityWithSimilarityConverter.convertToReference(entityWithId);
        
    }
    
    
    @Test
    public void convertToReference() {
        
        // given
        
        String destDocId = "doc_ABC";
        IdWithSimilarity idWithSimilarity = new IdWithSimilarity(destDocId, 13.4d);
        
        MatchableEntityData entityData = MatchableEntityData
                                                .newBuilder()
                                                .setRawText("RAW_TEXT")
                                                .setId("cit_QW1233_12")
                                                .build();
        MatchableEntity entity = new MatchableEntity(entityData);
        
        Tuple2<MatchableEntity, IdWithSimilarity> entityWithId = new Tuple2<>(entity, idWithSimilarity);
        
        
        // execute
        
        Tuple2<String, Reference> docIdReference = matchableEntityWithSimilarityConverter.convertToReference(entityWithId);
        
        
        // assert
        
        assertEquals(docIdReference._1(), "QW1233");
        assertEquals(docIdReference._2().getDocId(), "ABC");
        assertEquals(docIdReference._2().getRawText(), "RAW_TEXT");
        assertEquals(docIdReference._2().getRefNum(), 12);
    }
    
    
}

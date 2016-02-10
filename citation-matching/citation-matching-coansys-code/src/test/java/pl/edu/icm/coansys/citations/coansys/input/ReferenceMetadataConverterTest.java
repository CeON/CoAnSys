package pl.edu.icm.coansys.citations.coansys.input;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static pl.edu.icm.coansys.citations.coansys.input.TestReferenceFactory.createReference;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.converters.RawReferenceToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class ReferenceMetadataConverterTest {

    @InjectMocks
    private ReferenceMetadataConverter referenceMetadataConverter = new ReferenceMetadataConverter();
    
    @Mock
    private RawReferenceToEntityConverter rawReferenceToEntityConverter;
    
    
    @BeforeTest
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convertToMatchableEntities() {
        
        // given
        
        ReferenceMetadata ref1 = createReference("XXX");
        ReferenceMetadata ref2 = createReference("YYY");
        
        String doc1 = "DOC!";
        Tuple2<String, ReferenceMetadata> refTuple1 = new Tuple2<>(doc1, ref1);
        Tuple2<String, ReferenceMetadata> refTuple2 = new Tuple2<>(doc1, ref2);
        
        List<Tuple2<String, ReferenceMetadata>> references = Lists.newArrayList();
        references.add(refTuple1);
        references.add(refTuple2);
        
        
        MatchableEntity entity1 = Mockito.mock(MatchableEntity.class);
        when(entity1.id()).thenReturn("1");
        
        MatchableEntity entity2 = Mockito.mock(MatchableEntity.class);
        when(entity2.id()).thenReturn("2");
        
        when(rawReferenceToEntityConverter.convert(new CitEntityId(doc1, ref1.getPosition()), ref1.getRawCitationText())).thenReturn(entity1);
        when(rawReferenceToEntityConverter.convert(new CitEntityId(doc1, ref2.getPosition()), ref2.getRawCitationText())).thenReturn(entity2);
        
        
        // execute
        
        List<Tuple2<String, MatchableEntity>> docIdEntities = referenceMetadataConverter.convertToMatchableEntities(references.iterator());
        
        // assert
        
        assertEquals(2, docIdEntities.size());
        assertTrue(docIdEntities.get(0)._2() == entity1);
        assertEquals(docIdEntities.get(0)._1(), "1");
        assertTrue(docIdEntities.get(1)._2() == entity2);
        assertEquals(docIdEntities.get(1)._1(), "2");
    }
    
    

 
}

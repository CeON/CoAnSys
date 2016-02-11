package pl.edu.icm.coansys.citations.coansys.input;

import static org.testng.Assert.assertEquals;
import static pl.edu.icm.coansys.citations.coansys.input.TestReferenceFactory.createReference;

import java.util.List;

import org.testng.annotations.Test;

import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class ReferenceExtractorTest {

    
    private ReferenceExtractor referenceExtractor = new ReferenceExtractor();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractReferences() {
        
        // given
        
        BasicMetadata basicMetadata = BasicMetadata.newBuilder().build();
        
        ReferenceMetadata ref1 = createReference("Raw citation");
        ReferenceMetadata ref2 = createReference("");
        ReferenceMetadata ref3 = createReference("Raw citation number 3");
        ReferenceMetadata ref4 = createReference("Raw citation 4");
        
        
        DocumentMetadata docMetadata = DocumentMetadata.newBuilder()
                                                .setKey("KEY")
                                                .setBasicMetadata(basicMetadata)
                                                .addReference(ref1).addReference(ref2).addReference(ref3).addReference(ref4)
                                                .build();
        
        DocumentWrapper docWrapper = DocumentWrapper.newBuilder().setRowId("ROW_ID").setDocumentMetadata(docMetadata).build();
        
            
        // execute
        
        List<Tuple2<String, ReferenceMetadata>> docIdReferences = referenceExtractor.extractReferences(docWrapper, 20);
        

        // assert
        
        assertEquals(2,  docIdReferences.size());
        
        assertEquals(docMetadata.getKey(), docIdReferences.get(0)._1());
        assertEquals(docMetadata.getKey(), docIdReferences.get(1)._1());
        
        assertEquals(ref1, docIdReferences.get(0)._2());
        assertEquals(ref4, docIdReferences.get(1)._2());
        
    }


        
}

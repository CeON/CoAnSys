package pl.edu.icm.coansys.citations.coansys.input;

import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;

/**
* @author Łukasz Dumiszewski
*/

public class TestReferenceFactory {

    
    
    public static ReferenceMetadata createReference(String rawCitationText) {

        BasicMetadata basicMetadata = BasicMetadata.newBuilder().build();
        return ReferenceMetadata.newBuilder().setBasicMetadata(basicMetadata).setRawCitationText(rawCitationText).build();
    
    }
    
}

package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
 * 
 * Extractor of {@link ReferenceMetadata} objects from {@link DocumentWrapper}
 * 
 * @author Łukasz Dumiszewski
*/

class ReferenceExtractor implements Serializable {

    
    private static final long serialVersionUID = 1L;

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Extracts {@link ReferenceMetadata} objects from {@link DocumentWrapper}. Only takes into account these references  that
     * have the {@link ReferenceMetadata#getRawCitationText()} not blank and shorter or equal to maxSupportedCitationLength.
     * @return pairs of ({@link DocumentMetadata#getKey()}, {@link ReferenceMetadata})
     */
    public List<Tuple2<String, ReferenceMetadata>> extractReferences(DocumentWrapper docWrapper, int maxSupportedCitationLength) {
        
        List<ReferenceMetadata> docReferences = docWrapper.getDocumentMetadata().getReferenceList();
        
        return docReferences.stream()
                .filter(r->StringUtils.isNotBlank(r.getRawCitationText()) && r.getRawCitationText().length() <= maxSupportedCitationLength)
                .map(r->new Tuple2<String, ReferenceMetadata>(docWrapper.getDocumentMetadata().getKey(), r))
                .collect(Collectors.toList());
    
    }
}

package pl.edu.icm.coansys.citations.coansys.output;

import com.google.common.base.Preconditions;

import pl.edu.icm.coansys.citations.data.IdWithSimilarity;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId;
import pl.edu.icm.coansys.citations.data.entity_id.DocEntityId;
import pl.edu.icm.coansys.models.PICProtos.Reference;
import scala.Tuple2;

/**
 *
 * Converter of pairs of ({@link MatchableEntity}, {@link IdWithSimilarity}) to pairs of (sourceDocumentId, {@link Reference})
 * 
 * @author Łukasz Dumiszewski
*/

class MatchableEntityWithSimilarityConverter {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given pair of ({@link MatchableEntity}, {@link IdWithSimilarity}) to the pair of (sourceDocumentId, {@link Reference})
    */
    public Tuple2<String, Reference> convertToReference(Tuple2<MatchableEntity, IdWithSimilarity> entityWithId) {
        
        Preconditions.checkNotNull(entityWithId);
        Preconditions.checkNotNull(entityWithId._1());
        Preconditions.checkNotNull(entityWithId._2());
        
        MatchableEntity citation = entityWithId._1();
        
        CitEntityId citEntityId = CitEntityId.fromString(citation.id());
        DocEntityId destDocId = DocEntityId.fromString(entityWithId._2().getId());
        
        Reference.Builder referenceB = Reference.newBuilder();
        referenceB.setDocId(destDocId.documentId());
        referenceB.setRefNum(citEntityId.position());
        
        if (citation.rawText().isDefined()) {
            referenceB.setRawText(citation.rawText().get());
        }
        
        return new Tuple2<>(citEntityId.sourceDocumentId(), referenceB.build());

    }
    
    
}

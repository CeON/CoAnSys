package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.converters.RawReferenceToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.entity_id.CitEntityId;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;
import scala.Tuple2;

/**
 * 
 * Converter of ({@link DocumentMetadata#getKey}, {@link ReferenceMetata}) pairs to (docId, {@link MatchableEntity}) pairs
 * 
* @author Łukasz Dumiszewski
*/

class ReferenceMetadataConverter implements Serializable {

    
    private static final long serialVersionUID = 1L;
    
    transient private RawReferenceToEntityConverter rawReferenceToEntityConverter;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts ({@link DocumentMetadata#getKey}, {@link ReferenceMetata}) pairs to (docId, {@link MatchableEntity}) pairs.
     * {@link MatchableEntity} is created from {@link ReferenceMetadata#getRawCitationText()} by using {@link #setRawReferenceToEntityConverter(RawReferenceToEntityConverter)}
     * docId = cit_srcDocId_position.
    */
    public List<Tuple2<String, MatchableEntity>> convertToMatchableEntities(Iterator<Tuple2<String, ReferenceMetadata>> docIdReferenceIterator) {
        
        Preconditions.checkNotNull(docIdReferenceIterator);
        
        List<Tuple2<String, MatchableEntity>> matchableEntities = Lists.newArrayList();
        
        while (docIdReferenceIterator.hasNext()) {
            Tuple2<String, ReferenceMetadata> docIdReference = docIdReferenceIterator.next();
            MatchableEntity matchableEntity = rawReferenceToEntityConverter.convert(new CitEntityId(docIdReference._1(), docIdReference._2().getPosition()), docIdReference._2().getRawCitationText());
            matchableEntities.add(new Tuple2<>(matchableEntity.id(), matchableEntity));
        }
        
        return matchableEntities;
        
    }
    
    
    
    //------------------------ SETTERS --------------------------

    public void setRawReferenceToEntityConverter(RawReferenceToEntityConverter rawReferenceToEntityConverter) {
        this.rawReferenceToEntityConverter = rawReferenceToEntityConverter;
    }

}

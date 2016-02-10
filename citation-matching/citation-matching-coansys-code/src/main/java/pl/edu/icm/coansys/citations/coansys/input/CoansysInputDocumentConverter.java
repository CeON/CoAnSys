package pl.edu.icm.coansys.citations.coansys.input;

import org.apache.spark.api.java.JavaPairRDD;

import com.google.common.base.Preconditions;

import pl.edu.icm.coansys.citations.InputDocumentConverter;
import pl.edu.icm.coansys.citations.converters.DocumentMetadataToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
 * Coansys implementation of {@link InputDocumentConverter}
 * 
* @author Łukasz Dumiszewski
*/

public class CoansysInputDocumentConverter implements InputDocumentConverter<String, DocumentWrapper> {

    
    private DocumentMetadataToEntityConverter documentToMatchableEntityConverter = new DocumentMetadataToEntityConverter();
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts pairs of ({@link DocumentMetadata#getKey()}, {@link DocumentWrapper}) to pairs of (doc_{@link DocumentMetadata#getKey()}, {@link MatchableEntity})
     */
    @Override
    public JavaPairRDD<String, MatchableEntity> convertDocuments(JavaPairRDD<String, DocumentWrapper> inputDocuments) {
        
        Preconditions.checkNotNull(inputDocuments);
        
        return inputDocuments.mapToPair(doc-> convertToMatchableEntity(doc._2()));
    
    }
    
    
    
    
    //------------------------ PRIVATE --------------------------
    
    private Tuple2<String, MatchableEntity> convertToMatchableEntity(DocumentWrapper docWrapper) {
        
        MatchableEntity matchableEntity = documentToMatchableEntityConverter.convert(docWrapper.getDocumentMetadata());
        
        return new Tuple2<String, MatchableEntity>(matchableEntity.id(), matchableEntity);
    
    }

    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setDocumentToMatchableEntityConverter(DocumentMetadataToEntityConverter documentToMatchableEntityConverter) {
        
        this.documentToMatchableEntityConverter = documentToMatchableEntityConverter;
    
    }


}

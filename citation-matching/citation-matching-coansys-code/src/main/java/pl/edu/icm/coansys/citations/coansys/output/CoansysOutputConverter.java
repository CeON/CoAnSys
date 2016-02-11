package pl.edu.icm.coansys.citations.coansys.output;

import java.io.Serializable;

import org.apache.spark.api.java.JavaPairRDD;

import com.google.common.base.Preconditions;

import pl.edu.icm.coansys.citations.OutputConverter;
import pl.edu.icm.coansys.citations.data.IdWithSimilarity;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.coansys.models.PICProtos.Reference;

/**
 * 
 * Coansys implementation of {@link OutputConverter}
 * 
 * @author Łukasz Dumiszewski
*/

public class CoansysOutputConverter implements OutputConverter<String, PicOut>, Serializable {

    private static final long serialVersionUID = 1L;

    private MatchableEntityWithSimilarityConverter matchableEntityWithSimilarityConverter = new MatchableEntityWithSimilarityConverter();
    
    private ReferenceToPicOutConverter referenceToPicOutConverter = new ReferenceToPicOutConverter();
    
    
    
    //------------------------ LOGIC --------------------------
            
            
    /**
     * Converts pairs of ({@link MatchableEntity}, {@link IdWithSimilarity}) to pairs of (srcDocumentId, {@link PicOut})
     */
    @Override
    public JavaPairRDD<String, PicOut> convertMatchedCitations(JavaPairRDD<MatchableEntity, IdWithSimilarity> matchedCitations) {
        
        Preconditions.checkNotNull(matchedCitations);
        
        JavaPairRDD<String, Reference> matchedReferences = matchedCitations.mapToPair(entityWithId -> matchableEntityWithSimilarityConverter.convertToReference(entityWithId));
        
        JavaPairRDD<String, PicOut> docIdWithPicOut = matchedReferences.groupByKey().mapToPair(srcDocIdReferences -> referenceToPicOutConverter.convertToPicOut(srcDocIdReferences));
        
        return docIdWithPicOut;
    }



    //------------------------ SETTERS --------------------------
    
    public void setMatchableEntityWithSimilarityConverter(MatchableEntityWithSimilarityConverter matchableEntityWithSimilarityConverter) {
        this.matchableEntityWithSimilarityConverter = matchableEntityWithSimilarityConverter;
    }

    public void setReferenceToPicOutConverter(ReferenceToPicOutConverter referenceToPicOutConverter) {
        this.referenceToPicOutConverter = referenceToPicOutConverter;
    }
}

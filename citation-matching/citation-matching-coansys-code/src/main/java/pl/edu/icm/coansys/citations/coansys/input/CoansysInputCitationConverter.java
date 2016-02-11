package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;

import org.apache.spark.api.java.JavaPairRDD;

import pl.edu.icm.coansys.citations.InputCitationConverter;
import pl.edu.icm.coansys.citations.converters.RawReferenceToEntityConverter;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;

/**
 * Converter of JavaPairRDD&lt;String, ReferenceMetadata&gt; to JavaPairRDD&lt;String, MatchableEntity&gt;
 * 
* @author Łukasz Dumiszewski
*/

public class CoansysInputCitationConverter implements InputCitationConverter<String, ReferenceMetadata>, Serializable {

    
    private static final long serialVersionUID = 1L;

    private RawReferenceToEntityConverterFactory rawReferenceToEntityConverterFactory = new RawReferenceToEntityConverterFactory();
    
    private ReferenceMetadataConverter referenceMetadataConverter = new ReferenceMetadataConverter();
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given (source_document_id, reference) pairs into (citation_id, citation) pairs 
     */
    @Override
    public JavaPairRDD<String, MatchableEntity> convertCitations(JavaPairRDD<String, ReferenceMetadata> inputCitations) {

        return inputCitations.mapPartitionsToPair(docIdReferenceIterator -> {
            
            RawReferenceToEntityConverter rawReferenceToEntityConverter = rawReferenceToEntityConverterFactory.createRawReferenceToEntityConverter();
            
            referenceMetadataConverter.setRawReferenceToEntityConverter(rawReferenceToEntityConverter);
            
            return referenceMetadataConverter.convertToMatchableEntities(docIdReferenceIterator);
            
        });
    }
    
        
    //------------------------ SETTERS --------------------------

    public void setRawReferenceToEntityConverterFactory(RawReferenceToEntityConverterFactory rawReferenceToEntityConverterFactory) {
        this.rawReferenceToEntityConverterFactory = rawReferenceToEntityConverterFactory;
    }


    public void setReferenceMetadataConverter(ReferenceMetadataConverter referenceMetadataConverter) {
        this.referenceMetadataConverter = referenceMetadataConverter;
    }




}

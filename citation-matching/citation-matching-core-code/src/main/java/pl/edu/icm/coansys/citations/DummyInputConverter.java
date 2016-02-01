package pl.edu.icm.coansys.citations;

import org.apache.spark.api.java.JavaPairRDD;

import pl.edu.icm.coansys.citations.data.MatchableEntity;

/**
 * Dummy converter of input documents and citations.
 * It has empty implementation of converting citations and documents.
 * 
 * @author madryk
 *
 */
public class DummyInputConverter implements InputDocumentConverter<String, MatchableEntity>, InputCitationConverter<String, MatchableEntity> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public JavaPairRDD<String, MatchableEntity> convertCitations(JavaPairRDD<String, MatchableEntity> inputCitations) {
        return inputCitations;
    }

    @Override
    public JavaPairRDD<String, MatchableEntity> convertDocuments(JavaPairRDD<String, MatchableEntity> inputDocuments) {
        return inputDocuments;
    }

}

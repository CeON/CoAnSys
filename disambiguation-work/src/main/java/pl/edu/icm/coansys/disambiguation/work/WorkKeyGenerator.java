package pl.edu.icm.coansys.disambiguation.work;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;


/**
 * A work deduplication map/reduce phase key generator
 * @author Łukasz Dumiszewski
 *
 */
abstract class WorkKeyGenerator {
    
    private static final int KEY_PART_LENGTH = 5;
    
    
    private WorkKeyGenerator() {
        throw new IllegalStateException("not to instantiate");
    }
    
    
    /**
     * Generates key for the given {@link DocumentWrapper}
     * @param level influences the keyLength, the keyLength is a multiplication of the level and {@link #KEY_PART_LENGTH} 
     */
    public static String generateKey(DocumentWrapper doc, int level) {
        String docKey = DocumentWrapperUtils.getMainTitle(doc);
        docKey = StringTools.normalize(docKey);
        docKey = StringTools.removeStopWords(docKey);
        docKey = docKey.replaceAll("\\s", "");
        
        int keyLength = level*KEY_PART_LENGTH+KEY_PART_LENGTH;
        if (docKey.length() > keyLength) {
            docKey = docKey.substring(0, keyLength);
        }
        return docKey;
    }
}
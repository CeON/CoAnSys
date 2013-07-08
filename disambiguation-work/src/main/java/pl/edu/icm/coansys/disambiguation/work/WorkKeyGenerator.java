package pl.edu.icm.coansys.disambiguation.work;

import pl.edu.icm.coansys.disambiguation.work.tool.StringTools;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;


/**
 * A utility class for a work deduplication map/reduce key generator
 * @author Łukasz Dumiszewski
 *
 */
abstract class WorkKeyGenerator {
    
    private static int KEY_PART_LENGTH = 5;
    
    
    private WorkKeyGenerator() {
        throw new IllegalStateException("not to instantiate");
    }
    
    
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
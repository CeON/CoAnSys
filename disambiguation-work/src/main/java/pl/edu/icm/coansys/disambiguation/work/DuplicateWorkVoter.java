package pl.edu.icm.coansys.disambiguation.work;



import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("duplicateWorkVoter")
public class DuplicateWorkVoter {

    private DuplicateWorkVoterConfiguration config;
    
    @Autowired 
    public DuplicateWorkVoter(DuplicateWorkVoterConfiguration config) {
        this.config = config;
    }
    
    
    
    /**
     * Tells whether the given documents are duplicates. The result depends on the {@link DuplicateWorkVoterConfiguration} of the voter
     */
    public boolean isDuplicate(DocumentWrapper document, DocumentWrapper other) {
        int maxDistance = config.getMaxLevenshteinDistance();
        
        String docTitle = DocumentWrapperUtils.getMainTitle(document);
        String otherTitle = DocumentWrapperUtils.getMainTitle(other);
        
        int distance = StringUtils.getLevenshteinDistance(docTitle, otherTitle);
        if (distance<maxDistance) {
            return true;
        }
        return false;
    }
}

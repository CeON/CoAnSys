package pl.edu.icm.coansys.disambiguation.work;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("duplicateWorkVoter")
public class DuplicateWorkVoter {

    public boolean isDuplicate(DocumentWrapper document, DocumentWrapper other, DuplicateWorkVoterConfiguration config) {
        int maxDistance = config.getMaxLevenshteinDistance();
        
        String docTitle = DocumentWrapperHelper.getMainTitle(document);
        String otherTitle = DocumentWrapperHelper.getMainTitle(other);
        
        int distance = StringUtils.getLevenshteinDistance(docTitle, otherTitle);
        if (distance<maxDistance) {
            return true;
        }
        return false;
    }
}

package pl.edu.icm.coansys.disambiguation.work.comparator;

import static pl.edu.icm.coansys.disambiguation.work.tool.StringTools.inLevenshteinDistance;
import static pl.edu.icm.coansys.disambiguation.work.tool.StringTools.normalize;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

@Service("workJournalComparator")
public class WorkJournalComparator {

    boolean sameJournals(DocumentWrapper doc1, DocumentWrapper doc2) {
        
        String issn1 = normalize(doc1.getDocumentMetadata().getBasicMetadata().getIssn());
        String issn2 = normalize(doc2.getDocumentMetadata().getBasicMetadata().getIssn());
        
        if (!StringUtils.isBlank(issn1) && !StringUtils.isBlank(issn2)) {
            return StringUtils.equalsIgnoreCase(issn1, issn2);
        }
        
        String journalTitle1 = normalize(doc1.getDocumentMetadata().getBasicMetadata().getJournal());
        String journalTitle2 = normalize(doc2.getDocumentMetadata().getBasicMetadata().getJournal());
        if (StringUtils.isBlank(journalTitle1) || StringUtils.isBlank(journalTitle2)) {
            return false;
        }
        
        if (inLevenshteinDistance(journalTitle1, journalTitle2, 1)) {
            return true;
        }
        
        return false;
    }

}

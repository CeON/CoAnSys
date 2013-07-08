package pl.edu.icm.coansys.disambiguation.work.comparator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.disambiguation.work.DocumentWrapperUtils;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("workYearComparator")
public class WorkYearComparator {

    public boolean sameYears(DocumentWrapper doc1, DocumentWrapper doc2) {
        String doc1year = DocumentWrapperUtils.getPublicationYear(doc1);
        String doc2year = DocumentWrapperUtils.getPublicationYear(doc2);

        // if one of the years is not known then i treat these years as equal
        if (doc1year == null || doc2year == null) {
            return true;
        }
        
        return StringUtils.equalsIgnoreCase(doc1year, doc2year);
        
    }
}

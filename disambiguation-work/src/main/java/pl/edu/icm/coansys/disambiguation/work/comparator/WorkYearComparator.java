package pl.edu.icm.coansys.disambiguation.work.comparator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.disambiguation.work.DocumentWrapperUtils;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("workYearComparator")
public class WorkYearComparator {

    private WorkYearComparatorConfiguration config;
    
    @Autowired
    public WorkYearComparator(WorkYearComparatorConfiguration config) {
        this.config = config;
    }
    
    public boolean sameYears(DocumentWrapper doc1, DocumentWrapper doc2) {
        String doc1year = DocumentWrapperUtils.getPublicationYear(doc1);
        String doc2year = DocumentWrapperUtils.getPublicationYear(doc2);

        // if one of the years is not known then treat these years as equal
        if (StringUtils.isBlank(doc1year) || StringUtils.isBlank(doc2year)) {
            return true;
        }
        
        Integer year1 = Integer.parseInt(doc1year);
        Integer year2 = Integer.parseInt(doc2year);
        
        return Math.abs(year1-year2)<=config.getPublicationYearMaxDistance();
        
    }
}

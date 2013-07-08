package pl.edu.icm.coansys.disambiguation.work;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.disambiguation.work.comparator.WorkAuthorComparator;
import pl.edu.icm.coansys.disambiguation.work.comparator.WorkTitleComparator;
import pl.edu.icm.coansys.disambiguation.work.comparator.WorkYearComparator;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;


/**
 * 
 * @author Łukasz Dumiszewski
 *
 */

@Service("duplicateWorkVoter")
public class DuplicateWorkVoter {
    
    private WorkTitleComparator workTitleComparator;
    
    private WorkAuthorComparator workAuthorComparator;
    
    private WorkYearComparator workYearComparator;
    
    
    /**
     * Tells whether the given documents are duplicates.  
     */
    public boolean isDuplicate(DocumentWrapper doc1, DocumentWrapper doc2) {
        
        
        if (!workTitleComparator.sameTitles(doc1, doc2)) {
            return false;
        }
        
        
        if (!workAuthorComparator.sameAuthors(doc1, doc2)) {
            return false;
        }
        
        
        return workYearComparator.sameYears(doc1, doc2);
    }
    


        
    //******************** SETTERS ********************
    
    @Autowired
    public void setWorkTitleComparator(WorkTitleComparator workTitleComparator) {
        this.workTitleComparator = workTitleComparator;
    }

    @Autowired
    public void setWorkAuthorComparator(WorkAuthorComparator workAuthorComparator) {
        this.workAuthorComparator = workAuthorComparator;
    }
    
    @Autowired
    public void setWorkYearComparator(WorkYearComparator workYearComparator) {
        this.workYearComparator = workYearComparator;
    }

}

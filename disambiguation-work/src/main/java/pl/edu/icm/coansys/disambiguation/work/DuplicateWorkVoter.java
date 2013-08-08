/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.work;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.disambiguation.work.comparator.WorkAuthorComparator;
import pl.edu.icm.coansys.disambiguation.work.comparator.WorkTitleComparator;
import pl.edu.icm.coansys.disambiguation.work.comparator.WorkYearComparator;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;


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

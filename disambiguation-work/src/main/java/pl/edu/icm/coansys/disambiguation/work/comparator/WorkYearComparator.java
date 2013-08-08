/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.work.comparator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

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

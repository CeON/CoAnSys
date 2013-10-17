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

package pl.edu.icm.coansys.disambiguation.work;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */

@Service("duplicateWorkService")
public class DuplicateWorkService {

    
    
    @Autowired
    private DuplicateWorkComparator duplicateWorkComparator;
     
    
    
    //******************** LOGIC ********************
    
    /**
     * Find duplicates in the passed document list. Every set of duplicates is written under a unique key in the returned map.
     * Whether 2 documents are considered duplicates is determined by {@link DuplicateWorkVoter#isDuplicate(DocumentWrapper, DocumentWrapper)} 
     * 
     * E.g. let's assume we passed to the method the documents symbolized here as:
     * AAA, BBb, bbb, AAa, aAA, ccc
     * And that:
     * AAA is duplicate of AAa and aAA, and:
     * BBb is duplicate of bbb
     * 
     * Then the result of this method will be something like this:
     * <1, <AAA, AAa, aAA>>
     * <2, <BBb, bbb>>
     * 
     * 
     */
    public Map<Integer, Set<DocumentWrapper>> findDuplicates(List<DocumentWrapper> documents) {
        Map<Integer, Set<DocumentWrapper>> sameWorksMap = Maps.newHashMap();
        
        List<DocumentWrapper> documentsCopy = Lists.newArrayList(documents);
        
        int i=0;
        for (DocumentWrapper document : documents) {
           
           for (DocumentWrapper other : new ArrayList<DocumentWrapper>(documentsCopy)) {
                if (document.getRowId().equals(other.getRowId())) {
                    documentsCopy.remove(other);
                } else {
                    if (duplicateWorkComparator.isDuplicate(document, other)) {
                        addSameWorks(sameWorksMap, i, document, other);
                        documentsCopy.remove(other);
                    }
                }
            }
           i++;
        }
        return sameWorksMap;
    }
    
    
    
    

    //******************** PRIVATE ********************
    
    private void addSameWorks(Map<Integer, Set<DocumentWrapper>> sameWorksMap, int i, DocumentWrapper document, DocumentWrapper other) {
        Set<DocumentWrapper> sameWorks = sameWorksMap.get(i);
        if (sameWorks==null) {
            sameWorks = Sets.newHashSet();
            sameWorksMap.put(i, sameWorks);
        }
        sameWorks.add(document);
        sameWorks.add(other);
    }
}

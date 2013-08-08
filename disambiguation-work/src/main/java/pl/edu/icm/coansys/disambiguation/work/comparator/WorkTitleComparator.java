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

import static pl.edu.icm.coansys.commons.java.StringTools.getTrailingInteger;
import static pl.edu.icm.coansys.commons.java.StringTools.inLevenshteinDistance;
import static pl.edu.icm.coansys.commons.java.StringTools.normalize;
import static pl.edu.icm.coansys.commons.java.StringTools.replaceLastRomanNumberToDecimal;
import static pl.edu.icm.coansys.commons.java.StringTools.replaceLastWordNumberToDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

@Service("workTitleComparator")
public class WorkTitleComparator {

    
    private WorkTitleComparatorConfiguration config;
    
    
    @Autowired 
    public WorkTitleComparator(WorkTitleComparatorConfiguration config) {
        this.config = config;
    }
    
     
    /**
     * Tells whether the titles of the passed {@link DocumentWrapper}s are the same, the result
     * depends on the {@link WorkTitleComparatorConfiguration} 
     */
    public boolean sameTitles(DocumentWrapper doc1, DocumentWrapper doc2) {
            
            String title1 = normalizeTitle(doc1);
            String title2 = normalizeTitle(doc2);
            
            
            
            if (!titlesInLevenshteinDistance(title1, title2)) {
                return false;
            }
            
            if (!titleEndsInLevenshteinDistance(title1, title2)) {
                return false;
            }
            
            
            return sameTrailingNumbers(title1, title2);
            
        }
    
    
    
    
    //******************** PRIVATE ********************
    
    private String normalizeTitle(DocumentWrapper doc1) {
        String title = normalize(DocumentWrapperUtils.getMainTitle(doc1));
        title = replaceLastRomanNumberToDecimal(title);
        title = replaceLastWordNumberToDecimal(title);
        return title;
        
    }
    
    private boolean titlesInLevenshteinDistance(String title1, String title2) {
        int maxDistance = config.getRealLevenshteinDistance(title1, title2);
        return inLevenshteinDistance(title1, title2, maxDistance);
    }
   
    
    private boolean titleEndsInLevenshteinDistance(String title1, String title2) {
        if (title1.length() > config.getTitleMostMeaningfulEndLength() &&
            title2.length() > config.getTitleMostMeaningfulEndLength()) {    
            
            String doc1TitleEnd = title1.substring(title1.length()+1-config.getTitleMostMeaningfulEndLength());
            String doc2TitleEnd = title2.substring(title2.length()+1-config.getTitleMostMeaningfulEndLength());
            
            return inLevenshteinDistance(doc1TitleEnd, doc2TitleEnd, config.getTitleEndMaxLevenshteinDistance());
        }
        return true;
    }
    
    // if the title ends with number, the numbers must be the same
    // this way the 'Alice has got a cat part 1' will not be considered the same as
    // 'Alice has got a cat part 2'
    private boolean sameTrailingNumbers(String title1, String title2) {
        
        String doc1TitleTrailingInteger = getTrailingInteger(title1);
        String doc2TitleTrailingInteger = getTrailingInteger(title2);
        
        
        if (doc1TitleTrailingInteger != null && doc2TitleTrailingInteger != null) {
            return doc1TitleTrailingInteger.equals(doc2TitleTrailingInteger);
        
        } else if (doc1TitleTrailingInteger == null && doc2TitleTrailingInteger == null) {
            return true;
        
        } else {
            return false;
        }
    }

}

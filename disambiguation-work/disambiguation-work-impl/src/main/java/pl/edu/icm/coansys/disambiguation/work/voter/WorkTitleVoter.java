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

package pl.edu.icm.coansys.disambiguation.work.voter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.commons.stringsimilarity.EditDistanceSimilarity;
import pl.edu.icm.coansys.commons.stringsimilarity.TrailingNumbersSimilarity;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 */
public class WorkTitleVoter extends AbstractSimilarityVoter {
    
    Logger log = LoggerFactory.getLogger(WorkTitleVoter.class);
    
    private float disapproveLevel = 10f;
    private float approveLevel = 19f;
    private int digitsPercentageTreshold;

    @Override
    public Vote vote(DocumentWrapper doc1, DocumentWrapper doc2) {
        String title1 = getNormalizedTitle(doc1);
        String title2 = getNormalizedTitle(doc2);
        
        float appr = this.approveLevel;
        float disappr = this.disapproveLevel;
        
        int digitsPercentage = Math.max(StringTools.digitsPercentage(title1), StringTools.digitsPercentage(title2));
        if (digitsPercentage > digitsPercentageTreshold) {
            // if the title contains many digits, we are more restrictive in comparing titles
            appr /= 4;
            disappr /= 4;
        }
        
        TrailingNumbersSimilarity trailingNumberSimilarity = new TrailingNumbersSimilarity();
        EditDistanceSimilarity editDistanceSimilarity = new EditDistanceSimilarity(appr, disappr);
        
        
        if (trailingNumberSimilarity.calculateSimilarity(title1, title2) == 0.0f) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
        
        float editDistance = editDistanceSimilarity.calculateSimilarity(title1, title2);
        if (editDistance == 0.0f) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
        
        return new Vote(Vote.VoteStatus.PROBABILITY, editDistance);
    }
    
    private String getNormalizedTitle(DocumentWrapper doc) {
        String title = DocumentWrapperUtils.getMainTitle(doc);
        title = StringTools.normalize(title);
        title = StringTools.replaceLastRomanNumberToDecimal(title);
        title = StringTools.replaceLastWordNumberToDecimal(title);
        
        return title;
    }

    public void setDisapproveLevel(float disapproveLevel) {
        this.disapproveLevel = disapproveLevel;
    }

    public void setApproveLevel(float approveLevel) {
        this.approveLevel = approveLevel;
    }

    public void setDigitsPercentageTreshold(int digitsPercentageTreshold) {
        this.digitsPercentageTreshold = digitsPercentageTreshold;
    }
}

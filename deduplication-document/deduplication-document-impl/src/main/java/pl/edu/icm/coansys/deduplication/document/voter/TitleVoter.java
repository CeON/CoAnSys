/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

package pl.edu.icm.coansys.deduplication.document.voter;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.commons.stringsimilarity.AllNumbersSimilarity;
import pl.edu.icm.coansys.commons.stringsimilarity.EditDistanceSimilarity;
import pl.edu.icm.coansys.commons.stringsimilarity.PartQualifiersSimilarity;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 */
public class TitleVoter extends AbstractSimilarityVoter {
    
    private float disapproveLevel;
    private float approveLevel;
    private int maxNormalizedTitleLength;

    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {
        String title1 = getNormalizedTitle(doc1);
        String title2 = getNormalizedTitle(doc2);
        
        AllNumbersSimilarity allNumberSimilarity = new AllNumbersSimilarity();
        if (allNumberSimilarity.calculateSimilarity(title1, title2) == 0.0f) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
        
        PartQualifiersSimilarity partQualifiersSimilarity = new PartQualifiersSimilarity();
        if (partQualifiersSimilarity.calculateSimilarity(title1, title2) == 0.0f) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
        
        EditDistanceSimilarity editDistanceSimilarity = new EditDistanceSimilarity(approveLevel, disapproveLevel, maxNormalizedTitleLength);        
        float editDistance = editDistanceSimilarity.calculateSimilarity(title1, title2);
        if (editDistance == 0.0f) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
        
        return new Vote(Vote.VoteStatus.PROBABILITY, editDistance);
    }
    
    private String getNormalizedTitle(DocumentProtos.DocumentMetadata doc) {
        String title = DocumentWrapperUtils.getMainTitle(doc);
        title = StringTools.normalize(title);
        title = StringTools.replaceNumbersToDecimal(title);
        title = StringTools.normalizePartQualifiers(title);
        
        return title;
    }

    public void setDisapproveLevel(float disapproveLevel) {
        this.disapproveLevel = disapproveLevel;
    }

    public void setApproveLevel(float approveLevel) {
        this.approveLevel = approveLevel;
    }

    public void setMaxNormalizedTitleLength(int maxNormalizedTitleLength) {
        this.maxNormalizedTitleLength = maxNormalizedTitleLength;
    }
}

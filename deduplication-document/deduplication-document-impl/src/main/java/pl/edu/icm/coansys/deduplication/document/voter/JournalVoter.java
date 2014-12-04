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
package pl.edu.icm.coansys.deduplication.document.voter;

import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.commons.stringsimilarity.EditDistanceSimilarity;
import pl.edu.icm.coansys.commons.stringsimilarity.SimilarityCalculator;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class JournalVoter extends AbstractSimilarityVoter {

    private float disapproveLevel;
    private float approveLevel;

    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {
        String issn1 = extractIssn(doc1);
        String issn2 = extractIssn(doc2);
        if (issn1 != null && !issn1.isEmpty() && issn1.equals(issn2)) {
            return new Vote(Vote.VoteStatus.PROBABILITY, 1.0f);
        }

        String journal1 = extractJournal(doc1);
        String journal2 = extractJournal(doc2);
        if (journal1 == null || journal2 == null) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        }

        journal1 = StringTools.normalize(journal1);
        journal2 = StringTools.normalize(journal2);

        //SimilarityCalculator calculator = new LCSSimilarity();
        SimilarityCalculator calculator = new EditDistanceSimilarity(approveLevel, disapproveLevel);
        float similarity = calculator.calculateSimilarity(journal1, journal2);
        if (similarity > 0) {
            return new Vote(Vote.VoteStatus.PROBABILITY, similarity);
        } else {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
    }

    private static String extractJournal(DocumentProtos.DocumentMetadata doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getBasicMetadata();
        return basicMetadata.hasJournal() ? basicMetadata.getJournal() : null;
    }

    private static String extractIssn(DocumentProtos.DocumentMetadata doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getBasicMetadata();
        return basicMetadata.hasIssn() ? basicMetadata.getIssn() : null;
    }

    public void setDisapproveLevel(float disapproveLevel) {
        this.disapproveLevel = disapproveLevel;
    }

    public void setApproveLevel(float approveLevel) {
        this.approveLevel = approveLevel;
    }
}

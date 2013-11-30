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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class IssueVoter extends AbstractSimilarityVoter {
    
    private static Logger log = LoggerFactory.getLogger(IssueVoter.class);

    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {
        Integer issue1 = extractIssue(doc1);
        Integer issue2 = extractIssue(doc2);

        if (issue1 == null || issue2 == null) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        } else if (issue1.equals(issue2)) {
            return new Vote(Vote.VoteStatus.PROBABILITY, 1.0f);
        } else {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
    }

    private static Integer extractIssue(DocumentProtos.DocumentMetadata doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getBasicMetadata();
        if (basicMetadata.hasIssue()) {
            String issueStr = basicMetadata.getIssue();
            try {
                return Integer.parseInt(issueStr);
            } catch (NumberFormatException ex) {
                log.warn("Cannot parse issue: " + issueStr);
                return null;
            }
        } else {
            return null;
        }
    }
}

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

import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class DoiVoter extends AbstractSimilarityVoter {

    @Override
    public Vote vote(DocumentProtos.DocumentWrapper doc1, DocumentProtos.DocumentWrapper doc2) {
        String doi1 = extractDOI(doc1);
        String doi2 = extractDOI(doc2);
        if (doi1 == null || doi2 == null) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        } else if (doi1.equals(doi2)) {
            return new Vote(Vote.VoteStatus.EQUALS);
        } else {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
    }
    
    private String extractDOI(DocumentProtos.DocumentWrapper doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getDocumentMetadata().getBasicMetadata();
        if (!basicMetadata.hasDoi()) {
            return null;
        } else {
            return basicMetadata.getDoi();
        }
    }
}

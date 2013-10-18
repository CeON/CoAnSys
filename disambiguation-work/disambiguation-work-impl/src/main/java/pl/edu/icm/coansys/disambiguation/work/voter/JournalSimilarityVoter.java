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

import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.commons.stringsimilarity.LCSSimilarity;
import pl.edu.icm.coansys.commons.stringsimilarity.SimilarityCalculator;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class JournalSimilarityVoter extends AbstractSimilarityVoter {

    @Override
    public Vote vote(DocumentProtos.DocumentWrapper doc1, DocumentProtos.DocumentWrapper doc2) {
        String journal1 = extractJournal(doc1);
        String journal2 = extractJournal(doc2);
        if (journal1 == null || journal2 == null) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        }
        
        journal1 = StringTools.normalize(journal1);
        journal2 = StringTools.normalize(journal2);
        
        SimilarityCalculator calculator = new LCSSimilarity();
        float similarity = calculator.calculateSimilarity(journal1, journal2);
        return new Vote(Vote.VoteStatus.PROBABILITY, similarity);
    }

    private static String extractJournal(DocumentProtos.DocumentWrapper doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getDocumentMetadata().getBasicMetadata();
        return basicMetadata.hasJournal() ? basicMetadata.getJournal() : null;
    }
}
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
package pl.edu.icm.coansys.deduplication.document;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.deduplication.document.voter.SimilarityVoter;
import pl.edu.icm.coansys.deduplication.document.voter.Vote;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 *
 */
public class DuplicateWorkComparator {

    private static Logger logger = LoggerFactory.getLogger(DuplicateWorkComparator.class);
    private List<SimilarityVoter> similarityVoters;

    /**
     * Tells whether the given documents are duplicates.
     */
    public boolean isDuplicate(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {

        double weightsSum = 0.0;
        double probabilitiesSum = 0.0;

        String ids = doc1.getKey() + ", " + doc2.getKey();
        StringBuilder logBuilder = new StringBuilder();

        if (similarityVoters != null) {
            for (SimilarityVoter voter : similarityVoters) {
                Vote vote = voter.vote(doc1, doc2);

                switch (vote.getStatus()) {
                    case EQUALS:
                        logger.info("Documents " + ids + " considered as duplicates because of result EQUALS of voter "
                                + voter.getClass().getName());
                        return true;
                    case NOT_EQUALS:
                        return false;
                    case ABSTAIN:
                        continue;
                    case PROBABILITY:
                        logBuilder.append(" -- voter ").append(voter.getClass().getName())
                                .append(" returned probability ").append(vote.getProbability())
                                .append(", weight ").append(voter.getWeight()).append('\n');
                        weightsSum += voter.getWeight();
                        probabilitiesSum += vote.getProbability() * voter.getWeight();
                }
            }
        }

        logger.info(ids + " considered as duplicates because:\n" + logBuilder.toString());
        //logger.info("doc1:\n" + doc1.getDocumentMetadata());
        //logger.info("doc2:\n" + doc2.getDocumentMetadata());
        return (weightsSum > 0) && (probabilitiesSum / weightsSum > 0.5);
    }

    //******************** SETTERS ********************
    public void setSimilarityVoters(List<SimilarityVoter> similarityVoters) {
        this.similarityVoters = similarityVoters;
    }
}

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

import java.util.List;
import pl.edu.icm.coansys.disambiguation.work.voter.SimilarityVoter;
import pl.edu.icm.coansys.disambiguation.work.voter.Vote;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author Łukasz Dumiszewski
 * @author Artur Czeczko
 *
 */
public class DuplicateWorkComparator {

    private List<SimilarityVoter> similarityVoters;

    /**
     * Tells whether the given documents are duplicates.
     */
    public boolean isDuplicate(DocumentWrapper doc1, DocumentWrapper doc2) {

        double weightsSum = 0.0;
        double probabilitiesSum = 0.0;
        
        if (similarityVoters != null) {
            for (SimilarityVoter voter : similarityVoters) {
                Vote vote = voter.vote(doc1, doc2);

                switch (vote.getStatus()) {
                    case EQUALS:
                        return true;
                    case NOT_EQUALS:
                        return false;
                    case ABSTAIN:
                        continue;
                    case PROBABILITY:
                        weightsSum += voter.getWeight();
                        probabilitiesSum += vote.getProbability() * voter.getWeight();
                }
            }
        }

        return (weightsSum > 0) && (probabilitiesSum / weightsSum > 0.5);
    }

    //******************** SETTERS ********************
    public void setSimilarityVoters(List<SimilarityVoter> similarityVoters) {
        this.similarityVoters = similarityVoters;
    }
}

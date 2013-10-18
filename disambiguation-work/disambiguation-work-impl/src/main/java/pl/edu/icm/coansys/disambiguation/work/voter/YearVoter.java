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
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class YearVoter extends AbstractSimilarityVoter {

    private static Logger log = LoggerFactory.getLogger(YearVoter.class);
    private int yearMaxDistance = 0;

    @Override
    public Vote vote(DocumentProtos.DocumentWrapper doc1, DocumentProtos.DocumentWrapper doc2) {

        Integer doc1year = extractYear(doc1);
        Integer doc2year = extractYear(doc2);

        if (doc1year == null || doc2year == null) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        }

        int dif = Math.abs(doc2year - doc1year);
        if (dif > yearMaxDistance) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        } else {
            return new Vote(Vote.VoteStatus.PROBABILITY, 1 - dif / (yearMaxDistance + 1));
        }
    }

    private static Integer extractYear(DocumentProtos.DocumentWrapper doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getDocumentMetadata().getBasicMetadata();
        if (basicMetadata.hasYear()) {
            String yearStr = basicMetadata.getYear();
            try {
                Integer year = Integer.parseInt(yearStr);
                return year;
            } catch (NumberFormatException ex) {
                log.warn("Cannot parse year: " + yearStr, ex);
                return null;
            }
        } else {
            return null;
        }
    }

    public void setYearMaxDistance(int yearMaxDistance) {
        if (yearMaxDistance < 0) {
            throw new IllegalArgumentException("yearMaxDistance cannot be negative: " + yearMaxDistance);
        }
        this.yearMaxDistance = yearMaxDistance;
    }
}

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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class VolumeVoter extends AbstractSimilarityVoter {

    private static Logger log = LoggerFactory.getLogger(VolumeVoter.class);

    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {
        List<Integer> issue1 = extractVolume(doc1);
        List<Integer> issue2 = extractVolume(doc2);

        if (issue1.isEmpty() || issue2.isEmpty()) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        } else if (issue1.equals(issue2)) {
            return new Vote(Vote.VoteStatus.PROBABILITY, 1.0f);
        } else {
            int intersectionSize = 0;
            for (Integer i1 : issue1) {
                if (issue2.contains(i1)) {
                    intersectionSize++;
                }
            }
            if (intersectionSize == 0) {
                return new Vote(Vote.VoteStatus.NOT_EQUALS);
            } else {
                float prob = 0.9f * intersectionSize * 2 / (issue1.size() + issue2.size());
                return new Vote(Vote.VoteStatus.PROBABILITY, prob);
            }
        }
    }

    private static List<Integer> extractVolume(DocumentProtos.DocumentMetadata doc) {

        Pattern digitsPatt = Pattern.compile("(\\d+)");

        List<Integer> result = new ArrayList<Integer>();

        DocumentProtos.BasicMetadata basicMetadata = doc.getBasicMetadata();
        if (basicMetadata.hasVolume()) {
            String volumeStr = basicMetadata.getVolume();
            Matcher digitsMatcher = digitsPatt.matcher(volumeStr);
            if (digitsMatcher.matches()) {
                for (int i = 0; i < digitsMatcher.groupCount(); i++) {
                    result.add(Integer.parseInt(digitsMatcher.group(i)));
                }
            }
        }

        return result;
    }
}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public abstract class AbstractNumbersVoter extends AbstractSimilarityVoter {

    private boolean abstainIfAbsent = false;
    private float absentResult = 0.52f;
    private float subsetResult = 0.8f;
    private float partiallyMatchResult = 0.64f;
    private boolean removeRepeated = false;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {
        List<Long> numbers1 = extractNumbers(doc1, removeRepeated);
        List<Long> numbers2 = extractNumbers(doc2, removeRepeated);

        if (numbers1.size() * numbers2.size() == 0) {
            return abstainIfAbsent ? new Vote(Vote.VoteStatus.ABSTAIN) : new Vote(Vote.VoteStatus.PROBABILITY, absentResult);
        }

        Collections.sort(numbers1);
        Collections.sort(numbers2);

        int intersectionSize = 0;
        int idx1 = 0;
        int idx2 = 0;
        while (idx1 < numbers1.size() && idx2 < numbers2.size()) {
            if (numbers1.get(idx1).equals(numbers2.get(idx2))) {
                intersectionSize++;
                idx1++;
                idx2++;
            } else if (numbers1.get(idx1) < numbers2.get(idx2)) {
                idx1++;
            } else {
                idx2++;
            }
        }

        if (intersectionSize == 0) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }

        if (intersectionSize != Math.min(numbers1.size(), numbers2.size())) {
            return new Vote(Vote.VoteStatus.PROBABILITY, partiallyMatchResult);
        }

        if (numbers1.size() == numbers2.size()) {
            return new Vote(Vote.VoteStatus.PROBABILITY, 1.0f);
        }

        return new Vote(Vote.VoteStatus.PROBABILITY, subsetResult);

    }

    private List<Long> extractNumbers(DocumentProtos.DocumentMetadata doc, boolean removeRepeated) {

        Pattern digitsPatt = Pattern.compile("\\d+");

        List<Long> result = new ArrayList<Long>();
        String allFields = extractNumbersString(doc);
        Matcher digitsMatcher = digitsPatt.matcher(allFields);
        while (digitsMatcher.find()) {
            try {
                result.add(Long.parseLong(digitsMatcher.group()));
            } catch(NumberFormatException ex) {
                log.warn(ex.getMessage());
            }
        }
        
        if (removeRepeated) {
            Set<Long> numbersSet = new HashSet<Long>(result);
            result.clear();
            result.addAll(numbersSet);
        }

        return result;
    }

    protected abstract String extractNumbersString(DocumentProtos.DocumentMetadata doc);

    public void setAbstainIfAbsent(boolean abstainIfAbsent) {
        this.abstainIfAbsent = abstainIfAbsent;
    }

    public void setAbsentResult(float absentResult) {
        this.absentResult = absentResult;
    }

    public void setSubsetResult(float subsetResult) {
        this.subsetResult = subsetResult;
    }

    public void setPartiallyMatchResult(float partiallyMatchResult) {
        this.partiallyMatchResult = partiallyMatchResult;
    }

    public void setRemoveRepeated(boolean removeRepeated) {
        this.removeRepeated = removeRepeated;
    }
}

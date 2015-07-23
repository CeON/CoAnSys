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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class DoiVoter extends AbstractSimilarityVoter {
    
    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {
        String doi1 = extractDOI(doc1);
        String doi2 = extractDOI(doc2);
        if (doi1 == null || doi2 == null) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        } else if (doi1.equalsIgnoreCase(doi2)) {
            return new Vote(Vote.VoteStatus.EQUALS);
        } else {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
    }
    
    private static String extractDOI(DocumentProtos.DocumentMetadata doc) {
        DocumentProtos.BasicMetadata basicMetadata = doc.getBasicMetadata();
        if (!basicMetadata.hasDoi()) {
            return null;
        }
        
        String rawDoi = basicMetadata.getDoi().trim();
        String[] splittedDoi = rawDoi.split("\\|");
        if (splittedDoi.length == 2 && (splittedDoi[0].equals(splittedDoi[1]) || splittedDoi[1].startsWith("issn"))) {
            rawDoi = splittedDoi[0];
        } else if (rawDoi.length() % 2 == 0) {
            String firstHalf = rawDoi.substring(0, rawDoi.length() / 2);
            String secondHalf = rawDoi.substring(rawDoi.length() / 2);
            if (firstHalf.equals(secondHalf)) {
                rawDoi = firstHalf;
            }
        }
        
        String doiregex = ".*?(10[.][0-9]{4,}[^\\s\"/<>]*/[^\\s\"]+[^\\s\"\\]\\.;]).*";
        Pattern doiPattern = Pattern.compile(doiregex);
        Matcher matcher = doiPattern.matcher(rawDoi);
        
        if (matcher.matches()) {
            String doi = matcher.group(1);
            return doi;
        } else {
            return null;
        }
    }
}

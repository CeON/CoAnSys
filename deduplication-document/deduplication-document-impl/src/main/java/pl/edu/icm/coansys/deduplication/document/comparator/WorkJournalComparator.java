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

package pl.edu.icm.coansys.deduplication.document.comparator;

import static pl.edu.icm.coansys.commons.java.StringTools.inLevenshteinDistance;
import static pl.edu.icm.coansys.commons.java.StringTools.normalize;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

@Service("workJournalComparator")
public class WorkJournalComparator {

    boolean sameJournals(DocumentWrapper doc1, DocumentWrapper doc2) {
        
        String issn1 = normalize(doc1.getDocumentMetadata().getBasicMetadata().getIssn());
        String issn2 = normalize(doc2.getDocumentMetadata().getBasicMetadata().getIssn());
        
        if (!StringUtils.isBlank(issn1) && !StringUtils.isBlank(issn2)) {
            return StringUtils.equalsIgnoreCase(issn1, issn2);
        }
        
        String journalTitle1 = normalize(doc1.getDocumentMetadata().getBasicMetadata().getJournal());
        String journalTitle2 = normalize(doc2.getDocumentMetadata().getBasicMetadata().getJournal());
        if (StringUtils.isBlank(journalTitle1) || StringUtils.isBlank(journalTitle2)) {
            return false;
        }
        
        if (inLevenshteinDistance(journalTitle1, journalTitle2, 1)) {
            return true;
        }
        
        return false;
    }

}

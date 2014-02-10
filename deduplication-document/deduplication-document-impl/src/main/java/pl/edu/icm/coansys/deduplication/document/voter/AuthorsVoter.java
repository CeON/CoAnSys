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

import java.util.List;
import pl.edu.icm.coansys.commons.java.Pair;
import pl.edu.icm.coansys.commons.java.StringTools;
import pl.edu.icm.coansys.commons.reparser.Node;
import pl.edu.icm.coansys.commons.reparser.RegexpParser;
import pl.edu.icm.coansys.commons.stringsimilarity.EditDistanceSimilarity;
import pl.edu.icm.coansys.commons.stringsimilarity.SimilarityCalculator;
import pl.edu.icm.coansys.models.DocumentProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuthorsVoter extends AbstractSimilarityVoter {
    
    private float disapproveLevel;
    private float approveLevel;
    
    @Override
    public Vote vote(DocumentProtos.DocumentMetadata doc1, DocumentProtos.DocumentMetadata doc2) {

        Pair<String[], Boolean> doc1surnames = extractSurnames(doc1);
        Pair<String[], Boolean> doc2surnames = extractSurnames(doc2);

        if (doc1surnames.getX().length == 0 || doc2surnames.getX().length == 0) {
            return new Vote(Vote.VoteStatus.ABSTAIN);
        }
        
        float firstAuthorComponent = 0.0f;
        float allAuthorsMatchFactor = 0.75f;
        
        if (doc1surnames.getY() && doc2surnames.getY()) {
            String doc1firstAuthor = doc1surnames.getX()[0];
            String doc2firstAuthor = doc2surnames.getX()[0];
            
            SimilarityCalculator similarity = getSimilarityCalculator();
            if (similarity.calculateSimilarity(doc1firstAuthor, doc2firstAuthor) > 0.5f) {
                firstAuthorComponent = 0.667f;
                allAuthorsMatchFactor = 0.333f;
            } else {
                allAuthorsMatchFactor = 0.667f;
            }
        }
        
        float probability = firstAuthorComponent + 
                allAuthorsMatchFactor * allAuthorsMatching(doc1surnames.getX(), doc2surnames.getX());
        if (probability > 1.0f) {
            probability = 1.0f;
        }
        if (probability <= 0.0f) {
            return new Vote(Vote.VoteStatus.NOT_EQUALS);
        }
        
        return new Vote(Vote.VoteStatus.PROBABILITY, probability);
    }

    
    private static Pair<String[], Boolean> extractSurnames(DocumentProtos.DocumentMetadata doc) {
        RegexpParser authorParser = new RegexpParser("authorParser.properties", "author");
        List<DocumentProtos.Author> authorList = doc.getBasicMetadata().getAuthorList();
        String[] resultByPositionNb = new String[authorList.size()];
        String[] resultByOrder = new String[authorList.size()];
        boolean positionsCorrect = true;

        int orderNb = 0;
        for (DocumentProtos.Author author : doc.getBasicMetadata().getAuthorList()) {
            String surname;
            if (author.hasSurname()) {
                surname = author.getSurname();
            } else {
                String fullname = author.getName();
                Node authorNode = authorParser.parse(fullname);
                Node surnameNode;
                if(authorNode != null) {
                    surnameNode = authorNode.getFirstField("surname");
                    if(surnameNode != null) {
                        surname = surnameNode.getValue();
                    }
                    else {
                        surname = fullname;
                    }
                }
                else {
                    surname = fullname;                        
                }
            }
            
            surname = StringTools.normalize(surname);
            
            if (positionsCorrect) {
                if (!author.hasPositionNumber()) {
                    positionsCorrect = false;
                } else {
                    int authorPosition = author.getPositionNumber() - 1;
                    if (authorPosition < 0 || authorPosition >= resultByPositionNb.length || resultByPositionNb[authorPosition] != null) {
                        positionsCorrect = false;
                    } else {
                        resultByPositionNb[authorPosition] = surname;
                    }
                }
            }
            resultByOrder[orderNb] = surname;
            orderNb++;
        }
        return new Pair<String[], Boolean>(positionsCorrect ? resultByPositionNb : resultByOrder, positionsCorrect);
    }
    
    private SimilarityCalculator getSimilarityCalculator() {
        return new EditDistanceSimilarity(approveLevel, disapproveLevel);
    }
    
    /**
     * 
     * 
     * @param doc1authors
     * @param doc2authors
     * @return 
     */
    private float allAuthorsMatching(String[] doc1authors, String[] doc2authors) {
        int intersectionSize = 0;
        
        SimilarityCalculator similarity = getSimilarityCalculator();
        
        for (String d1author : doc1authors) {
            for (String d2author : doc2authors) {
                if (similarity.calculateSimilarity(d1author, d2author) > 0.5) {
                    intersectionSize++;
                    break;
                }
            }
        }
        return 2.0f * intersectionSize / (doc1authors.length + doc2authors.length);
    }

    public void setDisapproveLevel(float disapproveLevel) {
        this.disapproveLevel = disapproveLevel;
    }

    public void setApproveLevel(float approveLevel) {
        this.approveLevel = approveLevel;
    }
}

/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 * A set of configuration options influencing the result of 
 * {@link DuplicateWorkVoter#isDuplicate(DocumentWrapper, DocumentWrapper, DuplicateWorkVoterConfiguration)} 
 * 
 * @author lukdumi
 *
 */
public class DuplicateWorkVoterConfiguration {

    private int titleMaxLevenshteinDistance = 5;
    private int titleMostMeaningfulEndLength = 7;
    private int titleEndMaxLevenshteinDistance = 3;

    //******************** GETTERS ********************
    
    
    
    /** 
     * Max levenshtein distance of the publication title
     * Defaults to 5 
     * 
     * */
    public int getTitleMaxLevenshteinDistance() {
        return titleMaxLevenshteinDistance;
    }

    /**
     * The end of title is of special meaning when considering the equality of the publication.
     * For the end of the title it will be applied additionally {@link #getTitleEndMaxLevenshteinDistance()}
     * 
     * Think about it: <br/>
     * Aspiracje integracyjne państw śródziemnomorskich - Turcja <br/>
     * and <br/>
     * Aspiracje integracyjne państw śródziemnomorskich - Malta <br/>
     * are probably not the duplicates of the same article
     */
    public int getTitleMostMeaningfulEndLength() {
        return titleMostMeaningfulEndLength;
    }
    
    /**
     * The max levenshtein distance of the title end specified by {@link #getTitleMostMeaningfulEndLength()}
     */
    public int getTitleEndMaxLevenshteinDistance() {
        return titleEndMaxLevenshteinDistance;
    }

    


    
    //******************** LOGIC ********************
    
    
    
    
    //******** quasi builder methods */
    
    public static DuplicateWorkVoterConfiguration create() {
        return new DuplicateWorkVoterConfiguration();
    }
    
    public DuplicateWorkVoterConfiguration byTitleMaxLevenshteinDistance(int titleMaxLevenshteinDistance) {
        this.setTitleMaxLevenshteinDistance(titleMaxLevenshteinDistance);
        return this;
    }
    
    
    
    
    //******************** SETTERS ********************
    
    public void setTitleMaxLevenshteinDistance(int titleMaxLevenshteinDistance) {
        this.titleMaxLevenshteinDistance = titleMaxLevenshteinDistance;
    }

    public void setTitleMostMeaningfulEndLength(int titleMostMeaningfulEndLength) {
        this.titleMostMeaningfulEndLength = titleMostMeaningfulEndLength;
    }
    
    public void setTitleEndMaxLevenshteinDistance(int titleEndMaxLevenshteinDistance) {
        this.titleEndMaxLevenshteinDistance = titleEndMaxLevenshteinDistance;
    }
    

    
}

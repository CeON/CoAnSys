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

package pl.edu.icm.coansys.kwdextraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class KeywordCandidate {
    
    private String keyword = "";
    private List<String> words = new ArrayList<String>();
    private Double score;
    private int counter = 1;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getWords() {
        return words;
    }

    public void addWord(String word) {
        this.words.add(word);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
    public void incCounter() {
        counter++;
    }
    
    public int getCounter() {
        return counter;
    }

    public static class ScoreComparator implements Comparator<KeywordCandidate>, Serializable {

        @Override
        public int compare(KeywordCandidate o1, KeywordCandidate o2) {
            return - o1.score.compareTo(o2.score);
        }
    }
}

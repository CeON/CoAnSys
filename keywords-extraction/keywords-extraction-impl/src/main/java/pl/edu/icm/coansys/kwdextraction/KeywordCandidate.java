/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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

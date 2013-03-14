/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class KeywordCandidate implements Comparable<KeywordCandidate> {
    
    private String keyword = "";
    private List<String> words = new ArrayList<String>();
    private Double score;

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

    @Override
    public int compareTo(KeywordCandidate o) {
        return - this.score.compareTo(o.score);
    }
}

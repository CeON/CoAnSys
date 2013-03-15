/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.BreakIterator;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.cermine.DocumentTextExtractor;
import pl.edu.icm.cermine.PdfRawTextExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentProtos;

/**
 * Implementation of Rapid Automatic Keyword Extraction algorithm
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class RakeExtractor {

    private static final Logger logger = LoggerFactory.getLogger(RakeExtractor.class);
    private static final String STOPWORDS_PATH = "stopwords/stopwords_fr_en.txt";
    private static final String ILLEGAL_CHARS = "[^\\p{L}0-9-'\\s]";
    private static final int DEFAULT_KEYWORDS_NUMBER = 8;
    private String content;
    private Set<String> stopwords;
    private List<KeywordCandidate> keywordCandidates;
    private Map<String, Map<String, Integer>> cooccurrences;

    /**
     * Every constructor sets this.content (document's content) and calls
     * prepareToExtraction()
     *
     * @param content Document's content as a String
     * @throws IOException
     */
    public RakeExtractor(String content) throws IOException {
        this.content = content;
        prepareToExtraction();
    }

    /**
     * Every constructor sets this.content (document's content) and calls
     * prepareToExtraction()
     *
     * @param pdfStream Stream containing a PDF file
     * @throws AnalysisException
     * @throws IOException
     */
    public RakeExtractor(InputStream pdfStream) throws AnalysisException, IOException {
        DocumentTextExtractor<String> extr = new PdfRawTextExtractor();
        content = extr.extractText(pdfStream);
        prepareToExtraction();
    }

    /**
     * Every constructor sets this.content (document's content) and calls
     * prepareToExtraction()
     *
     * @param docWrapper Protocol buffers message containing document
     * @throws IOException
     */
    public RakeExtractor(DocumentProtos.DocumentWrapper docWrapper) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (DocumentProtos.Media media : docWrapper.getMediaContainer().getMediaList()) {
            if (media.getMediaType().equals(ProtoConstants.mediaTypePdf)) {
                try {
                    DocumentTextExtractor<String> extr = new PdfRawTextExtractor();
                    sb.append(extr.extractText(media.getContent().newInput()));
                } catch (AnalysisException ex) {
                    logger.error("Cannot extract text from PDF: " + ex.toString());
                }
            } else if (media.getMediaType().equals(ProtoConstants.mediaTypeTxt)) {
                sb.append(media.getContent().toStringUtf8());
            }
            sb.append("\n");
        }
        content = sb.toString();
        prepareToExtraction();
    }

    /**
     * All steps of keyword extraction. this.content should be set by a constructor.
     * 
     * @throws IOException 
     */
    private void prepareToExtraction() throws IOException {
        loadStopwords();
        extractKeywordCandidates();
        countCooccurrences();
        countMetrics();
    }

    /**
     * Loading stopwords from a file
     * 
     * @throws IOException 
     */
    private void loadStopwords() throws IOException {
        stopwords = new HashSet<String>();

        URL stopwordsURL = this.getClass().getClassLoader().getResource(STOPWORDS_PATH);
        String stopwordsPath = stopwordsURL.getPath();
        BufferedReader br = new BufferedReader(new FileReader(stopwordsPath));

        String stopword = br.readLine();
        while (stopword != null) {
            stopword = stopword.trim();
            if (!stopword.isEmpty()) {
                stopwords.add(stopword);
            }
            stopword = br.readLine();
        }
    }

    /**
     * Finding words or word sequences separated by stopwords, punstuation marks etc.
     */
    private void extractKeywordCandidates() {

        keywordCandidates = new ArrayList<KeywordCandidate>();

        BreakIterator wordIterator = BreakIterator.getWordInstance();

        wordIterator.setText(content);
        int wordStart = wordIterator.first();

        int candidateStart = wordStart;
        String candidate = null;
        KeywordCandidate kwdCand = new KeywordCandidate();

        for (int wordEnd = wordIterator.next(); wordEnd != BreakIterator.DONE; wordStart = wordEnd, wordEnd = wordIterator.next()) {

            String word = content.substring(wordStart, wordEnd).trim().toLowerCase();
            String alpha = word.replaceAll(ILLEGAL_CHARS, "");

            if (!word.isEmpty()) {

                if (stopwords.contains(word) || word.matches("\\W+") || isNum(word) || !word.equals(alpha)) {
                    candidate = content.substring(candidateStart, wordStart);
                } else {
                    kwdCand.addWord(word);
                    if (wordEnd == content.length()) {
                        candidate = content.substring(candidateStart, wordEnd);
                    }
                }
                if (candidate != null) {
                    candidate = candidate.trim().toLowerCase().replaceAll(ILLEGAL_CHARS, "");
                    if (!candidate.isEmpty()) {
                        kwdCand.setKeyword(candidate);
                        keywordCandidates.add(kwdCand);
                    }
                    candidate = null;
                    candidateStart = wordEnd;
                    kwdCand = new KeywordCandidate();
                }
            }
        }
    }

    /**
     * Calculate a matrix with words cooccurrences in keyword candidates.
     */
    private void countCooccurrences() {
        cooccurrences = new HashMap<String, Map<String, Integer>>();
        
        for (KeywordCandidate cand : keywordCandidates) {
            for (String word : cand.getWords()) {
                Map<String, Integer> submap;
                if (cooccurrences.containsKey(word)) {
                    submap = cooccurrences.get(word);
                } else {
                    submap = new HashMap<String, Integer>();
                    cooccurrences.put(word, submap);
                }
                for (String coword : cand.getWords()) {
                    int count = 1;
                    if (submap.containsKey(coword)) {
                        count += submap.get(coword);
                    }
                    submap.put(coword, count);
                }
            }
        }
    }
    
    /**
     * Counts deg/freq for every words and for keyword candidates.
     */
    private void countMetrics() {
        Map<String, Double> wordScore = new HashMap<String, Double>();
        
        for (String word : cooccurrences.keySet()) {
            //deg and freq
            int degValue = 0;
            for (String coword : cooccurrences.get(word).keySet()) {
                degValue += cooccurrences.get(word).get(coword);
            }
            int freqValue = cooccurrences.get(word).get(word);
            
            //wordScore = deg/freq
            wordScore.put(word, 1.0 * degValue / freqValue);
        }
        
        for (KeywordCandidate cand : keywordCandidates) {
            double score = 0;
            for (String word : cand.getWords()) {
                score += wordScore.get(word);
            }
            cand.setScore(score);
        }
        
        Collections.sort(keywordCandidates);
    }
    
    /**
     * Returns n best keywords from keyword candidates.
     * 
     * @param n
     * @return 
     */
    private List<String> choiceKeywords(int n) {
        int resultSize = Math.min(n, keywordCandidates.size());
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < resultSize; i++) {
            result.add(keywordCandidates.get(i).getKeyword());
        }
        return result;
    }

    /**
     * Returns extracted keywords.
     * 
     * @return 
     */
    public List<String> getKeywords() {
        return choiceKeywords(DEFAULT_KEYWORDS_NUMBER);
    }
    
    /**
     * Returns n best extracted keywords.
     * 
     * @param n
     * @return 
     */
    public List<String> getKeywords(int n) {
        return choiceKeywords(n);
    }

    /**
     * Checks if s is a number.
     * 
     * @param s
     * @return 
     */
    private static boolean isNum(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

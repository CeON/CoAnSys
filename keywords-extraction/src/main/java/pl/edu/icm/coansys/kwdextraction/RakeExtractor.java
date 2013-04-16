/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Map.Entry;
import java.util.*;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.cermine.DocumentTextExtractor;
import pl.edu.icm.cermine.PdfNLMContentExtractor;
import pl.edu.icm.cermine.PdfRawTextExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.kwdextraction.langident.LanguageIdentifierBean;

/**
 * Implementation of Rapid Automatic Keyword Extraction algorithm
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class RakeExtractor {

    private enum Lang {

        PL, FR, EN
    }
    private static final Logger logger = LoggerFactory.getLogger(RakeExtractor.class);
    private static final String STOPWORDS_EN = "stopwords_en.txt";
    private static final String STOPWORDS_FR = "stopwords_fr.txt";
    private static final String STOPWORDS_PL = "stopwords_pl.txt";
    private static final String ILLEGAL_CHARS = "[^\\p{L}0-9-'\\s]";
    private static final int DEFAULT_KEYWORDS_NUMBER = 8;
    private static final Map<Lang, Set<String>> stopwords;
    private String content;
    private Lang lang = Lang.EN;
    private List<KeywordCandidate> keywordCandidates;
    private Map<String, Map<String, Integer>> cooccurrences;

    static {
        try {
            stopwords = new EnumMap<Lang, Set<String>>(Lang.class);
            for (Lang l : Lang.values()) {
                stopwords.put(l, loadStopwords(l));
            }
        } catch (IOException ex) {
            logger.error("Unable to load stopwords: " + ex);
            throw new RuntimeException(ex);
        }
    }

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
    public RakeExtractor(InputStream pdfStream) throws IOException, AnalysisException {
        content = extractTextFromPdf(pdfStream);
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
                    sb.append(extractTextFromPdf(media.getContent().newInput()));
                } catch (AnalysisException ex) {
                    logger.error("Cannot extract text from PDF: " + ex.toString() + " " + media.getSourcePath());
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
     * Extract text from pdf stream
     *
     * @param pdfStream
     * @return String object containing document content
     * @throws IOException
     * @throws AnalysisException
     */
    private String extractTextFromPdf(InputStream pdfStream) throws IOException, AnalysisException {
        String result;
        PdfNLMContentExtractor nextr = new PdfNLMContentExtractor();
        Element contentEl = nextr.extractContent(pdfStream);
        Element bodyEl = contentEl.getChild("body");
        result = bodyEl.getValue();
        
        if (result == null || result.isEmpty()) {
            DocumentTextExtractor<String> extr = new PdfRawTextExtractor();
            result = extr.extractText(pdfStream);
        }
        //TODO language detection
        LanguageIdentifierBean li = new LanguageIdentifierBean();
        return ("en".equals(li.classify(result))) ? result : "";
        //return result;
    }

    /**
     * All steps of keyword extraction. this.content should be set by a
     * constructor.
     *
     * @throws IOException
     */
    private void prepareToExtraction() throws IOException {
        extractKeywordCandidates();
        countCooccurrences();
        countMetrics();
    }

    /**
     * Loading stopwords from a file
     *
     * @throws IOException
     */
    private static Set<String> loadStopwords(Lang lang) throws IOException {
        Set<String> result = new HashSet<String>();

        String stopwordsPath;
        switch (lang) {
            case EN:
                stopwordsPath = STOPWORDS_EN;
                break;
            case FR:
                stopwordsPath = STOPWORDS_FR;
                break;
            case PL:
                stopwordsPath = STOPWORDS_PL;
                break;
            default:
                stopwordsPath = STOPWORDS_EN;
                break;
        }

        if (lang.equals(Lang.FR)) {
            stopwordsPath = STOPWORDS_FR;
        } else if (lang.equals(Lang.PL)) {
            stopwordsPath = STOPWORDS_PL;
        }

        InputStream stopwordsStream;
        try {
            stopwordsStream = RakeExtractor.class.getClassLoader().getResourceAsStream(stopwordsPath);
        } catch (NullPointerException ex) {
            stopwordsStream = RakeExtractor.class.getClassLoader().getResourceAsStream("/" + stopwordsPath);
        }
        InputStreamReader isr = new InputStreamReader(stopwordsStream);
        BufferedReader br = new BufferedReader(isr);

        String stopword = br.readLine();
        while (stopword != null) {
            stopword = stopword.trim();
            if (!stopword.isEmpty()) {
                result.add(stopword);
            }
            stopword = br.readLine();
        }
        return result;
    }

    /**
     * Finding words or word sequences separated by stopwords, punstuation marks
     * etc.
     */
    private void extractKeywordCandidates() {

        Map<String, KeywordCandidate> candidatesMap = new HashMap<String, KeywordCandidate>();

        BreakIterator wordIterator = BreakIterator.getWordInstance();

        wordIterator.setText(content);
        int wordStart = wordIterator.first();

        int candidateStart = wordStart;
        String candidateStr = null;
        KeywordCandidate kwdCand = new KeywordCandidate();

        for (int wordEnd = wordIterator.next(); wordEnd != BreakIterator.DONE; wordStart = wordEnd, wordEnd = wordIterator.next()) {

            String word = content.substring(wordStart, wordEnd).trim().toLowerCase();
            String alpha = word.replaceAll(ILLEGAL_CHARS, "");

            if (!word.isEmpty()) {

                if (stopwords.get(lang).contains(word) || word.matches("\\W+") || isNum(word) || !word.equals(alpha)) {
                    candidateStr = content.substring(candidateStart, wordStart);
                } else {
                    kwdCand.addWord(word);
                    if (wordEnd == content.length()) {
                        candidateStr = content.substring(candidateStart, wordEnd);
                    }
                }
                if (candidateStr != null) {
                    candidateStr = candidateStr.trim().toLowerCase().replaceAll(ILLEGAL_CHARS, "").replaceAll("\\s+", " ");
                    if (!candidateStr.isEmpty()) {
                        if (candidatesMap.containsKey(candidateStr)) {
                            candidatesMap.get(candidateStr).incCounter();
                        } else {
                            kwdCand.setKeyword(candidateStr);
                            candidatesMap.put(candidateStr, kwdCand);
                        }
                    }
                    candidateStr = null;
                    candidateStart = wordEnd;
                    kwdCand = new KeywordCandidate();
                }
            }
        }

        keywordCandidates = new ArrayList<KeywordCandidate>();
        for (Entry<String, KeywordCandidate> e : candidatesMap.entrySet()) {
            keywordCandidates.add(e.getValue());
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
                    int count = cand.getCounter();
                    if (submap.containsKey(coword)) {
                        count += submap.get(coword) * cand.getCounter();
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

    public void setLang(String lang) {
        if ("fr".equals(lang)) {
            this.lang = Lang.FR;
        } else if ("pl".equals(lang)) {
            this.lang = Lang.PL;
        } else {
            this.lang = Lang.EN;
        }
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

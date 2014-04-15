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

package pl.edu.icm.coansys.kwdextraction;

import java.io.*;
import java.nio.charset.Charset;
import java.text.BreakIterator;
import java.util.Map.Entry;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.cermine.DocumentTextExtractor;
import pl.edu.icm.cermine.PdfNLMContentExtractor;
import pl.edu.icm.cermine.PdfRawTextExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.kwdextraction.langident.LanguageIdentifierBean;
import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;
import pl.edu.icm.coansys.models.constants.ProtoConstants;

/**
 * Implementation of Rapid Automatic Keyword Extraction algorithm
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class RakeExtractor {

    private enum Lang {

        // language code, stopwords path
//    	DE("de", "stopwords/stopwords_de.txt"),
//    	DK("dk", "stopwords/stopwords_dk.txt"),
    	EN("en", "stopwords/stopwords_en.txt"),
//    	ES("es", "stopwords/stopwords_es.txt"),
//    	FI("fi", "stopwords/stopwords_fi.txt"),
    	FR("fr", "stopwords/stopwords_fr.txt"),
//    	HU("hu", "stopwords/stopwords_hu.txt"),
//    	IT("it", "stopwords/stopwords_it.txt"),
//    	NL("nl", "stopwords/stopwords_nl.txt"),
//    	NO("no", "stopwords/stopwords_no.txt"),
    	PL("pl", "stopwords/stopwords_pl.txt");
//    	PT("pt", "stopwords/stopwords_pt.txt"),
//    	RU("ru", "stopwords/stopwords_ru.txt"),
//    	SE("se", "stopwords/stopwords_se.txt"),
//    	TR("tr", "stopwords/stopwords_tr.txt");
        private String langCode;
        private String stopwordsPath;

        Lang(String langCode, String stopwordsPath) {
            this.langCode = langCode;
            this.stopwordsPath = stopwordsPath;
        }
    }

    private enum ExtractionOption {

        CONTENT(true, false),
        ABSTRACT(false, true),
        CONTENT_AND_ABSTRACT(true, true);
        private boolean fromContent;
        private boolean fromAbstract;

        ExtractionOption(boolean fromContent, boolean fromAbstract) {
            this.fromContent = fromContent;
            this.fromAbstract = fromAbstract;
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(RakeExtractor.class);
    private static final String ILLEGAL_CHARS = "[^\\p{L}0-9-'\\s]";
    private static final int DEFAULT_KEYWORDS_NUMBER = 8;
    private static final Map<Lang, Set<String>> stopwords;
    private String content;
    private Lang lang;
    private ExtractionOption extractionOption;
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
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Every constructor sets this.content (document's content) and calls
     * prepareToExtraction()
     *
     * @param content Document's content as a String
     * @param langCode Document's language (texts in other languages will be
     * ignored)
     * @throws IOException
     */
    public RakeExtractor(String content, String langCode) throws IOException {
        setLang(langCode);
        this.content = filterTextByLang(content, lang.langCode);
        prepareToExtraction();
    }

    /**
     * Every constructor sets this.content (document's content) and calls
     * prepareToExtraction()
     *
     * @param pdfContent Byte array containing a PDF file
     * @param langCode Document's language (texts in other languages will be
     * ignored)
     * @throws AnalysisException
     * @throws IOException
     */
    public RakeExtractor(byte[] pdfContent, String langCode) throws AnalysisException, IOException {
        setLang(langCode);
        content = extractTextFromPdf(pdfContent, this.lang);
        prepareToExtraction();
    }

    /**
     * Every constructor sets this.content (document's content) and calls
     * prepareToExtraction()
     *
     * @param docWrapper Protocol buffers message containing document
     * @param option specifies which parts of the document are searched while
     * extracting keywords. Possible values: ABSTRACT - only the abstract is
     * processed, CONTENT - on the body of the document is processed,
     * CONTENT_AND_ABSTRACT - both abstract and body are processed.
     * @throws IOException
     */
    public RakeExtractor(DocumentProtos.DocumentWrapper docWrapper, String option, String langCode) throws IOException {
        setLang(langCode);
        setOption(option);
        StringBuilder sb = new StringBuilder();

        if (extractionOption.fromContent) {
            for (DocumentProtos.Media media : docWrapper.getMediaContainer().getMediaList()) {
                if (media.getMediaType().equals(ProtoConstants.mediaTypePdf)) {
                    try {
                        sb.append(extractTextFromPdf(media.getContent().toByteArray(), this.lang));
                    } catch (Exception ex) {
                        logger.error("Cannot extract text from PDF: " + ex.toString() + " " + media.getSourcePath());
                    }
                } else if (media.getMediaType().equals(ProtoConstants.mediaTypeTxt)) {
                    sb.append(filterTextByLang(media.getContent().toStringUtf8(), lang.langCode));
                }
                sb.append("\n");
            }
        }

        if (extractionOption.fromAbstract) {
            for (TextWithLanguage documentAbstract : docWrapper.getDocumentMetadata().getDocumentAbstractList()) {
                sb.append(filterTextByLang(documentAbstract.getText(), lang.langCode));
            }
        }

        content = sb.toString();
        prepareToExtraction();
    }

    /**
     * Extract text from pdf stream
     *
     * @param pdfContent content of pdf file
     * @param lang Document's language (texts in other languages will be
     * ignored)
     * @return String object containing document content
     * @throws IOException
     * @throws AnalysisException
     */
    private String extractTextFromPdf(byte[] pdfContent, Lang lang) throws IOException, AnalysisException {
        String result;
        InputStream pdfStream = new ByteArrayInputStream(pdfContent);
        PdfNLMContentExtractor nextr = new PdfNLMContentExtractor();
        Element contentEl = nextr.extractContent(pdfStream);
        Element bodyEl = contentEl.getChild("body");
        result = bodyEl.getValue();

        if (result == null || result.isEmpty()) {
            pdfStream = new ByteArrayInputStream(pdfContent);
            DocumentTextExtractor<String> extr = new PdfRawTextExtractor();
            result = extr.extractText(pdfStream);
        }

        return filterTextByLang(result, lang.langCode);
    }

    /**
     * Returns a text only if it is in given language
     *
     * @param text
     * @param language
     * @return text or empty String
     * @throws IOException
     */
    private String filterTextByLang(String text, String language) throws IOException {
        LanguageIdentifierBean li = new LanguageIdentifierBean();
        return (language.equals(li.classify(text))) ? text : "";
    }

    /**
     * All steps of keyword extraction. Not to be called before setting of
     * this.content, this.lang and this.option.
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
     * @param lang Stopwords language
     * @return Set of stopwords
     * @throws IOException
     */
    private static Set<String> loadStopwords(Lang lang) throws IOException {
        Set<String> result = new HashSet<String>();

        InputStream stopwordsStream;
        InputStreamReader isr;
        BufferedReader br = null;

        stopwordsStream = RakeExtractor.class.getClassLoader().getResourceAsStream(lang.stopwordsPath);
        
        try {
            isr = new InputStreamReader(stopwordsStream, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);

            String stopword = br.readLine();
            while (stopword != null) {
                stopword = stopword.trim();
                if (!stopword.isEmpty()) {
                    result.add(stopword);
                }
                stopword = br.readLine();
            }
        }
        finally {
            IOUtils.closeQuietly(br);
        }
        return result;
    }

    /**
     * Finding words or word sequences separated by stopwords, punctuation marks
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

        Collections.sort(keywordCandidates, new KeywordCandidate.ScoreComparator());
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
    
    private void setLang(String langCode) {
        this.lang = null;
        for(Lang curr : Lang.values()){
        	if(curr.langCode.equals(langCode)){
        		this.lang = curr;
            	break;
        	}
        }
        if(this.lang==null){
        	this.lang = Lang.EN;
        }
    }

    private void setOption(String option) {
        this.extractionOption = ExtractionOption.valueOf(option);
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

    public static List<String> getSupportedLanguages() {
        List<String> result = new ArrayList<String>();
        for (Lang l : Lang.values()) {
            result.add(l.langCode);
        }
        return result;
    }

    public static List<String> getAvailableExtractionOptions() {
        List<String> result = new ArrayList<String>();
        for (ExtractionOption opt : ExtractionOption.values()) {
            result.add(opt.name());
        }
        return result;
    }
}

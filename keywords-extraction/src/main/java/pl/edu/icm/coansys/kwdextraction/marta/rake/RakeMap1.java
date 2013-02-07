/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.rake;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import pl.edu.icm.coansys.kwdextraction.utils.IntStringPair;

/**
 * Map
 * input key  = <number of words read so far, path to the document>;
 * input value = <number of words in whole current document, single sentence> 
 * (we divide text into sentence using BREAK_ITERATOR)
 * 
 * This reducer using stopwords divides text into potential keywords.
 * For each potential keyword (keyword) and for each word this keyword consits of we have one output pair:
 * <word, keyword>

 */
public class RakeMap1 extends MapReduceBase implements Mapper<IntStringPair, IntStringPair, Text, Text> {
	public static int MAX_LENGTH;
	public static String PATH_TO_STOPWORDS;

	protected Set<String> stopwords = getStopwords();


	private Set<String> getStopwords() {
		ArrayList<String> stop = new ArrayList<String>();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(PATH_TO_STOPWORDS));
		    String str;
		    while ((str = in.readLine()) != null) {
		        stop.add(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		return new HashSet<String>(stop);
	}
	
	protected String cleanWord(String word) {
		return word.trim().toLowerCase();
	};

	protected boolean isNum(String s) {
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	@Override
	public void map(IntStringPair key, IntStringPair value, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

		String sentence = value.getSecond();
		BreakIterator wordIterator = BreakIterator.getWordInstance();
		wordIterator.setText(sentence);
		int wordStart = wordIterator.first(), wordEnd = 0, keywordStart = wordStart;
		while ((wordEnd = wordIterator.next()) != BreakIterator.DONE) {
			String word = cleanWord(sentence.substring(wordStart, wordEnd));
			String s2 = word.replaceAll("[^a-zA-Z0-9-\\s]", "");
			if (stopwords.contains(word) || word.matches("\\W+") || isNum(word) || !word.matches(s2)) {
				// word is keyword break
				String keyword = cleanWord(sentence.substring(keywordStart, wordStart));
				if (keyword.length() > 0) {
					String[] words = keyword.split("\\s");
					if (words.length <= MAX_LENGTH) {
						for (int i = 0; i < words.length; i++)
							output.collect(new Text(words[i]), new Text(keyword));
					}
				}
				keywordStart = wordEnd;
			} else if (wordEnd == sentence.length()) {
				// word is last in sentence
				String keyword = cleanWord(sentence.substring(keywordStart, wordEnd));
				if (keyword.length() > 0) {
					String[] words = keyword.split("\\s");
					if (words.length <= MAX_LENGTH) {
						for (int i = 0; i < words.length; i++)
							output.collect(new Text(words[i]), new Text(keyword));
					}
				}
			}
			wordStart = wordEnd;
		}
	}

}

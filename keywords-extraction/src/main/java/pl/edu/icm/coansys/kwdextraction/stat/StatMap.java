/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.stat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import pl.edu.icm.coansys.kwdextraction.utils.IntStringPair;
import pl.edu.icm.coansys.kwdextraction.utils.IntWritablePair;

public class StatMap extends MapReduceBase implements Mapper<IntStringPair, IntStringPair, Text, IntWritablePair> {
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
	
	private boolean toAdd(String word) {
		String words[] = word.split(" ");
		boolean add = true;
		for (String w : words) {
			if (stopwords.contains(w) || w.matches("\\W+") || isNum(w))
				add = false;
			String temp = w.replaceAll("[^a-zA-Z0-9-\\s]", "");
			if (!w.matches(temp))
				add = false;
		}
		return add;
	}

	@Override
	public void map(IntStringPair key, IntStringPair value,
			OutputCollector<Text, IntWritablePair> output, Reporter reporter)
			throws IOException {
		String sentence = value.getSecond();
		int wordCount = value.getFirst();
		
		Locale loc = new Locale("en");
		
	    
	    IntWritable sizeWritable = new IntWritable(key.getFirst());

		BreakIterator wordIterator = BreakIterator.getWordInstance(loc);
		wordIterator.setText(sentence);
		int wordStart = wordIterator.first(), wordEnd = 0, wCount = 0;
		ArrayList<String> words = new ArrayList<String>();
		while ((wordEnd = wordIterator.next()) != BreakIterator.DONE) {
			String word = cleanWord(sentence.substring(wordStart, wordEnd));
			wordStart = wordEnd;
			if (!word.isEmpty() && !word.matches("\\W") && !word.matches("_") )
				words.add(word);
		}
		for (int j = MAX_LENGTH; j > 0; j--) {
			wCount = 0;
			String s = "";
	    	if (words.size() >= j) {
	    		for (int k = 0; k < j; k++)
	    			s = s + words.get(k) + " ";
    			wCount++;
    			if (toAdd(s)) {
    				output.collect(new Text(s),
    						new IntWritablePair(new IntWritable(wordCount + wCount), sizeWritable));
    			}
	    		for (int k = j; k < words.size(); k++) {
	    			wCount++;
	    			s = s.substring(s.indexOf(" ") + 1);
	    			s = s.concat(words.get(k).concat(" "));
	    			if (toAdd(s)) {
	    				output.collect(new Text(s),
    						new IntWritablePair(new IntWritable(wordCount + wCount), sizeWritable));
	    			}
	    		}
	    	}
		}
	}

}

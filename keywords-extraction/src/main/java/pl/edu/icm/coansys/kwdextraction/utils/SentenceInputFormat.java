/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;

public class SentenceInputFormat extends FileInputFormat<IntStringPair, IntStringPair>{

	private static class SentenceRecordReader implements RecordReader<IntStringPair, IntStringPair> {
		
		private Path file;
		private int wordCount = 0;
		private int currentWordCount = 0;
		private ArrayList<String> content;
		private DataInputStream in;
		private BreakIterator sentenceIterator;
		private int sentenceStart;
		private int sentenceEnd;
		private int cur;

		
		public SentenceRecordReader(InputSplit genericSplit, JobConf job) throws IOException {
			FileSplit split = (FileSplit) genericSplit;
			file = split.getPath();
		 
			FileSystem fs = file.getFileSystem(job);
			in = fs.open(split.getPath());
			String text = in.readUTF();
			XmlParser xml = new XmlParser();
			content = xml.getText(text);
			for (int i = 0; i < content.size(); i++) {
				String[] words = content.get(i).split("\\s");
				wordCount += words.length;
			}
				
			sentenceIterator = BreakIterator.getSentenceInstance(new Locale("en"));
			sentenceIterator.setText(content.get(0));
			sentenceStart = 0;
			sentenceEnd = sentenceIterator.next();
			cur = 0;
		}
		
		
		@Override
		public void close() throws IOException {
			if (in != null) { 
				in.close(); in = null;
			}
		}

		@Override
		public IntStringPair createKey() {
			return new IntStringPair();
		}

		@Override
		public IntStringPair createValue() {
			return new IntStringPair();
		}

		@Override
		public long getPos() throws IOException {
			return currentWordCount;
		}

		@Override
		public float getProgress() throws IOException {
			if (wordCount == 0) return 0;
			return (float) currentWordCount / (float) wordCount;
		}

		@Override
		public boolean next(IntStringPair key, IntStringPair value) throws IOException {
			if (sentenceEnd == BreakIterator.DONE) {
				cur++;
				if (cur == content.size())
					return false;
				sentenceIterator.setText(content.get(cur));
				sentenceStart = 0;
				sentenceEnd = sentenceIterator.next();
			}
			String sentence = content.get(cur).substring(sentenceStart, sentenceEnd);
			value.set(wordCount, sentence);
			key.set(currentWordCount, file.toString());
			String[] words = sentence.split("\\s");
			currentWordCount += words.length;
			sentenceEnd = sentenceIterator.next();
			return true;
			
		}
		
	}
	
	@Override
	public RecordReader<IntStringPair, IntStringPair> getRecordReader(InputSplit arg0,
			JobConf arg1, Reporter arg2) throws IOException {
		
		return new SentenceRecordReader(arg0, arg1);

	}

}

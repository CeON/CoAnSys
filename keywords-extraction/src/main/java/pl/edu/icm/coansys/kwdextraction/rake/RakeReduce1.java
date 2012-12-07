/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.rake;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * Reducer
 * Input Pair is <word, keyword>
 *
 */
public class RakeReduce1 extends MapReduceBase implements	Reducer<Text, Text, Text, DoubleWritable> {

	@Override
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
		Set<String> keywords = new HashSet<String>();
		int freq = 0, deg = 0;
		String keyword = "";
		String[] words;
		while (values.hasNext()) {
			keyword = values.next().toString();
			words = keyword.split("\\s");
			freq++;
			deg += words.length;
			keywords.add(keyword);
		}
		
		double d = 0;
		d = (double) deg / (double) freq;
		for (String word : keywords) {
			String s = word.replaceAll("\\n", " ");
			output.collect(new Text(s), new DoubleWritable(d));
		}
	}

}

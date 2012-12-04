/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class SortMap extends MapReduceBase implements Mapper<LongWritable, Text, DoubleWritable, Text> {

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<DoubleWritable, Text> output, Reporter reporter)
			throws IOException {
		String line = value.toString();
		String[] spliLine = line.split("\\s");
		int len = spliLine.length;
		String s = "";
		double d = Double.parseDouble(spliLine[len - 1]) * (-1);
		for (int i = 0; i < len - 1; i++) {
			s = s.concat(spliLine[i].concat(" "));
		}
		s  = s.trim();
		
		output.collect(new DoubleWritable(d), new Text(s));
	}
}

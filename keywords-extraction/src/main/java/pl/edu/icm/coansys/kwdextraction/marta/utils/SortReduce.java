/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class SortReduce extends MapReduceBase implements Reducer<DoubleWritable, Text, Text, DoubleWritable> {

	@Override
	public void reduce(DoubleWritable key, Iterator<Text> values,
			OutputCollector<Text, DoubleWritable> output, Reporter reporter)
			throws IOException {
		key.set((-1)* key.get());
		while (values.hasNext()) {
			output.collect(values.next(), key);
		}
		
	}

}

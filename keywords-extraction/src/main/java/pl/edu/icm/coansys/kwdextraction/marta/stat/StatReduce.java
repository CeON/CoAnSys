/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import pl.edu.icm.coansys.kwdextraction.utils.IntWritablePair;

public class StatReduce extends MapReduceBase implements Reducer<Text, IntWritablePair, Text, DoubleWritable> {
	
	private int wordCount;
	private Integer sum (ArrayList<Integer> dist) {
		int s = 0;
		for (Integer k : dist)
			s+=k;
		return s;
	}
	private double mean (ArrayList<Integer> dist){
		return ((double) sum(dist)) / dist.size();
	}
	
	private double variance (ArrayList<Integer> dist) {
		double s = mean (dist);
		double r = 0, temp = 0;
		for (Integer k : dist) {
			temp =  k;
			r += ((temp - s) *  (temp - s));
		}
		return r/dist.size();
	}
	
	private double standardDeviation(ArrayList<Integer> dist) {
		return Math.sqrt(variance(dist));
	}
	
	private double countParam(ArrayList<Integer> dist){
		double d = dist.size();
		return d / wordCount;
	}
	
	private double countSigma (ArrayList<Integer> dist) {
		double p = countParam(dist);
		double sigma = standardDeviation(dist) / mean(dist);
		return sigma / (Math.sqrt(1 - p));
		
	}
	
	private ArrayList<Integer> countDistance(ArrayList<Integer> occ) {
		int prev = 0;
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (Integer k : occ) {
			result.add(k - prev);
			prev = k;
		}
		return result;
	}
	
	private double countWordWeight (ArrayList<Integer> occ) {
		ArrayList<Integer> distance = countDistance(occ);
		double sigma = countSigma(distance);
		double n = occ.size();
	
		double sigmaNorm = (2*n - 1) / (2*n +2);
		double sd = 1 / (Math.sqrt(n) * (1 + 2.8 * Math.pow(n, -0.865)));
		
		return (sigma - sigmaNorm) / sd;
	}

	@Override
	public void reduce(Text key, Iterator<IntWritablePair> values,
			OutputCollector<Text, DoubleWritable> output, Reporter reporter)
			throws IOException {
		  
		ArrayList<Integer> list = new ArrayList<Integer>();
		  
		double res;
		IntWritablePair p;
		while (values.hasNext()) {
			p = values.next();
			list.add(p.getFirst().get());
			wordCount = p.getSecond().get();
		}
		
		Collections.sort(list);

		res = countWordWeight(list);
		if (res > 0)
			output.collect(key, new DoubleWritable(res));
	}

}

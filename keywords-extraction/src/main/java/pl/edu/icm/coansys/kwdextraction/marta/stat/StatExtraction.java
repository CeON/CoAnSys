/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.stat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import pl.edu.icm.coansys.kwdextraction.utils.IntWritablePair;
import pl.edu.icm.coansys.kwdextraction.utils.SentenceInputFormat;
import pl.edu.icm.coansys.kwdextraction.utils.SortMap;
import pl.edu.icm.coansys.kwdextraction.utils.SortReduce;

public class StatExtraction {
	
	public int maxLength;
	public String pathToStopwords;
	public String outputPath;
	private ArrayList<Path> inputPaths = new ArrayList<Path>();
	
	/**
	 * @param p - Path object representing input directory
	 */
	public void addInputPath(Path p) {
		inputPaths.add(p);
	}
	
	public void addInputPaths(ArrayList<Path> p) {
		inputPaths.addAll(p);
	}
	
	
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	/**
	 * @param in - input Path
	 * @throws IOException
	 * 
	 * sorts extracted keywords in respect to their weighting
	 */
	private void sort (String in) throws IOException {
		JobConf sort = new JobConf();
		sort.setMapperClass(SortMap.class);
		sort.setReducerClass(SortReduce.class);
		sort.setMapOutputKeyClass(DoubleWritable.class);
		sort.setMapOutputValueClass(Text.class);
		sort.setOutputKeyClass(Text.class);
		sort.setOutputValueClass(DoubleWritable.class);
		FileInputFormat.addInputPath(sort, new Path(in));
		deleteDir(new File(outputPath));
		FileOutputFormat.setOutputPath(sort, new Path(outputPath));
		JobClient.runJob(sort);
	}
	
	/**
	 * finds keywords
	 * @throws IOException
	 */
	public void run () throws IOException {
		JobConf job = new JobConf();
		job.setMapperClass(StatMap.class);
		job.setReducerClass(StatReduce.class);
		job.setInputFormat(SentenceInputFormat.class);
		StatMap.PATH_TO_STOPWORDS = pathToStopwords;

		for (int i = 0; i < inputPaths.size(); i++) {
			FileInputFormat.addInputPath(job, inputPaths.get(i));
		}
		StatMap.MAX_LENGTH = maxLength;
		deleteDir(new File("out/"));
		FileOutputFormat.setOutputPath(job, new Path("out/"));
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritablePair.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		JobClient.runJob(job);
		
		sort("out/");
	}

}

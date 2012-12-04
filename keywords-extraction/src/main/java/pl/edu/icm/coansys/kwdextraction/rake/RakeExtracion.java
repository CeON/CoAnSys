/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.rake;

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
import pl.edu.icm.coansys.kwdextraction.utils.SentenceInputFormat;
import pl.edu.icm.coansys.kwdextraction.utils.SortMap;
import pl.edu.icm.coansys.kwdextraction.utils.SortReduce;


public class RakeExtracion {
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
		JobConf firstJob = new JobConf();
		firstJob.setMapperClass(RakeMap1.class);
		firstJob.setReducerClass(RakeReduce1.class);
		firstJob.setInputFormat(SentenceInputFormat.class);
		RakeMap1.PATH_TO_STOPWORDS = pathToStopwords;
		for (int i = 0; i < inputPaths.size(); i++) {
			FileInputFormat.addInputPath(firstJob, inputPaths.get(i));
		}
		RakeMap1.MAX_LENGTH = maxLength;
		deleteDir(new File("out/"));
		FileOutputFormat.setOutputPath(firstJob, new Path("out/"));
		firstJob.setMapOutputKeyClass(Text.class);
		firstJob.setMapOutputValueClass(Text.class);
		firstJob.setOutputKeyClass(Text.class);
		firstJob.setOutputValueClass(DoubleWritable.class);

		JobConf secJob = new JobConf();
		secJob.setMapperClass(RakeMap2.class);
		secJob.setReducerClass(RakeReduce2.class);
		deleteDir(new File("out2/"));
		FileInputFormat.addInputPath(secJob, new Path("out/"));
		FileOutputFormat.setOutputPath(secJob, new Path("out2/"));
		secJob.setMapOutputKeyClass(Text.class);
		secJob.setMapOutputValueClass(DoubleWritable.class);
		secJob.setOutputKeyClass(Text.class);
		secJob.setOutputValueClass(DoubleWritable.class);
		
		JobClient.runJob(firstJob);
		JobClient.runJob(secJob);
		
		sort("out2/");

	}
}

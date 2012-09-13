/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.commons.hbase;

import java.io.IOException;
import java.util.Date;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

/**
 *
 * @author akawa
 */
public class SequenceFileKeysSplitingAlgorithm {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        if (args.length < 2) {
            System.out.println("two parameters needed: <sequence-file, number-of-regions>");
            System.exit(1);
        }
        
        
        String file = args[0];
        int regionNum = Integer.parseInt(args[1]);
        
        
        long date = new Date().getTime();

        Configuration conf = new Configuration();
        Job job = new Job(conf);

        Path input = new Path(file);
        input = input.makeQualified(input.getFileSystem(conf));

        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, input);
        SequenceFileOutputFormat.setOutputPath(job, new Path(file + date));

        job.setNumReduceTasks(regionNum);
        job.setOutputKeyClass(BytesWritable.class);
        job.setOutputKeyClass(BytesWritable.class);
        job.setPartitionerClass(TotalOrderPartitioner.class);

        InputSampler.Sampler<BytesWritable, BytesWritable> sampler =
                new InputSampler.RandomSampler<BytesWritable, BytesWritable>(0.5, 10000, 10);

        Path partitionFile = new Path(input, "_partitions" + date);
        TotalOrderPartitioner.setPartitionFile(conf, partitionFile);
        InputSampler.writePartitionFile(job, sampler);

        boolean success = job.waitForCompletion(true);
        if (!success) {
            throw new IOException("Error with job!");
        }
    }
}

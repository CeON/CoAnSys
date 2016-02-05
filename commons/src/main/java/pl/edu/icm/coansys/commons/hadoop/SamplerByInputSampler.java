/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.commons.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author akawa
 */
public class SamplerByInputSampler implements Tool {

    private Configuration conf;

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    @Override
    public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        return createParitionFile(args[0], args[1], Float.parseFloat(args[2]), Integer.parseInt(args[3]));
    }

    private int createParitionFile(String inputPath, String outputFile, float frequency, int samplesCnt) throws IOException, ClassNotFoundException, InterruptedException {
        Path input = new Path(inputPath);
        Job sampler = new Job(getConf());
        
        TextInputFormat.addInputPath(sampler, input);
        InputSampler.Sampler<LongWritable, Text> inputSampler =
                new InputSampler.RandomSampler<LongWritable, Text>(frequency, samplesCnt);


        Path partitionFile = new Path(outputFile);
        TotalOrderPartitioner.setPartitionFile(sampler.getConfiguration(), partitionFile);
        InputSampler.writePartitionFile(sampler, inputSampler);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 4) {
            usage("Wrong number of arguments: " + otherArgs.length);
            System.exit(-1);
        }

        int result = ToolRunner.run(conf, new SamplerByInputSampler(), otherArgs);
        System.exit(result);
    }

    private static void usage(String info) {
        System.out.println(info);
        System.out.println("Four parameters needed: <input-path, output-file, frequency, samples-cnt>");
        System.out.println("Example: hadoop jar target/commons-1.0-SNAPSHOT.jar " + SamplerByInputSampler.class.getName() + " <input-path, output-file, frequency, samples-cnt>");
    }
}

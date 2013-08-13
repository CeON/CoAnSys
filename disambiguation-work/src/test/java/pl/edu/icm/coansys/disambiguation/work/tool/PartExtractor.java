/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

package pl.edu.icm.coansys.disambiguation.work.tool;

import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Extracts and writes the given percent of records from the given sequence file
 * @author Łukasz Dumiszewski
 *
 */

public class PartExtractor extends Configured implements Tool {
    
    private static Logger log = LoggerFactory.getLogger(PartExtractor.class);
    
    
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new PartExtractor(), args);
        System.exit(res);
    }
    
    @Override
    public int run(String[] args) throws Exception {
        
        
        if (args.length < 3) {
            log.error("usage: PartExtractingJob fileInputPath outputDirectoryPath percentOfWritten");
            System.exit(1);
        }
        
        int percentOfWritten = Integer.parseInt(args[2]);
        
        if (percentOfWritten<0 || percentOfWritten > 100) {
            log.error("invalid value of percentOfWritten");
            System.exit(1);
        }
         
        Job job = new Job(getConf(), "Part extracting");
       
        
        job.getConfiguration().setInt("percentOfWritten", percentOfWritten);
        
        job.setJarByClass(getClass());
        
        job.setMapperClass(PartExtractingMapper.class);
        //job.setMapperClass(CharTokenizingMapper.class); // uncomment if you want to count characters
        //job.setCombinerClass(IntSumReducer.class);
        //job.setReducerClass(IntSumReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
        
        String outputDir = args[1] + new Date().getTime();
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(outputDir));
        
       
        
        boolean success = job.waitForCompletion(true);
        
        return success ? 0 : 1;
    
    }
}

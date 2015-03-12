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

package pl.edu.icm.coansys.deduplication.document.tool;

import java.io.File;

import org.apache.commons.io.FileUtils;
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


public class DuplicateGenerator  extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(DuplicateGenerator.class);
    
    
    public static void main(String[] args) throws Exception {
        checkArguments(args);
        ToolRunner.run(new Configuration(), new DuplicateGenerator(), args);
        
    }

    
    @Override
    public int run(String[] args) throws Exception {
        checkArguments(args);
        
        String inputFile = args[0];
        
        String baseOutputDir = args[1];
        String jobOutputDir = baseOutputDir + "generated/duplicated-works";
        
        FileUtils.deleteDirectory(new File(jobOutputDir));

        Job job = new Job(getConf(), "duplicateGenerator");
        job.setJarByClass(getClass());
        
        job.setMapperClass(DuplicateGenerateMapper.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(inputFile));
        
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(jobOutputDir));
        
        job.setNumReduceTasks(1);
        
        boolean success = job.waitForCompletion(true);
        
        FileUtils.copyFile(new File(jobOutputDir+"/part-r-00000"), new File(baseOutputDir+"/generated/ambiguous-publications.seq"));
        
        FileUtils.deleteDirectory(new File(jobOutputDir));
        
        
        return success ? 0 : 1;
    
    }
    

    //******************** PRIVATE ********************
    
    private static void checkArguments(String[] args) {
        if (args.length < 2) {
            log.error("Missing arguments.");
            log.error("Usage: DuplicateGenerator inputFile outputDir");
            System.exit(1);
        }
        
        //Preconditions.checkArgument(new File(args[0]).exists(), args[0] + " does not exist");
        
    }

}

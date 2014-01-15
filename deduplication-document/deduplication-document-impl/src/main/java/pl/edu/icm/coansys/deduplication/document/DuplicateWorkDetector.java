/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.deduplication.document;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
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

import pl.edu.icm.coansys.commons.spring.DiMapper;
import pl.edu.icm.coansys.commons.spring.DiReducer;

import com.google.common.base.Preconditions;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class DuplicateWorkDetector extends Configured implements Tool {

    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetector.class);
    
    
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new DuplicateWorkDetector(), args);
        System.exit(res);
    }

    
    @Override
    public int run(String[] args) throws Exception {
        checkArguments(args);
        
        String inputFile = args[0];
        
        String jobOutputDir = args[1];
        
        getConf().set(DiMapper.DI_MAP_APPLICATION_CONTEXT_PATH, "spring/applicationContext.xml");
        getConf().set(DiMapper.DI_MAP_SERVICE_BEAN_NAME, "duplicateWorkDetectMapService");
        getConf().set(DiReducer.DI_REDUCE_APPLICATION_CONTEXT_PATH, "spring/applicationContext.xml");
        getConf().set(DiReducer.DI_REDUCE_SERVICE_BEAN_NAME, "duplicateWorkDetectReduceService");
        
        
        getConf().set("dfs.client.socket-timeout", "120000");
        getConf().setInt("mapred.task.timeout", 1200000);
        getConf().set("mapred.child.java.opts", "-Xmx4096m");
        
        Job job = new Job(getConf(), "duplicateWorkDetector");
        
        job.setNumReduceTasks(8);
        
        job.setJarByClass(getClass());
        
        job.setMapperClass(DiMapper.class);
        job.setReducerClass(DiReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.addInputPath(job, new Path(inputFile));
        
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job, new Path(jobOutputDir));
        
        boolean success = job.waitForCompletion(true);
        
        return success ? 0 : 1;
    
    }
    

    //******************** PRIVATE ********************
    
    private void checkArguments(String[] args) throws IOException {
        if (args.length < 2) {
            log.error("Missing arguments.");
            log.error("Usage: DuplicateWorkDetector inputFile outputDir");
            return;
        }
        FileSystem hdfs = FileSystem.get(getConf());
        Path path = new Path(args[0]);
        Preconditions.checkArgument(hdfs.exists(path), args[0] + " does not exist");
        
    }

}

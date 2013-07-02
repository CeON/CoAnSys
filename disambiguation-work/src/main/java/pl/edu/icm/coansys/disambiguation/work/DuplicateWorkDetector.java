package pl.edu.icm.coansys.disambiguation.work;

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
        getConf().setInt(DuplicateWorkDetectMapService.KEY_LENGTH, 5);
        
        getConf().set("dfs.client.socket-timeout", "120000");
        
        @SuppressWarnings("deprecation")
        Job job = new Job(getConf(), "duplicateWorkDetector");
        
        job.setNumReduceTasks(8);
        
        job.setJarByClass(getClass());
        
        job.setMapperClass(DiMapper.class);
        job.setReducerClass(DiReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        
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
            System.exit(1);
        }
        FileSystem hdfs = FileSystem.get(getConf());
        Path path = new Path(args[0]);
        Preconditions.checkArgument(hdfs.exists(path), args[0] + " does not exist");
        
    }

}

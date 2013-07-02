package pl.edu.icm.coansys.disambiguation.work.tool;

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

import com.google.common.base.Preconditions;


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
        String jobOutputDir = baseOutputDir + "/duplicated-works";
        
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
        
        
        boolean success = job.waitForCompletion(true);
        
        FileUtils.copyFile(new File(jobOutputDir+"/part-r-00000"), new File(baseOutputDir+"/ambiguous-publications.seq"));
        
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
        
        Preconditions.checkArgument(new File(args[0]).exists(), args[0] + " does not exist");
        
    }

}

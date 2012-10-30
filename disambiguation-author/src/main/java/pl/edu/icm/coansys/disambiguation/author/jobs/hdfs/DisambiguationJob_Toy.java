/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.jobs.hdfs;

import java.lang.management.ManagementFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DisambiguationJob_Toy extends Configured implements Tool {

    private static Logger logger = Logger.getLogger(LoggingInDisambiguation.class);
    private Configuration conf;
    /*
     * Fields to be set
     */
    private String inputPath = null;
    private String outputDir = null;
    private String featuresDescription = null;
    private String jobName = null;
    private String threshold = null;
    private int reducerNumber = 65;

    /*
     * Getters and setters
     */
    public String getInputPath() {
        return inputPath;
    }

    public DisambiguationJob_Toy setInputPath(String ip) {
        inputPath = ip;
        return this;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public DisambiguationJob_Toy setOutputDir(String od) {
        outputDir = od;
        return this;
    }

    public String getName() {
        return jobName;
    }

    public DisambiguationJob_Toy setName(String name) {
        jobName = name;
        return this;
    }

    public String getFEATURES_DESCRIPTION() {
        return featuresDescription;
    }

    public DisambiguationJob_Toy setFEATURES_DESCRIPTION(String fEATURES_DESCRIPTION) {
        featuresDescription = fEATURES_DESCRIPTION;
        return this;
    }

    public DisambiguationJob_Toy setReducerNumber(int rn) {
        reducerNumber = rn;
        return this;
    }

    public DisambiguationJob_Toy setTHRESHOLD(String tHRESHOLD) {
        threshold = tHRESHOLD;
        return this;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    private void parseArgs(String[] args) {
        if (args == null || args.length != 5) {
            logger.debug("# of parameters is not equal to 5");
            logger.debug("You need to provide:");
            logger.debug("* an input table name");
            logger.debug("* an output table name");
            logger.debug("* a feature description string, e.g. FeatureXName#FeatureXExtractorName#FeatureXWeight#FeaturXMaxValue[,FeatureYName#FeatureYExtractorName#FeatureYWeight#FeaturYMaxValue]    ");
            logger.debug("* a disambiguation threshold");
            logger.debug("* a job name");
            logger.debug("");
            logger.debug("Default values will be used:");
            logger.debug("* grotoap10_dump/mproto-m-00000");
            logger.debug("* grotoap10_disambig");
            logger.debug("* EmailDisambiguator#DocumentProto2EmailExtractor#0.81#1,"
                    + "KeywordDisambiguator#DocumentProto2KeyWordExtractor#0.13#33");
            logger.debug("* -0.846161134713438d");
            logger.debug("* DisambiguationJob_Toy");

            args = new String[5];
            //args[0] = "grotoap10";
            args[0] = "grotoap10_dump/mproto-m-00000";
            args[1] = "grotoap10_disambig";
            args[2] = "EmailDisambiguator#DocumentProto2EmailExtractor#0.81#1,"
                    + "KeywordDisambiguator#DocumentProto2KeyWordExtractor#0.13#33";
            args[3] = "-0.846161134713438d";
            args[4] = "DisambiguationJob_Toy";
        }

        setInputPath(args[0]);
        setOutputDir(args[1]);
        setFEATURES_DESCRIPTION(args[2]);
        setTHRESHOLD(args[3]);
        setName(args[4]);
    }

    /*
     * Job configuration and ignition
     */
    @Override
    public int run(String[] args) throws Exception {
        parseArgs(args);
        
        Configuration config = getConf();
        config.set("FEATURE_DESCRIPTION", featuresDescription);
        config.set("THRESHOLD", threshold);
        
        //job creation
        Job job = new Job(config);
        job.setJobName(jobName + " (input: " + inputPath + ")");
        job.setJarByClass(DisambiguationJob_Toy.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        SequenceFileInputFormat.setInputPaths(job, inputPath);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TextTextArrayMapWritable.class);
        
        job.setMapperClass(FeaturesExtractionMapper_Toy.class);
        //consume results of _FeaturesExtractionMapper_Toy 
        //by reduce function from FeaturesMergeShardReducer
        job.setReducerClass(ClusterDisambiguationReducer_Toy.class);
        job.setNumReduceTasks(reducerNumber);
        //setting output parameters
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(outputDir));

        /*
         * Launch job
         */
        long startTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        boolean success = job.waitForCompletion(true);
        long endTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        double duration = (endTime - startTime) / Math.pow(10, 9);
        logger.info("=== Job1 Finished in " + duration + " seconds " + (success ? "(success)" : "(failure)"));
        return success ? 0 : 1;
    }

    /*
     * The Main method
     */
    public static void main(String args[]) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        DisambiguationJob_Toy job = new DisambiguationJob_Toy();        
        int result = ToolRunner.run(conf, job, args);
        logger.debug("=== Job End ===");
        System.exit(result);
    }
}
/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.jobs;

import java.lang.management.ManagementFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.importers.constants.HBaseConstant;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DisambiguationJob_Toy implements Tool {

    /*
     * 
     * Inner fields
     * 
     */
    
    private static Logger logger = Logger.getLogger(LoggingInDisambiguation.class);
    private Configuration conf;
	
	/*
	 * 
	 * Fields to be set
	 * 
	 */
	
	String INPUT_TABLE = null;
	String OUTPUT_TABLE = null;
    String FEATURES_DESCRIPTION = null;
    
    String NAME = null;
	String THRESHOLD = null;
	int REDUCER_NUM = 65;
	
	
	/*
	 * 
	 * Getters and setters
	 * 
	 */
	
	public String getINPUT_TABLE() {
		return INPUT_TABLE;
	}

	public DisambiguationJob_Toy setINPUT_TABLE(String iNPUT_TABLE) {
		INPUT_TABLE = iNPUT_TABLE;
		return this;
	}

	public String getOUTPUT_TABLE() {
		return OUTPUT_TABLE;
	}

	public DisambiguationJob_Toy setOUTPUT_TABLE(String oUTPUT_TABLE) {
		OUTPUT_TABLE = oUTPUT_TABLE;
		return this;
	}
    
    public String getNAME() {
        return NAME;
    }

    public DisambiguationJob_Toy setNAME(String nAME) {
        NAME = nAME;
        return this;
    }

    public String getFEATURES_DESCRIPTION() {
		return FEATURES_DESCRIPTION;
	}

	public DisambiguationJob_Toy setFEATURES_DESCRIPTION(String fEATURES_DESCRIPTION) {
		FEATURES_DESCRIPTION = fEATURES_DESCRIPTION;
		return this;
	}

    public DisambiguationJob_Toy setREDUCER_NUM(int rEDUCER_NUM) {
        REDUCER_NUM = rEDUCER_NUM;
        return this;
    }

    public DisambiguationJob_Toy setTHRESHOLD(String tHRESHOLD) {
        THRESHOLD = tHRESHOLD;
        return this;
    }
    
    @SuppressWarnings("unused")
    private static String gedPaddedNumber(int i) {
        return String.format("%010d", i);
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
    		logger.debug("* testProto");
    		logger.debug("* disambigTest");
    		logger.debug("* EmailFeature#DocumentProto2EmailExtractor#0.81#1," +
        		"KeywordFeature#DocumentProto2KeyWordExtractor#0.13#33");
    		logger.debug("* -0.846161134713438d");
    		logger.debug("* DisambiguationJob_Toy");
    		
    		args = new String[5];
    		args[0] = "testProto";
    		args[1] = "disambigTest"; 
    		args[2] = "EmailDisambiguator#DocumentProto2EmailExtractor#0.81#1," +
        		"KeywordDisambiguator#DocumentProto2KeyWordExtractor#0.13#33";
    		args[3] = "-0.846161134713438d";
    		args[4] = "DisambiguationJob_Toy";
        }
        	
        setINPUT_TABLE(args[0]);
        setOUTPUT_TABLE(args[1]);
        setFEATURES_DESCRIPTION(args[2]);
        setTHRESHOLD(args[3]);
        setNAME(args[4]);
    }

    /*
     * 
     * Job configuration and ignition
     * 
     */
    
    @Override
    public int run(String[] args) throws Exception {  	
    	
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        parseArgs(otherArgs);
    	
    	
        /*
         * First job configuration
         */
        conf.set("FEATURE_DESCRIPTION", FEATURES_DESCRIPTION);
        conf.set("THRESHOLD", THRESHOLD);
        
        //setting input table
        conf.set(TableInputFormat.INPUT_TABLE, INPUT_TABLE);
        //setting output table        
        conf.set(TableOutputFormat.OUTPUT_TABLE, OUTPUT_TABLE);
        //job creation
        Job job = new Job(conf);
        job.setJobName(NAME + " (input: " + INPUT_TABLE + ")");
        job.setJarByClass(DisambiguationJob_Toy.class);
        //scan for relevant data
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(HBaseConstant.FAMILY_METADATA),
        		Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO));
        //scan additional parameters
        scan.setCaching(1000);
        scan.setCacheBlocks(false);
        //initial map job ( from class _FeaturesExtractionMapper_Toy ) 
        //on data from scan
        TableMapReduceUtil.initTableMapperJob(INPUT_TABLE, scan,
                FeaturesExtractionMapper_Toy.class, Text.class, TextTextArrayMapWritable.class,
                job);
        //consume results of _FeaturesExtractionMapper_Toy 
        //by reduce function from FeaturesMergeShardReducer
        job.setReducerClass(ClusterDisambiguationReducer_Toy.class);
        job.setNumReduceTasks(REDUCER_NUM);
        //setting output parameters
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Put.class);
        job.setOutputFormatClass(TableOutputFormat.class);
        
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
     * 
     * The Main method
     * 
     */
    
    public static void main(String args[]) throws Exception{
        Configuration conf = HBaseConfiguration.create();
        DisambiguationJob_Toy job = new DisambiguationJob_Toy();
        int result = ToolRunner.run(conf, job , args);
        logger.debug("=== Job End ===");
        System.exit(result);
    }
}
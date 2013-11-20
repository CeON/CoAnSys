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

package pl.edu.icm.coansys.disambiguation.author.jobs;

import java.io.IOException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.models.constants.HBaseConstant;

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
    private static Logger logger = LoggerFactory.getLogger(DisambiguationJob_Toy.class);
    private Configuration conf;
    /*
     *
     * Fields to be set
     *
     */
    private String INPUT_TABLE = null;
    private String OUTPUT_TABLE = null;
    private String FEATURES_DESCRIPTION = null;
    private String NAME = null;
    private String THRESHOLD = null;
    private int REDUCER_NUM = 65;

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

    /*
     * @SuppressWarnings("unused") private static String gedPaddedNumber(int i)
     * { return String.format("%010d", i);
    }
     */
    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    private void parseArgs(String[] args) {
        String[] tmp = new String[5];
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
            logger.debug("* EmailDisambiguator#DocumentProto2EmailExtractor#0.81#1,"
                    + "KeywordDisambiguator#DocumentProto2KeyWordExtractor#0.13#33");
            logger.debug("* -0.846161134713438d");
            logger.debug("* DisambiguationJob_Toy");

            tmp[0] = "testProto";
            tmp[1] = "disambigTest";
            tmp[2] = "EmailDisambiguator#DocumentProto2EmailExtractor#0.81#1,"
                    + "KeywordDisambiguator#DocumentProto2KeyWordExtractor#0.13#33";
            tmp[3] = "-0.846161134713438d";
            tmp[4] = "DisambiguationJob_Toy";
        }
        else {
            tmp[0] = args[0];
            tmp[1] = args[1];
            tmp[2] = args[2];
            tmp[3] = args[3];
            tmp[4] = args[4];
        }

        setINPUT_TABLE(tmp[0]);
        setOUTPUT_TABLE(tmp[1]);
        setFEATURES_DESCRIPTION(tmp[2]);
        setTHRESHOLD(tmp[3]);
        setNAME(tmp[4]);
    }

    /*
     *
     * Job configuration and ignition
     *
     */
    @Override
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

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
    public static void main(String args[]) {
        Configuration conf = HBaseConfiguration.create();
        DisambiguationJob_Toy job = new DisambiguationJob_Toy();
        int result;
		try {
			result = ToolRunner.run(conf, job, args);
	        logger.debug("=== Job End ===");
	        System.exit(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception ");
			e.printStackTrace();
		}
    }
}

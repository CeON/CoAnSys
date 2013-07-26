/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.classification.documents.auxil.LoggingInClassification;
import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;
import pl.edu.icm.coansys.models.constants.HBaseConstant;

/**
*
* @author pdendek
*/
public class TfidfJob_Proto implements Tool {
    /*
     *
     * Inner fields
     *
     */

    private static Logger logger = LoggerFactory.getLogger(LoggingInClassification.class);
    private Configuration conf;
    /*
     *
     * Fields to be set
     *
     */
    private String INPUT_TABLE = null;
    private String AUXIL_PATH = null;
    private String FINAL_PATH = null;
    private String NAME = null;
    private int DOCS_NUM = 0;
    private int REDUCER_NUM = 65;

    /*
     *
     * Getters and setters
     *
     */
    public String getINPUT_TABLE() {
        return INPUT_TABLE;
    }

    public TfidfJob_Proto setINPUT_TABLE(String iNPUT_TABLE) {
        INPUT_TABLE = iNPUT_TABLE;
        return this;
    }

    public String getAUXIL_PATH() {
        return AUXIL_PATH;
    }

    public TfidfJob_Proto setAUXIL_PATH(String aUXIL_PATH) {
        AUXIL_PATH = aUXIL_PATH;
        if (!AUXIL_PATH.endsWith("/")) {
            AUXIL_PATH += "/";
        }
        return this;
    }

    public String getNAME() {
        return NAME;
    }

    public TfidfJob_Proto setNAME(String nAME) {
        NAME = nAME;
        return this;
    }

    public TfidfJob_Proto setFINAL_PATH(String fINAL_PATH) {
        FINAL_PATH = fINAL_PATH;
        return this;
    }

    public TfidfJob_Proto setREDUCER_NUM(int rEDUCER_NUM) {
        REDUCER_NUM = rEDUCER_NUM;
        return this;
    }

    public int getDOCS_NUM() {
        return DOCS_NUM;
    }

    public void setDOCS_NUM(int dOCS_NUM) {
        DOCS_NUM = dOCS_NUM;
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

    public Configuration getConf() {
        return conf;
    }

    private void parseArgs(String[] args) {
        String[] tmp = new String[4];
        if (args == null || args.length != 3) {
            logger.debug("# of parameters is not equal to 4");
            logger.debug("You need to provide:");
            logger.debug("* an input table name");
            logger.debug("* an auxiliar path for intermediate result");
            logger.debug("* a final path with results");
            logger.debug("* a job name");
            logger.debug("");
            logger.debug("Default values will be used:");
            logger.debug("* testProto");
            logger.debug("* /user/pdendek/tfidf/");
            logger.debug("* TfidfJob_Proto");

            tmp[0] = "testProto";
            tmp[1] = "/home/pdendek/tfidf/intermediate";
            tmp[2] = "/user/pdendek/tfidf/final";
            tmp[3] = "TfidfJob_Proto";
        }
        else {
            tmp[0] = args[0];
            tmp[1] = args[1];
            tmp[2] = args[2];
            tmp[3] = args[3];
        }

        setINPUT_TABLE(tmp[0]);
        setAUXIL_PATH(tmp[1]);
        setFINAL_PATH(tmp[2]);
        setNAME(tmp[3]);
    }

    /*
     *
     * Job configuration and ignition
     *
     */
    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        /////////////////////////// CONSUME ARGS /////////////////////////////
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        parseArgs(otherArgs);
        /////////////////////////// JOB 1 /////////////////////////////
        if (!firstJobExecution(args)) {
            return 1;
        }
        /////////////////////////// JOB 2 /////////////////////////////
        if (!secondJobExecution(args)) {
            return 2;
        }
        /////////////////////////// JOB 3 /////////////////////////////
        return thirdJobExecution(args) ? 0 : 3;
    }

    private boolean firstJobExecution(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        conf.clear();
        new GenericOptionsParser(conf, args);
        //setting input table
        conf.set(TableInputFormat.INPUT_TABLE, INPUT_TABLE);
        //job creation
        Job wordCountJob = new Job(conf);
        wordCountJob.setJobName(NAME + " WordCount");
        wordCountJob.setJarByClass(TfidfJob_Proto.class);
        //scan for relevant data
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes(HBaseConstant.FAMILY_METADATA),
                Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO));
        //scan additional parameters
        scan.setCaching(1000);
        scan.setCacheBlocks(false);
        //initial map job (wordCountMapper) 
        //on data from scan
        TableMapReduceUtil.initTableMapperJob(INPUT_TABLE, scan,
                WordCountMapper_Proto.class, TextArrayWritable.class, IntWritable.class,
                wordCountJob);
        //consume results the mapper
        wordCountJob.setNumReduceTasks(REDUCER_NUM);
        wordCountJob.setReducerClass(WordCountReducer.class);
        wordCountJob.setOutputKeyClass(TextArrayWritable.class);
        wordCountJob.setOutputValueClass(IntWritable.class);
        wordCountJob.setOutputFormatClass(SequenceFileOutputFormat.class);
        //setting output parameters        
        SequenceFileOutputFormat.setOutputPath(wordCountJob, new Path(AUXIL_PATH + "job1"));
        /*
         * Launch job
         */
        long startTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        boolean success = wordCountJob.waitForCompletion(true);
        long endTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        double duration = (endTime - startTime) / Math.pow(10, 9);
        logger.info("=== Job1 Finished in " + duration + " seconds " + (success ? "(success)" : "(failure)"));

        int docs_num = calculateDocsNum(wordCountJob);
        setDOCS_NUM(docs_num);

        return success;
    }

    private int calculateDocsNum(Job wordCountJob) throws IOException, InterruptedException {
        org.apache.hadoop.mapreduce.Counters cs = wordCountJob.getCounters();
        org.apache.hadoop.mapreduce.CounterGroup cg = cs.getGroup("org.apache.hadoop.mapreduce.TaskCounter");
        org.apache.hadoop.mapreduce.Counter cr = cg.findCounter("MAP_INPUT_RECORDS");
        return (int) cr.getValue();
    }

    private boolean secondJobExecution(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        conf.clear();
        new GenericOptionsParser(conf, args);

        Job wordPerDocJob = new Job(conf);
        wordPerDocJob.setJobName(NAME + " WordPerDocCount");
        wordPerDocJob.setJarByClass(TfidfJob_Proto.class);

        wordPerDocJob.setMapperClass(WordPerDocCountMapper.class);
        wordPerDocJob.setMapOutputKeyClass(Text.class);
        wordPerDocJob.setMapOutputValueClass(StringListIntListWritable.class);
        wordPerDocJob.setInputFormatClass(SequenceFileInputFormat.class);

        wordPerDocJob.setReducerClass(WordPerDocCountReducer.class);
        wordPerDocJob.setOutputKeyClass(TextArrayWritable.class);
        wordPerDocJob.setOutputValueClass(StringListIntListWritable.class);
        wordPerDocJob.setOutputFormatClass(SequenceFileOutputFormat.class);


        SequenceFileInputFormat.addInputPath(wordPerDocJob, new Path(AUXIL_PATH + "job1"));
        SequenceFileOutputFormat.setOutputPath(wordPerDocJob, new Path(AUXIL_PATH + "job2"));

        /*
         * Launch job
         */
        long startTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        boolean success = wordPerDocJob.waitForCompletion(true);
        long endTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        double duration = (endTime - startTime) / Math.pow(10, 9);
        logger.info("=== Job1 Finished in " + duration + " seconds " + (success ? "(success)" : "(failure)"));
        return success;
    }

    private boolean thirdJobExecution(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        conf.clear();
        new GenericOptionsParser(conf, args);
        conf.set("DOCS_NUM", getDOCS_NUM() + "");

        Job tfidfJob = new Job(conf);
        tfidfJob.setJobName(NAME + " Tfidf");
        tfidfJob.setJarByClass(TfidfJob_Proto.class);

        tfidfJob.setMapperClass(TfidfMapper.class);
        tfidfJob.setMapOutputKeyClass(Text.class);
        tfidfJob.setMapOutputValueClass(StringListIntListWritable.class);
        tfidfJob.setInputFormatClass(SequenceFileInputFormat.class);

        tfidfJob.setReducerClass(TfidfReducer.class);
        tfidfJob.setOutputKeyClass(TextArrayWritable.class);
        tfidfJob.setOutputValueClass(DoubleWritable.class);
        tfidfJob.setOutputFormatClass(SequenceFileOutputFormat.class);

        SequenceFileInputFormat.addInputPath(tfidfJob, new Path(AUXIL_PATH + "job2"));
        SequenceFileOutputFormat.setOutputPath(tfidfJob, new Path(FINAL_PATH + (int)(Math.random() * Integer.MAX_VALUE)));

        /*
         * Launch job
         */
        long startTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        boolean success = tfidfJob.waitForCompletion(true);
        long endTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
        double duration = (endTime - startTime) / Math.pow(10, 9);
        logger.info("=== Job1 Finished in " + duration + " seconds " + (success ? "(success)" : "(failure)"));
        return success;
    }

    /*
     *
     * The Main method
     *
     */
    public static void main(String args[]) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        TfidfJob_Proto job = new TfidfJob_Proto();
        int result = ToolRunner.run(conf, job, args);
        logger.debug("=== Job End ===");
        System.exit(result);
    }
}

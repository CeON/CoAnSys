package pl.edu.icm.coansys.disambiguation.author;


import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SmallTest{
	
    private PigTest test;
    private static Cluster cluster;
    public String[] script;
    
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    public final static String TST_RESOURCES_DIR = "src/test/resources/";
    
	private final static String testScriptPath = PIG_SCRIPT_DIR+"small.pig";
	private final static String testInputPath = TST_RESOURCES_DIR + "newDisambiguation/" + "bazekon_secret.sf";
	private final static String testOutput = TST_RESOURCES_DIR + "tmp/" + "small_" + (new Date()).getTime() + "_secret.tmp";
	private final static String testExpectedOutputPath = TST_RESOURCES_DIR + "newDisambiguation/" + "small.out";
		
	private final static String[] params = {
			"dc_m_hdfs_inputDocsData=null",// + testInputPath,
			"dc_m_hdfs_outputContribs=null",// + testOutput,
	        "tfidfPath=null",
	        "outputPath=null",
	        "commonJarsPath=.",
	        "parallel=1"
	};
	
    @BeforeClass
    public static void beforeClass() throws Exception {
        cluster = PigTest.getCluster();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        cluster.delete(new Path("pigunit-input-overriden.txt"));
    }
	
	@Test
	public void smallTest() throws IOException, ParseException {
		
		LinkedList<String> script = PigScriptExtractor.extract(testScriptPath); 
		script.addFirst("REGISTER 'target/*.jar'");
		test = new PigTest(script.toArray(new String[]{}), params);
		
		String[] in = {
				"k1\tm1",
				"k2\tm2",
				"k3\tm3"
		};
		
		String[] out = {
				"(k1)",
				"(k2)",
				"(k3)"
		};
		
		test.assertOutput("A1",in,"A3",out);
//		test.assertOutput(new File(testExpectedOutputPath));
	}
}

package pl.edu.icm.coansys.disambiguation.author;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.Test;

public class SmallTest{
	
    private PigTest test;

    //private static Cluster cluster;
    public String[] script;
    
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    public final static String TST_RESOURCES_DIR = "src/test/resources/";
    
	private final static String testScriptPath = PIG_SCRIPT_DIR+"small.pig";
		
	private final static String[] params = {
			"dc_m_hdfs_inputDocsData=null",
			"dc_m_hdfs_outputContribs=null",
	        "commonJarsPath=.",
	        "parallel=1"
	};

	/*
    @BeforeClass
    public static void beforeClass() throws Exception {
        cluster = PigTest.getCluster();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        cluster.delete(new Path("pigunit-input-overriden.txt"));
    }
	*/

	@Test(groups = {"fast"})
	public void smallTest() throws IOException, ParseException {
		LinkedList<String> script = PigScriptExtractor.extract(testScriptPath); 
		test = new PigTest(script.toArray(new String[]{}), params);
		String[] in = {"k1\tm1","k2\tm2","k3\tm3"};
		String[] out = {"(k1)","(k2)","(k3)"};
		test.assertOutput("A1",in,"A3",out);
	}
}

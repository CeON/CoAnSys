/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.script;

import java.io.IOException;
import java.util.LinkedList;
import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.edu.icm.coansys.similarity.test.utils.PigScriptExtractor;

/**
 *
 * @author akawa
 */
public class TestAllpairsCosineSimilarity {

    private PigTest test;
    private static Cluster cluster;
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    private static final String[] params = {
        "tfidfPath=null",
        "outputPath=null",
        "commonJarsPath=."
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
    public void testSingle() throws IOException, ParseException {

        LinkedList<String> script = PigScriptExtractor.extract(PIG_SCRIPT_DIR + "allpairs_similarity.pig");
        test = new PigTest(script.toArray(new String[]{}), params);

        String[] input = {
            "d1\tt1\t1",
            "d1\tt2\t2",
            "d2\tt1\t3",
            "d2\tt2\t4"
        };

        // verify intermdiate data
        String[] similaritiesOutput = {
            "(d1,d2," + (3d + 8d) / (Math.sqrt(5) * Math.sqrt(9+16)) + ")"
        };
        test.assertOutput("TFIDF", input, "S1", similaritiesOutput);    
    }
}
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
import pl.edu.icm.coansys.similarity.test.utils.MacroExtractor;

/**
 *
 * @author akawa
 */


public class TestTFIDF {

    private PigTest test;
    private static Cluster cluster;
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";

    @BeforeClass
    public static void beforeClass() throws Exception {
        cluster = PigTest.getCluster();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        cluster.delete(new Path("pigunit-input-overriden.txt"));
    }

    @Test
    public void testTFIDF() throws IOException, ParseException {

        // get the content of calculate_tf_idf macro from macros.pig file
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "calculate_tf_idf");
        // add LOAD to the script (in fact String[] input will be used as input data)
        macro.addFirst("docTerm = LOAD 'ommited' AS (docId, term);");
        String[] script = macro.toArray(new String[]{});
        
        // set values for macro parameters
        final String[] params = {
            "docTerm=docTerm",
            "tfidf=tfidfResults"
        };

        test = new PigTest(script, params);
        String[] input = {
            "d1\tt1",
            "d1\tt1",
            "d2\tt1",
            "d3\tt2",};

        // adding total number of documents that a given word occurs in
        String[] outputE = {
            "(d1,t1,2,2,3,2)",
            "(d2,t1,1,1,3,2)",
            "(d3,t2,1,1,3,1)"
        };
        test.assertOutput("docTerm", input, "E", outputE);

        // verify tfidf results
        String[] tfidfOutput = {
            "(d1,t1," + (2d / 2d) * Math.log((1d + 3d) / (1d + 2d)) + ")",
            "(d2,t1," + (1d / 1d) * Math.log((1d + 3d) / (1d + 2d)) + ")",
            "(d3,t2," + (1d / 1d) * Math.log((1d + 3d) / (1d + 1d)) + ")",};

        test.assertOutput("docTerm", input, "tfidfResults", tfidfOutput);
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.script;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pl.edu.icm.coansys.similarity.test.utils.MacroExtractor;

/**
 *
 * @author akawa
 */
public class TestTFIDF {

    private PigTest test;
    private static Cluster cluster;
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    private static String[] script;
    private static String[] params;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cluster = PigTest.getCluster();
        script = getScriptToTestTfIdfMacro();
        params = getDefaultParams();
        
    }

    @AfterClass
    public static void afterClass() throws Exception {
        cluster.delete(new Path("pigunit-input-overriden.txt"));
    }

    private void print(String[] script) {
        for (String line : script) {
            System.out.println(line);
        }
    }

    private static String[] getScriptToTestTfIdfMacro() throws FileNotFoundException, IOException {
        // get the content of calculate_tf_idf macro from macros.pig file
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "tf_idf");
        macro.addFirst("in_relation = LOAD 'ommited' AS (docId, term);");
        macro.addLast("tfidf_values = ORDER tfidf_values BY docId, term;");
        return macro.toArray(new String[]{});
    }

    private static String[] getDefaultParams() {
        String[] defaultParams = {
            "in_relation=in_relation",
            "id_field=docId",
            "token_field=term",
            "tfidf_values=tfidf_values",
            "parallel=1"
        };
        return defaultParams;
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testTfIdf() throws IOException, ParseException {

       
        String[] input = {
            "d1\tt1",
            "d1\tt1",
            "d1\tt2",
            "d2\tt1"};

        // verify tfidf results
        String[] tfidfOutput = {
            "(d1,t1," + (2d / 3d) * Math.log(2d / 2d) + ")",
            "(d1,t2," + (1d / 3d) * Math.log(2d / 1d) + ")",
            "(d2,t1," + (1d / 1d) * Math.log(2d / 2d) + ")"};

        test.assertOutput("in_relation", input, "tfidf_values", tfidfOutput);
    }

    @org.testng.annotations.Test(groups = {"medium"})
    public void testTfIdfMedium() throws IOException, ParseException {

        test = new PigTest(script, params);
        String[] input = {
            "d1\tt1",
            "d1\tt1",
            "d2\tt1",
            "d2\tt2",
            "d3\tt2",
            "d3\tt3"
        };

        // verify tfidf results
        String[] tfidfOutput = {
            "(d1,t1," + (2d / 2d) * Math.log(3d / 2d) + ")",
            "(d2,t1," + (1d / 2d) * Math.log(3d / 2d) + ")",
            "(d2,t2," + (1d / 2d) * Math.log(3d / 2d) + ")",
            "(d3,t2," + (1d / 2d) * Math.log(3d / 2d) + ")",
            "(d3,t3," + (1d / 2d) * Math.log(3d / 1d) + ")"
        };

        test.assertOutput("in_relation", input, "tfidf_values", tfidfOutput);
    }

    @org.testng.annotations.Test(groups = {"medium"})
    public void testTfIdfMedium2() throws IOException, ParseException {

        test = new PigTest(script, params);
        String[] input = {
            "d1\tt1",
            "d1\tt1",
            "d1\tt1",
            "d2\tt1",
            "d2\tt2",
            "d3\tt2",
            "d3\tt2",
            "d3\tt3",
            "d3\tt4",
            "d4\tt5"
        };

        // verify tfidf results
        String[] tfidfOutput = {
            "(d1,t1," + (3d / 3d) * Math.log(4d / 2d) + ")",
            "(d2,t1," + (1d / 2d) * Math.log(4d / 2d) + ")",
            "(d2,t2," + (1d / 2d) * Math.log(4d / 2d) + ")",
            "(d3,t2," + (2d / 4d) * Math.log(4d / 2d) + ")",
            "(d3,t3," + (1d / 4d) * Math.log(4d / 1d) + ")",
            "(d3,t4," + (1d / 4d) * Math.log(4d / 1d) + ")",
            "(d4,t5," + (1d / 1d) * Math.log(4d / 1d) + ")",};

        test.assertOutput("in_relation", input, "tfidf_values", tfidfOutput);
    }
}

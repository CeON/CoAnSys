/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.script;

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



/**
 *
 * @author akawa
 */

public class TestPairwiseSimilarity {

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

        test = new PigTest(PIG_SCRIPT_DIR + "pairwise_similarity.pig", params);

        String[] input = {
            "d1\tt1\t1",
            "d2\tt1\t2"
        };

        // verify total similarity
        String[] docs_similarityOutput = {
            "(d1,d2,2.0)"
        };
        test.assertOutput("TFIDF", input, "docs_similarity", docs_similarityOutput);
    }

    @Test
    public void testMultiple() throws IOException, ParseException {

        test = new PigTest(PIG_SCRIPT_DIR + "pairwise_similarity.pig", params);

        String[] input = {
            "d1\tt1\t1",
            "d1\tt2\t2",
            "d2\tt1\t3",
            "d2\tt2\t4",
            "d3\tt1\t5"
        };

        // verify intermdiate data
        String[] term_docs_similarityOutput = {
            "(t1,d1,d2,1.0,3.0,3.0)",
            "(t1,d1,d3,1.0,5.0,5.0)",
            "(t1,d2,d3,3.0,5.0,15.0)",
            "(t2,d1,d2,2.0,4.0,8.0)"
        };

        test.assertOutput("TFIDF", input, "term_docs_similarity", term_docs_similarityOutput);

        // verify total similarity
        String[] docs_similarityOutput = {
            "(d1,d2,12.0)",
            "(d1,d3,5.0)",
            "(d2,d3,15.0)",};
        test.assertOutput("TFIDF", input, "docs_similarity", docs_similarityOutput);
    }
}
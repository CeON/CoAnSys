package pl.edu.icm.coansys.similarity.pig.script;

import java.io.IOException;
import java.util.LinkedList;
import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pl.edu.icm.coansys.similarity.test.utils.PigScriptExtractor;

/**
 *
 * @author akawa
 */
public class TestPairwiseSimilarity {

    private PigTest test;
    private static Cluster cluster;
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    private static final String[] params = {
        "t=null",
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

    @org.testng.annotations.Test(groups = {"fast"})
    public void testSingle() throws IOException, ParseException {

        LinkedList<String> script = PigScriptExtractor.extract(PIG_SCRIPT_DIR + "pairwise_similarity.pig");
        test = new PigTest(script.toArray(new String[]{}), params);

        String[] input = {
            "t1\td1\td2\t5\t4",
            "t2\td1\td2\t2\t8"
        };

        String[] docsSimilarity = {
            "(d1,d2,160.0)"
        };
        test.assertOutput("term_doc_tfidf", input, "docs_similarity", docsSimilarity);
    }

    @org.testng.annotations.Test(groups = {"medium"})
    public void testMultiple() throws IOException, ParseException {

        LinkedList<String> script = PigScriptExtractor.extract(PIG_SCRIPT_DIR + "pairwise_similarity.pig");
        test = new PigTest(script.toArray(new String[]{}), params);
        
        
        String[] input = {
            "t1\td1\td2\t5\t4",
            "t2\td1\td2\t2\t8"
        };

        String[] termDocSimilarity = {
            "(t1,d1,d2,5.0,4.0,20.0)",
            "(t2,d1,d2,2.0,8.0,16.0)"
        };
        test.assertOutput("term_doc_tfidf", input, "term_doc_similarity", termDocSimilarity);

        String[] docsSimilarity = {
            "(d1,d2,160.0)"
        };
        test.assertOutput("term_doc_tfidf", input, "docs_similarity", docsSimilarity);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.script;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.pig.tools.parameters.ParseException;
import pl.edu.icm.coansys.similarity.test.utils.MacroExtractor;

/**
 *
 * @author akawa
 */
public class TestStopwordsRemoval extends AbstractPigUnitTest {

    private String[] params = {
        "doc_word=doc_word",
        "doc_field=docId",
        "term_field=term",
        "stopwords=stopwords",
        "non_stopwords=non_stopwords"
    };

    @Override
    public String[] getScriptParams() {
        return params;
    }

    @Override
    public String[] getScriptToTest() throws FileNotFoundException, IOException {
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "remove_stopwords");
        macro.addFirst(""
                + "doc_word = LOAD 'ommited' AS (docId, term);\n"
                + "stopwords = LIMIT (FOREACH doc_word GENERATE term) 2;");
        macro.addLast("doc_word_ordered = ORDER non_stopwords BY docId, term;");
        return macro.toArray(new String[]{});
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStopwordsRemoval() throws IOException, ParseException {
        String[] input = {
            "d1\tt1",
            "d1\tt2",
            "d1\tt3",
            "d2\tt1"
        };
        String[] output = {"(d1,t3)"};
        test.assertOutput("doc_word", input, "doc_word_ordered", output);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStopwordsRemoval2() throws IOException, ParseException {
        String[] input = {
            "d1\tt1",
            "d1\tt2",
            "d1\tt3",
            "d1\tt4",
            "d1\tt1",
            "d2\tt2",
            "d3\tt3"
        };
        String[] output = {"(d1,t3)", "(d1,t4)", "(d3,t3)"};
        test.assertOutput("doc_word", input, "doc_word_ordered", output);
    }
}

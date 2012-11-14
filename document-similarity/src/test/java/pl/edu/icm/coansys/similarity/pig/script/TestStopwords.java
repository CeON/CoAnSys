/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.script;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.pig.tools.parameters.ParseException;
import pl.edu.icm.coansys.similarity.test.utils.MacroExtractor;

/**
 *
 * @author akawa
 */
public class TestStopwords extends AbstractPigUnitTest {

    private String[] params = {
        "doc_word=doc_word",
        "doc_field=docId",
        "term_field=term",
        "percentage=0.9",
        "stopwords=stopwords"
    };

    @Override
    public String[] getScriptParams() {
        return params;
    }

    @Override
    public String[] getScriptToTest() throws FileNotFoundException, IOException {
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "find_stopwords");
        macro.addFirst("doc_word = LOAD 'ommited' AS (docId, term);");
        macro.addLast("stopwords_ordered = ORDER stopwords BY term;");
        return macro.toArray(new String[]{});
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStopwords() throws IOException, ParseException {
        String[] input = {
            "d1\tt1",
            "d1\tt2",
            "d1\tt3",
            "d2\tt1"
        };
        String[] output = {"(t1)"};
        test.assertOutput("doc_word", input, "stopwords_ordered", output);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStopwords2() throws IOException, ParseException {
        String[] input = {
            "d1\tt1",
            "d1\tt2",
            "d1\tt3",
            "d2\tt1",
            "d2\tt2",
            "d3\tt1"
        };
        String[] output = {"(t1)"};
        test.assertOutput("doc_word", input, "stopwords_ordered", output);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testStopwords3() throws IOException, ParseException {
        int max = 100;
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < i; j++) {
                list.add(i + "\t" + j);
            }
        }
        String[] input = list.toArray(new String[]{});
        String[] output = {"(0)", "(1)", "(2)", "(3)", "(4)", "(5)", "(6)", "(7)", "(8)", "(9)"};
        test.assertOutput("doc_word", input, "stopwords_ordered", output);
    }
}

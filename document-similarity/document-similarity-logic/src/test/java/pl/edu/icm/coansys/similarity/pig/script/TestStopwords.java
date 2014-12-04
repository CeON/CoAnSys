/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
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
        "stopwords=stopwords",
        "CC=::"
    };

    @Override
    public String[] getScriptParams() {
        return params;
    }

    @Override
    public String[] getScriptToTest() throws FileNotFoundException, IOException {
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "find_stopwords");
        macro.addFirst("doc_word = LOAD 'ommited' AS (docId, term);");
        macro.addLast("stopworordered = ORDER stopwords BY term;");
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
        //test.assertOutput("doc_word", input, "stopworordered", output);
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
        //test.assertOutput("doc_word", input, "stopworordered", output);
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
        //test.assertOutput("doc_word", input, "stopworordered", output);
    }
}

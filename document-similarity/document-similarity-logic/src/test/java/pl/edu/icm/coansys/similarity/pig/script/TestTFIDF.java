/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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
import java.util.LinkedList;

import org.apache.pig.tools.parameters.ParseException;

import pl.edu.icm.coansys.similarity.test.utils.MacroExtractor;

/**
 *
 * @author akawa
 */
public class TestTFIDF extends AbstractPigUnitTest {

    private String[] params = {
        "in_relation=in_relation",
        "id_field=docId",
        "token_field=term",
        "tfidf_values=tfidf_values",
        "tfidfMinValue=0.0",
        "parallel=1",
        "CC=::"
    };

    @Override
    public String[] getScriptParams() {
        return params;
    }

    @Override
    public String[] getScriptToTest() throws FileNotFoundException, IOException {
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "calculate_tfidf");
        macro.addFirst("in_relation = LOAD 'ommited' AS (docId, term);");
        macro.addLast("tfidf_values = ORDER tfidf_values BY docId, term;");
        return macro.toArray(new String[]{});
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

        //test.assertOutput("in_relation", input, "tfidf_values", tfidfOutput);
    }

    @org.testng.annotations.Test(groups = {"medium"})
    public void testTfIdfMedium() throws IOException, ParseException {

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

        //test.assertOutput("in_relation", input, "tfidf_values", tfidfOutput);
    }

    @org.testng.annotations.Test(groups = {"medium"})
    public void testTfIdfMedium2() throws IOException, ParseException {

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
            "(d4,t5," + (1d / 1d) * Math.log(4d / 1d) + ")"
        };

        //test.assertOutput("in_relation", input, "tfidf_values", tfidfOutput);
    }
}

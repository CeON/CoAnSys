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
public class TestTopnSimilarDocuments extends AbstractPigUnitTest {

    private String[] params = {
        "in_relation=in_relation",
        "group_field=docId1",
        "order_field=tfidf",
        "order_direction=desc",
        "topn=2",
        "out_relation=out_relation",
            "CC=::"
    };

    @Override
    public String[] getScriptParams() {
        return params;
    }

    @Override
    public String[] getScriptToTest() throws FileNotFoundException, IOException {
        LinkedList<String> macro = MacroExtractor.extract(PIG_SCRIPT_DIR + "macros.pig", "get_topn_per_group");
        macro.addFirst(""
                + "in_relation = LOAD 'ommited' AS (docId1, docId2, tfidf);");
        macro.addLast("topn = ORDER out_relation BY docId1 asc, tfidf desc, docId2 asc;");
        return macro.toArray(new String[]{});
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void testTopN() throws IOException, ParseException {
        String[] input = {
            "d1\td2\t4",
            "d1\td3\t3",
            "d1\td4\t2"
        };
        String[] output = {"(d1,d2,4)", "(d1,d3,3)"};
        //test.assertOutput("in_relation", input, "topn", output);
    }
}

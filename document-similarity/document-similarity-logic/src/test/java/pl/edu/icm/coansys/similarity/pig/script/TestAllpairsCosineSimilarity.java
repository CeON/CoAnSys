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
public class TestAllpairsCosineSimilarity {

    private PigTest test;
    private static Cluster cluster;
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    private static final String[] params = {
        "tfidfPath=null",
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

        LinkedList<String> script = PigScriptExtractor.extract(PIG_SCRIPT_DIR + "document-similarity-s2,3-leftsim_basic,topn_basic.pig");
        test = new PigTest(script.toArray(new String[]{}), params);

        String[] input = {
            "d1\tt1\t1",
            "d1\tt2\t2",
            "d2\tt1\t3",
            "d2\tt2\t4"
        };

        // verify intermdiate data
        String[] similaritiesOutput = {
            "(d1,d2," + (3d + 8d) / (Math.sqrt(5d) * Math.sqrt(9d + 16d)) + ")"
        };
        test.assertOutput("t", input, "sim", similaritiesOutput);
    }
}
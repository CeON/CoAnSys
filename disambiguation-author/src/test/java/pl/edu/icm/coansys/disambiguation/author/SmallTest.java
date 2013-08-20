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

package pl.edu.icm.coansys.disambiguation.author;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;
import org.testng.annotations.Test;

public class SmallTest{
	
    private PigTest test;

    //private static Cluster cluster;
    public String[] script;
    
    private static final String PIG_SCRIPT_DIR = "src/main/pig/";
    public final static String TST_RESOURCES_DIR = "src/test/resources/";
    
	private final static String testScriptPath = PIG_SCRIPT_DIR+"small.pig";
		
	private final static String[] params = {
			"dc_m_hdfs_inputDocsData=null",
			"dc_m_hdfs_outputContribs=null",
	        "commonJarsPath=.",
	        "parallel=1"
	};

	/*
    @BeforeClass
    public static void beforeClass() throws Exception {
        cluster = PigTest.getCluster();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        cluster.delete(new Path("pigunit-input-overriden.txt"));
    }
	*/

	@Test(groups = {"fast"})
	public void smallTest() throws IOException, ParseException {
		LinkedList<String> script = PigScriptExtractor.extract(testScriptPath); 
		test = new PigTest(script.toArray(new String[]{}), params);
		String[] in = {"k1\tm1","k2\tm2","k3\tm3"};
		String[] out = {"(k1)","(k2)","(k3)"};
		test.assertOutput("A1",in,"A3",out);
	}
}

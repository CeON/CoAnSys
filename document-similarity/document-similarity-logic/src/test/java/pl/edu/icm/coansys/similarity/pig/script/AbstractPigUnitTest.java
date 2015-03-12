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
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.testng.annotations.BeforeTest;

/**
 *
 * @author akawa
 */
public abstract class AbstractPigUnitTest {

    public PigTest test;
    public Cluster cluster;
    
    public final String PIG_SCRIPT_DIR = "src/main/pig/";
    public String[] script;

    @BeforeTest
    public void init() throws FileNotFoundException, IOException {
        cluster = PigTest.getCluster();
        script = getScriptToTest();
        test = new PigTest(script, getScriptParams());
    }

    public abstract String[] getScriptToTest() throws FileNotFoundException, IOException;
     public abstract String[] getScriptParams();
}

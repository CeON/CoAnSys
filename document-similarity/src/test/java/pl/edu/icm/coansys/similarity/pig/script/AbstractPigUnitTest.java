/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.similarity.pig.script;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.pig.pigunit.Cluster;
import org.apache.pig.pigunit.PigTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author akawa
 */
public abstract class AbstractPigUnitTest {

    public PigTest test;
    public static Cluster cluster;
    
    public final String PIG_SCRIPT_DIR = "src/main/pig/";
    public String[] script;

    @BeforeClass
    public static void beforeClass() throws Exception {
        cluster = PigTest.getCluster();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        cluster.delete(new Path("pigunit-input-overriden.txt"));
    }

    @BeforeTest
    public void init() throws FileNotFoundException, IOException {
        script = getScriptToTest();
        test = new PigTest(script, getScriptParams());
    }

    public abstract String[] getScriptToTest() throws FileNotFoundException, IOException;
     public abstract String[] getScriptParams();
}

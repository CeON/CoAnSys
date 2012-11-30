/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

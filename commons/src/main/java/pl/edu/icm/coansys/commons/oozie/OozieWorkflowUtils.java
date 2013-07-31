/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.oozie;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author akawa
 */
public final class OozieWorkflowUtils {
    
    private OozieWorkflowUtils() {}
    
    public static void captureOutput(String... keyValuePairs) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            props.setProperty(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        
        captureOutput(props);
    }
    
    public static void captureOutput(Properties outputProperties) throws FileNotFoundException, IOException {
        File file = new File(System.getProperty("oozie.action.output.properties"));
        OutputStream os = new FileOutputStream(file);
        try {
            outputProperties.store(os, "");
        }
        finally {
            os.close();
        }
    }
}

/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

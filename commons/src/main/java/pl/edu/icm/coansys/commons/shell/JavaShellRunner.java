/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author akawa
 */

public class JavaShellRunner {

    private static Logger logger = LoggerFactory.getLogger(JavaShellRunner.class);

    private static String[] parseArgs(String[] args) {
        if (args == null || args.length < 2) {
            logger.debug("# of parameters should be greater or equal to 2");
            logger.debug("You need to provide:");
            logger.debug("* the script file");
            logger.debug("* the directory where the script is to run");
            logger.debug("");

            args = new String[2];
            args[0] = "/home/akawa/Documents/codes/scripts/create_testProto.sh";
            args[1] = "/home/akawa/Documents/codes/scripts";

            logger.debug("Default values will be used:");
            logger.debug("* " + args[0]);
            logger.debug("* " + args[1]);
        }

        return args;
    }

    private static void printOutputStream(Process proces) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(proces.getInputStream()));
        String resultLine;
        while ((resultLine = in.readLine()) != null) {
            System.out.println(resultLine);
        }

    }
    
    private static ProcessBuilder buildProcess(String[] args) {
        ProcessBuilder pb = new ProcessBuilder(args[0]);
        Map<String, String> env = pb.environment();
        for (int i = 3; i < args.length; i = i + 2) {
            env.put(args[i], args[i + 1]);
        }
        pb.directory(new File(args[1]));
        return pb;
        
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        args = parseArgs(args);

        ProcessBuilder pb = buildProcess(args);
        Process proc = pb.start();
        printOutputStream(proc);
        if (proc.waitFor() == 0) {
            System.out.println("Process terminates normally");
        }
        proc.destroy();
    }
}

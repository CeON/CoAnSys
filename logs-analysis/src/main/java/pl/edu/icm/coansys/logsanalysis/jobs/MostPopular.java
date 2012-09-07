/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.jobs;

import java.math.BigInteger;
import java.util.Random;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class MostPopular {

    public static void main(String[] args) throws Exception {
        String[] countArgs;
        String[] sortArgs = new String[3];

        //temporary directory path - remove it manually after work
        Random random = new Random();
        String tempPath = "/tmp/most-popular-" + System.currentTimeMillis() + "-" + new BigInteger(130, random).toString(32);

        if (args.length == 4) {
            countArgs = new String[3];
            countArgs[2] = args[3];
        } else if (args.length == 3) {
            countArgs = new String[2];
        } else {
            System.err.println("Usage: MostPopular <input_file_uri> <output_dir> <nb_of_most_popular> [<weight_class>]");
            System.exit(1);
            return;
        }
        countArgs[0] = args[0];
        countArgs[1] = tempPath;
        sortArgs[0] = tempPath;
        sortArgs[1] = args[1];
        sortArgs[2] = args[2];
        int countStatus = ToolRunner.run(new CountUsagesPart(), countArgs);
        if (countStatus != 0) {
            System.exit(countStatus);
        } else {
            System.exit(ToolRunner.run(new SortUsagesPart(), sortArgs));
        }
    }
}

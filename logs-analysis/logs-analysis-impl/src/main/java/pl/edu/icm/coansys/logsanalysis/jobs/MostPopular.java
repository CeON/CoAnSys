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

package pl.edu.icm.coansys.logsanalysis.jobs;

import java.math.BigInteger;
import java.util.Random;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class MostPopular {

    private static final Logger logger = LoggerFactory.getLogger(MostPopular.class);

    private MostPopular() {
    }

    public static void main(String[] args) {
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
        try {
            int countStatus = ToolRunner.run(new CountUsagesPart(), countArgs);
            if (countStatus != 0) {
                System.exit(countStatus);
            } else {
                System.exit(ToolRunner.run(new SortUsagesPart(), sortArgs));
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
            System.exit(1);
        }
    }
}

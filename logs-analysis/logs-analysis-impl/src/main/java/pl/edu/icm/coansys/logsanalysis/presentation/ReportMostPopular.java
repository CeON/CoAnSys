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

package pl.edu.icm.coansys.logsanalysis.presentation;

import java.io.IOException;
import java.util.Date;
import pl.edu.icm.coansys.commons.hadoop.BytesArray2SequenceFile;
import pl.edu.icm.coansys.models.MostPopularProtos;
import pl.edu.icm.coansys.models.MostPopularProtos.ResourceStat;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class ReportMostPopular {

    private ReportMostPopular() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: ReportMostPopular <file_uri>");
            return;
        }
        for (byte[] bytes : BytesArray2SequenceFile.read(args[0])) {
            MostPopularProtos.MostPopularStats stats = MostPopularProtos.MostPopularStats.parseFrom(bytes);
            System.out.println("Most popular statistics from date " + new Date(stats.getTimestamp()));
            for (ResourceStat rs : stats.getStatList()) {
                System.out.println("Resource ID: " + rs.getResourceId() + "; usages: " + rs.getCounter());
            }
        }
    }
}

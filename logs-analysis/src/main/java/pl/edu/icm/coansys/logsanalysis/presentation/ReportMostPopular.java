/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.presentation;

import java.io.IOException;
import java.util.Date;
import pl.edu.icm.coansys.importers.models.MostPopularProtos;
import pl.edu.icm.coansys.importers.models.MostPopularProtos.ResourceStat;
import pl.edu.icm.coansys.logsanalysis.transformers.BytesArray2SequenceFile;

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

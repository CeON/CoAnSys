/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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
package pl.edu.icm.coansys.statisticsgenerator;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import pl.edu.icm.coansys.models.StatisticsProtos;
import pl.edu.icm.oozierunner.OozieRunner;

public class TestIT {

    @Test
    public void testTest1() throws Exception {
        OozieRunner or = new OozieRunner();
        File workflowOutputData = or.run();

        assertTrue(workflowOutputData.exists());
        assertTrue(workflowOutputData.isDirectory());
        assertTrue(workflowOutputData.listFiles().length > 0);

        int foundRecords = 0;
        int statistics = 0;
        int selectedStats = 0;
        double counter = 0;

        for (File f : FileUtils.listFiles(workflowOutputData, null, true)) {
            if (f.isFile() && f.getName().startsWith("part-")) {
                Configuration conf = new Configuration();
                Path path = new Path("file://" + f.getAbsolutePath());
                Reader reader = new Reader(conf, Reader.file(path));
                Text key = new Text();
                BytesWritable value = new BytesWritable();
                while (reader.next(key, value)) {
                    statistics++;
                    StatisticsProtos.SelectedStatistics protoValue = StatisticsProtos.SelectedStatistics.parseFrom(value.copyBytes());
                    for (StatisticsProtos.Statistics stat : protoValue.getStatsList()) {
                        selectedStats++;
                        for (StatisticsProtos.KeyValue summary : stat.getStatisticsList()) {
                            foundRecords++;
                            counter += Double.parseDouble(summary.getValue());
                        }
                    }
                }
            }
        }

        assertEquals(foundRecords, 20);
        assertEquals(counter, 20.0);
        assertEquals(statistics, 20);
        assertEquals(selectedStats, 20);
    }
}

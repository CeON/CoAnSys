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

package pl.edu.icm.coansys.logsanalysis.jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.testng.annotations.BeforeClass;
import pl.edu.icm.coansys.logsanalysis.constants.ParamNames;
import pl.edu.icm.coansys.logsanalysis.models.LogsMessageHelper;
import pl.edu.icm.coansys.models.LogsProtos;
import pl.edu.icm.coansys.models.LogsProtos.LogsMessage;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class CountUsagesTest {
    
    private final static int TEST_ENTRIES_COUNT = 4;
    private final static String TEST_SESSION_ID = "test_session";
    private final static String TEST_USER = "test_user";
    private final static String TEST_RESOURCE = "resource01";
    private final static String TEST_EVENT_PREFIX = "event_"; 
   
    MapReduceDriver<Writable, BytesWritable, Text, LongWritable, Text, LongWritable> mapReduceDriver;
    
    @BeforeClass
    public void beforeClass() {
        Mapper m = new CountUsagesPart.CounterMap();
        Reducer r = new CountUsagesPart.CounterReduce();
        mapReduceDriver = new MapReduceDriver(m, r);
        Configuration conf = mapReduceDriver.getConfiguration();
        conf.set("USAGE_WEIGHT_CLASS", "pl.edu.icm.coansys.logsanalysis.metrics.SimpleUsageWeight");
    }
    
    @org.testng.annotations.Test(groups = {"fast"})
    public void countUsagesTest() {
        for (int i = 0; i < TEST_ENTRIES_COUNT; i++) {
            Map<String, String> args = new HashMap<String, String>();
            args.put(ParamNames.RESOURCE_ID_PARAM, TEST_RESOURCE);
            args.put(ParamNames.SESSION_ID_PARAM, TEST_SESSION_ID);
            args.put(ParamNames.USER_ID_PARAM, TEST_USER);

            LogsMessage logsMessage = LogsMessageHelper.createLogsMessage(TEST_EVENT_PREFIX + i, LogsProtos.LogsLevel.INFO,
                    new Date(System.currentTimeMillis()), LogsProtos.EventType.FETCH_CONTENT, args);
            
            byte[] bytes = logsMessage.toByteArray();
            mapReduceDriver.addInput(NullWritable.get(), new BytesWritable(bytes));
        }
        mapReduceDriver.addOutput(new Text(TEST_RESOURCE), new LongWritable(TEST_ENTRIES_COUNT));
        mapReduceDriver.runTest();
    }
}

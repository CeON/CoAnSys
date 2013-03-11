/*
 * (C) 2010-2012 ICM UW. All rights reserved.
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
import pl.edu.icm.coansys.logsanalysis.constants.ServicesEventsWeights;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryHelper;
import pl.edu.icm.coansys.logsanalysis.transformers.AuditEntry2Protos;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

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
            
            AuditEntry ae = AuditEntryHelper.getAuditEntry(TEST_EVENT_PREFIX+i, AuditEntry.Level.DEBUG, new Date(System.currentTimeMillis()),
                    ServicesEventsWeights.FETCH_CONTENT.getServiceId(), ServicesEventsWeights.FETCH_CONTENT.getEventType(), args);
            
            byte[] bytes = AuditEntry2Protos.serialize(ae).toByteArray();
            mapReduceDriver.addInput(NullWritable.get(), new BytesWritable(bytes));
        }
        mapReduceDriver.addOutput(new Text(TEST_RESOURCE), new LongWritable(TEST_ENTRIES_COUNT));
        mapReduceDriver.runTest();
    }
}

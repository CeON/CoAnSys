/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.jobs;

import java.util.Date;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryFactory;
import pl.edu.icm.coansys.logsanalysis.transformers.AuditEntry2Protos;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class CountUsagesTest {
    
    private final static int TEST_ENTRIES_COUNT = 4;
    
    MapReduceDriver<Writable, BytesWritable, Text, LongWritable, Text, LongWritable> mapReduceDriver;
    
    @Before
    public void setUp() {
        Mapper m = new CountUsagesPart.CounterMap();
        Reducer r = new CountUsagesPart.CounterReduce();
        mapReduceDriver = new MapReduceDriver(m, r);
        Configuration conf = mapReduceDriver.getConfiguration();
        conf.set("USAGE_WEIGHT_CLASS", "pl.edu.icm.coansys.logsanalysis.metrics.SimpleUsageWeight");
    }
    
    @Test
    public void countUsagesTest() {        
        for (int i = 0; i < TEST_ENTRIES_COUNT; i++) {
            AuditEntry ae = AuditEntryFactory.getAuditEntry("event"+i, AuditEntry.Level.DEBUG, new Date(System.currentTimeMillis()), "testService", "SAVE_TO_DISK",
                    "127.0.0.1", "http://localhost/", "http://localhost/", "test_session", "test_user", "resource01");
            byte[] bytes = AuditEntry2Protos.serialize(ae).toByteArray();
            mapReduceDriver.addInput(NullWritable.get(), new BytesWritable(bytes));
        }
        mapReduceDriver.addOutput(new Text("resource01"), new LongWritable(TEST_ENTRIES_COUNT));
        mapReduceDriver.runTest();
    }
}

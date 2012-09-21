/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.jobs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;
import pl.edu.icm.coansys.logsanalysis.models.MostPopularProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class SortUsagesTest {

    private final static String RES1_ID = "resource01";
    private final static String RES2_ID = "resource02";
    private final static String RES3_ID = "resource03";
    private final static int COUNT1 = 5;
    private final static int COUNT2 = 3;
    private final static int COUNT3 = 7;
    private final static String RESULT_RECORDS = "2";
    private final static String TEST_DATE = "2012-09-21";
    MapReduceDriver<Text, LongWritable, NullWritable, Text, NullWritable, BytesWritable> mapReduceDriver;

    @Before
    public void setUp() {
        Mapper m = new SortUsagesPart.SorterMap();
        Reducer r = new SortUsagesPart.SorterReduce();
        mapReduceDriver = new MapReduceDriver(m, r);
        Configuration conf = mapReduceDriver.getConfiguration();
        conf.set("NB_OF_RECORDS", RESULT_RECORDS);
        conf.set("RESULT_DATE", TEST_DATE);
    }

    @Test
    public void countUsagesTest() throws ParseException {
        mapReduceDriver.addInput(new Text(RES1_ID), new LongWritable(COUNT1));
        mapReduceDriver.addInput(new Text(RES2_ID), new LongWritable(COUNT2));
        mapReduceDriver.addInput(new Text(RES3_ID), new LongWritable(COUNT3));

        MostPopularProtos.MostPopularStats.Builder statsBuilder = MostPopularProtos.MostPopularStats.newBuilder();
        statsBuilder.setTimestamp(new SimpleDateFormat("yyyy-MM-dd").parse(TEST_DATE).getTime());

        MostPopularProtos.ResourceStat.Builder resourceStatBuilder = MostPopularProtos.ResourceStat.newBuilder();
        resourceStatBuilder.setCounter(COUNT3);
        resourceStatBuilder.setResourceId(RES3_ID);
        statsBuilder.addStat(resourceStatBuilder);

        resourceStatBuilder = MostPopularProtos.ResourceStat.newBuilder();
        resourceStatBuilder.setCounter(COUNT1);
        resourceStatBuilder.setResourceId(RES1_ID);
        statsBuilder.addStat(resourceStatBuilder);

        BytesWritable expectedOutput = new BytesWritable(statsBuilder.build().toByteArray());

        System.out.println("Expected output: " + statsBuilder.build().toString());
        mapReduceDriver.addOutput(NullWritable.get(), expectedOutput);

        mapReduceDriver.runTest();
    }
}

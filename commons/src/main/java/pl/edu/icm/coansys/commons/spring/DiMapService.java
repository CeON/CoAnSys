/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

/**
 * A contract for classes performing map business logic in {@link DiMapper}
 * @author lukdumi
 *
 */
public interface DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

    void map(KEYIN key, VALUEIN value, Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>.Context context) throws IOException, InterruptedException;
}

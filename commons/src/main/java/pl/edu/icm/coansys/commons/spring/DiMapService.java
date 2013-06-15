package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

public interface DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

    void map(KEYIN key, VALUEIN value, Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>.Context context) throws IOException, InterruptedException;
}

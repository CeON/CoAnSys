package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

public interface DiReduceService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    
    
    void reduce(KEYIN key, Iterable<VALUEIN> values, Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT>.Context context) throws IOException, InterruptedException;
    
}

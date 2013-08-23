/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

/**
 *
 * @author akawa
 */
public class BytesToCharArray extends EvalFunc<String> {
   
    @Override
    public String exec(Tuple input) throws IOException {
        byte[] bytes = (byte[]) input.get(0);
        String s = Bytes.toString(bytes);
        return s;
    }
}
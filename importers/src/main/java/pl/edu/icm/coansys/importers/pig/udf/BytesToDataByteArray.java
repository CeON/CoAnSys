/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;

/**
 *
 * @author akawa
 */
public class BytesToDataByteArray extends EvalFunc<DataByteArray> {
    private DataByteArray byteArray = new DataByteArray();
    
    @Override
    public DataByteArray exec(Tuple input) throws IOException {
        byte[] bytes = (byte[]) input.get(0);
        byteArray.set(bytes);
        return byteArray;
    }
}
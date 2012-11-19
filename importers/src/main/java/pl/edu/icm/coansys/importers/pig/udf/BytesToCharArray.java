/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.pig.udf;

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
/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 *
 * @author pdendek
 */
public class EXTRACT_BAG_FROM_MAP extends EvalFunc<DataBag> {

//	@Override
//	public Schema outputSchema(Schema p_input){
//		try{
//			return Schema.generateNestedSchema(DataType.BAG, 
//					DataType.CHARARRAY);
//		}catch(FrontendException e){
//			throw new IllegalStateException(e);
//		}
//	}
    @Override
    public DataBag exec(Tuple input) throws IOException {
        try {
            Map<String, Object> map;
            String key;
            Object raw;
            try {
                map = (Map<String, Object>) input.get(0);
                key = (String) input.get(1);
                raw = map.get(key);
            } catch (Exception e) {
                System.out.println("No map or key/The key does not occure in the given map");
                return null;
            }

            if (raw != null) {
                DataBag ret = new DefaultDataBag();
//    		   System.out.println("-------------1------------");
                String vals = raw.toString();
                if (vals.length() <= 2) {
                    return null;
                }
                String[] valsA = vals.substring(1, vals.length() - 1).split(",");
                for (final String v : valsA) {
                    if (v.length() <= 2) {
                        continue;
                    }
                    ret.add(TupleFactory.getInstance().newTuple(new ArrayList<String>() {

                        {
                            add(v.substring(1, v.length() - 1));
                        }
                    }));
                }
                return ret;
            }
            return null;
        } catch (Exception e) {
            // Throwing an exception will cause the task to fail.
            throw new RuntimeException("Error while parsing DocumentMetadata" + e);
        }
    }
}

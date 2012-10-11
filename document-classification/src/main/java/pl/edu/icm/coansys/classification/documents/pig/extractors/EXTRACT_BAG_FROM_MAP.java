package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.PUT;

import org.apache.commons.collections.Bag;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

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
	static String vv;
	
   @Override
   public DataBag exec(Tuple input) throws IOException {
       try {
    	   Map<String, Object> map;
    	   String key;
    	   Object raw;
    	   try{
    		   map = (Map<String, Object>) input.get(0);
        	   key = (String) input.get(1);
    		   raw = map.get(key);
    	   }catch(Exception e){
    		   System.out.println("No map or key/The key does not occure in the given map");
    		   return null;
    	   }

    	   if(raw!=null){
    		   DataBag ret = new DefaultDataBag();
//    		   System.out.println("-------------1------------");
    		   String vals = raw.toString();
		   if(vals.length()<=2){
			return null;
		   }
    		   String[] valsA = vals.substring(1,vals.length()-1).split(",");
    		   for(String v : valsA){
    			   vv=v;
			   if(vv.length()<=2){
				continue;
			   }
    			   ret.add(TupleFactory.getInstance().newTuple(new ArrayList<String>(){{
    				   add(vv.substring(1,vv.length()-1));
    			   }}));
    		   }
    		   return ret;
    	   }
    	   return null;
       } catch (Exception e) {
           // Throwing an exception will cause the task to fail.
           throw new RuntimeException("Error while parsing DocumentMetadata"+e);
       }
   }
}

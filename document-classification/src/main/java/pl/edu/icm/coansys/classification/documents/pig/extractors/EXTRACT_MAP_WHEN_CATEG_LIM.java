/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

/**
*
* @author pdendek
*/
@SuppressWarnings("rawtypes")
public class EXTRACT_MAP_WHEN_CATEG_LIM extends EvalFunc<Map> {

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.MAP);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}	
	
   @Override
   public Map exec(Tuple input) throws IOException {
       try {
           DataByteArray protoMetadata = (DataByteArray) input.get(0);
           DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMetadata.get());
           Integer lim = Integer.parseInt((String) input.get(1));
           DataBag db = getCategories(metadata.getClassifCodeList());
           if(db.size() >= lim){
        	   Map<String, Object> map = new HashMap<String, Object>();
               map.put("key", metadata.getKey());
               map.put("title", metadata.getTitle());
               map.put("keywords", getConcatenated(metadata.getKeywordList()));
               map.put("abstract", metadata.getAbstrakt());
               map.put("categories", db);
               return map;
           }
           return null;
           
       } catch (Exception e) {
    	// Throwing an exception will cause the task to fail.
           throw new IOException("Caught exception processing input row:\n"
           		+ StackTraceExtractor.getStackTrace(e));
       }
   }

   private DataBag getCategories(List<ClassifCode> classifCodeList) {
	   DataBag db = new DefaultDataBag();
	   for(ClassifCode code : classifCodeList)
       	for(String co_str : code.getValueList()){
//       		System.out.print(" "+co_str);
       		db.add(TupleFactory.getInstance().newTuple(co_str));
       	}
	return db;
}

private String getConcatenated(List<String> list) {
       if (list == null || list.isEmpty()) {
           return null;
       }
       StringBuilder sb = new StringBuilder(list.size());
       sb.append(list.get(0));
       for (int i = 1; i < list.size(); i++) {
           sb.append(" ").append(list.get(i));
       }
       return sb.toString();
   }
}

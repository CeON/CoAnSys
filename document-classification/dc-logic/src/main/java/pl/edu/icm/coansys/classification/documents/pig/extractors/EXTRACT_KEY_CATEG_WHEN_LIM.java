/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.extractors;

import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
*
* @author pdendek
*/
public class EXTRACT_KEY_CATEG_WHEN_LIM extends EvalFunc<Tuple>{
    
        private static final Logger logger = LoggerFactory.getLogger(EXTRACT_KEY_CATEG_WHEN_LIM.class);

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.CHARARRAY, DataType.BAG, DataType.INTEGER );
		}catch(FrontendException e){
                    logger.error("Error in creating output schema:", e);
		    throw new IllegalStateException(e);
		}
	}
	
        @Override
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			Object obj = null;
			Integer limnum = null;
			try{
				obj = (DataByteArray) input.get(1);
				
			}catch(ExecException e){
                            logger.error("Error in reading field proto:", e);
                            throw e;
			}
			
			try{
				limnum = (Integer) input.get(2);
			}catch(ExecException e){
                            logger.error("Error in reading baglimit:", e);	
                            throw e;
			}
			
			DataByteArray dba = null;
			try{
				dba = (DataByteArray) obj;	
			}catch(ClassCastException e){
                            logger.error("Error in casting Object ("+input.getType(1)+") to DataByteArray:", e);	
                            throw e;
			}
			
			DocumentMetadata dm = null;
			try{
				dm = DocumentMetadata.parseFrom(dba.get());
			}catch(InvalidProtocolBufferException e){
                            logger.error("Error in reading ByteArray to DocumentMetadata:", e);	
                            throw e;
			}
			 
	        String key = dm.getKey();
	        DataBag db = new DefaultDataBag();
	        int bagsize = 0;
	        for(ClassifCode code : dm.getBasicMetadata().getClassifCodeList()) {
	        	for(String co_str : code.getValueList()){
	        		bagsize++;
	        		db.add(TupleFactory.getInstance().newTuple(co_str));
	        	}
                }
	        if(bagsize>limnum){
	        	Object[] to = new Object[]{key,db,bagsize};
		        return TupleFactory.getInstance().newTuple(Arrays.asList(to));
	        }
	        return null;
		}catch(Exception e){
                    logger.error("Error in processing input row:", e);	
                    throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
		}
	}
}

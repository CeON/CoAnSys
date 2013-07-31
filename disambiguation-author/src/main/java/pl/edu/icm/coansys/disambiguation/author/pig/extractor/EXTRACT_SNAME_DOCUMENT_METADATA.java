/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.disambiguation.author.pig.extractor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
*
* @author pdendek
*/
public class EXTRACT_SNAME_DOCUMENT_METADATA extends EvalFunc<DataBag>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.BAG);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public DataBag exec(Tuple input) throws IOException {
		
	
		if (input == null || input.size() == 0)
			return null;
		
		try{		
			DataByteArray dba = null;
			try{
				dba = (DataByteArray) input.get(0);	
			}catch(Exception e){
				System.out.println("Trying to cast Object ("+input.getType(0)
						+") to DataByteArray");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DocumentWrapper dm = null;
			try{
				dm = DocumentWrapper.parseFrom( dba.get() );
			}catch(Exception e){
				System.out.println("Trying to read ByteArray to DocumentMetadata");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DataBag ret = new DefaultDataBag();
			DataByteArray metadata = 
					new DataByteArray(dm.getDocumentMetadata().toByteArray());
			
			List <Author> authors =  
					dm.getDocumentMetadata().getBasicMetadata().getAuthorList();
			
			for ( int i = 0; i < authors.size(); i++ ){
				String sname = authors.get(i).getSurname();
				Object[] to = new Object[]{sname, metadata, i};
				Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
				ret.add(t);
			}
	        
	        return ret;
			
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
		}
	}
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

public class EXTRACT_KEY_CATEG_WHEN_LIM extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.CHARARRAY, DataType.BAG, DataType.INTEGER );
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			Object obj = null;
			Integer limnum = null;
			try{
				obj = (DataByteArray) input.get(1);
				
			}catch(Exception e){
				System.out.println("Trying to read field proto");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			try{
				limnum = (Integer) input.get(2);
			}catch(Exception e){
				System.out.println("Trying to read baglimit");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DataByteArray dba = null;
			try{
				dba = (DataByteArray) obj;	
			}catch(Exception e){
				System.out.println("Trying to cast Object ("+input.getType(1)+") to DataByteArray");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			
			DocumentMetadata dm = null;
			try{
				dm = DocumentMetadata.parseFrom(dba.get());
			}catch(Exception e){
				System.out.println("Trying to read ByteArray to DocumentMetadata");
				System.out.println("Failure!");
				e.printStackTrace();
				throw e;
			}
			 
	        String key = dm.getKey();
	        DataBag db = new DefaultDataBag();
	        int bagsize = 0;
//	        System.out.print(key+":");
	        for(ClassifCode code : dm.getClassifCodeList())
	        	for(String co_str : code.getValueList()){
	        		bagsize++;
//	        		System.out.print(" "+co_str);
	        		db.add(TupleFactory.getInstance().newTuple(co_str));
	        	}
//	        System.out.println("");
	        if(bagsize>limnum){
	        	Object[] to = new Object[]{key,db,bagsize};
		        Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
		        return t;
	        }
	        return null;
		}catch(Exception e){
			throw new IOException("Caught exception processing input row ", e);
		}
	}
}
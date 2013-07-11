/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.disambiguation.author.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

import java.util.Date;

/**
 * @author pdendek
 * @author mwos
 */

public class UdfTools  {
	
	class GenUUID extends EvalFunc< String > {

	    /**
	     * @param Tuple input with one String - contributor name, 
	     * for whom we want find unique id
	     * @returns String UUID
	     */		
		//TODO: na wejsciu DateBag nie pojedyncza Tupla, na wykscoi DateBag z String
		@Override
		public String exec( Tuple input ) throws IOException {	
			return "" + input.get(0) + (new Date()).getTime();
		}
	}
	
	class GetContributors extends EvalFunc<String> {

		/**
		 * @param Tuple input with DocumentMetadata metadata of document 
		 * and int index of contributor in document authors list  
		 * @return int author's key
		 */
		//TODO: wejscie i wyjscie jak TODO w GenUUID
		@Override
		public String exec( Tuple input ) throws IOException {
					
			if (input == null || input.size() == 0)
				return null;
			
			try{			
				//getting metadata
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
				
				DocumentMetadata metadane;
				metadane = DocumentMetadata.parseFrom( dba.get() );
				
				//getting contributor index in list of this document's authors
				int contributorPos;
				
				try{
					contributorPos = (Integer) input.get(1);
				}catch(Exception e){
					System.out.println("Trying to read field rowId");
					System.out.println("Failure!");
					e.printStackTrace();
					throw e;
				}
				
				//DataBag ret = new DefaultDataBag();
			
				return metadane.getBasicMetadata().getAuthorList().
						get( contributorPos ).getKey();

			}catch(Exception e){
				// Throwing an exception will cause the task to fail.
				throw new IOException("Caught exception processing input row:\n"
						+ StackTraceExtractor.getStackTrace(e));
			}		
		}
	}
}


/*

DataBag ret = new DefaultDataBag();
DataByteArray metadata = new DataByteArray(dm.getDocumentMetadata().toByteArray());

for(Author a : dm.getDocumentMetadata().getBasicMetadata().getAuthorList()){
	String sname = a.getSurname();
	Object[] to = new Object[]{key,sname, metadata};
	Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
	ret.add(t);
}

return ret;
*/
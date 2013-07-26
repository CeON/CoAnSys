package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class GetContributors extends EvalFunc<String> {

	/**
	 * @param Tuple input with DocumentMetadata metadata of document 
	 * and int index of contributor in document authors list  
	 * @return String author's key
	 */

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
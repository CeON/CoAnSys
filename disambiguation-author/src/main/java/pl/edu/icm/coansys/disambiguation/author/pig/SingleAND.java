package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DefaultTuple;
import org.apache.pig.data.Tuple;

import pl.edu.icm.coansys.disambiguation.author.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class SingleAND extends EvalFunc< Tuple > {

	/**
	 * Extract contributor key from document's metadata using index on the
	 * author list. 
	 * @param Tuple input with DataByteArray metadata of document 
	 * and int index of contributor in document authors list  
	 * @return Tuple with String UUID, String author's key
	 */

	@Override
	public Tuple exec( Tuple input ) throws IOException {
				
		if (input == null || input.size() == 0)
			return null;
		
		try{			
			//getting metadata
			DataByteArray dba = (DataByteArray) input.get(0);
			DocumentMetadata metadata = DocumentMetadata.parseFrom( dba.get() );
			
			//getting contributor index in list of this document's authors
			int contributorPos = (Integer) input.get(1);

			Tuple ret = new DefaultTuple();
			
			Author contr = metadata.getBasicMetadata().getAuthorList().
					get( contributorPos );
			
			ret.append( contr.getKey() );
			ret.append( contr.getKey() );
			
			return ret; 
			
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}
}
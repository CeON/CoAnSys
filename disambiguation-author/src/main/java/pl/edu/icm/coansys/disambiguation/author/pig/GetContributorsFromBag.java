/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.author.pig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

public class GetContributorsFromBag extends EvalFunc<DataBag> {

	/**
	 * @param Tuple input with two DataBags - 1st: Tuples with metadata (DataByteArray), 
	 * 2nd: Tuples with index (int) of contributors in document author list
	 * @return DateBag contributors key from indexes in document author list
	 */
	
	@Override
	public DataBag exec( Tuple input ) throws IOException {
				
		if (input == null || input.size() == 0)
			return null;
		
		try{			
			DataBag ret = new DefaultDataBag();
			
			DataBag dbMetadata = (DefaultDataBag) input.get(0);	
			DataBag dbContribPos = (DefaultDataBag) input.get(1);			
			
			if ( dbMetadata.size() != dbContribPos.size() )
				throw new IOException("Diffrent size of data bags with " +
						"contributor positions and metadata \n");
			
			Iterator<Tuple> itDbMetadata = dbMetadata.iterator();
			Iterator<Tuple> itDbContribPos = dbContribPos.iterator();
			
			while ( itDbContribPos.hasNext() )
			{
				DataByteArray dbaMetadata = (DataByteArray) itDbMetadata.next().get(0);
				DocumentMetadata metadane = DocumentMetadata.parseFrom( dbaMetadata.get() );
				
				int contributorPos = (Integer) itDbContribPos.next().get(0);
				
				String contribKey = metadane.getBasicMetadata().getAuthorList().
						get( contributorPos ).getKey();
				
				Object[] to = new Object[]{contribKey};
				Tuple t = TupleFactory.getInstance().newTuple( Arrays.asList(to) );
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

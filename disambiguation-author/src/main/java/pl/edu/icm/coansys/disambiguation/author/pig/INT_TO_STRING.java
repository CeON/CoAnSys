/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class INT_TO_STRING extends EvalFunc<Tuple> {
	
	private static final Logger logger = LoggerFactory.getLogger(INT_TO_STRING.class);
	
    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }
	
	@Override
	public Tuple exec( Tuple input ) throws IOException {

		int size = input.size();
		int toStringIndex = Integer.parseInt((String)input.get(size-1));
		Tuple t = TupleFactory.getInstance().newTuple();
		
		for(int i = 0; i<size;i++){
			if(i==toStringIndex){
				t.append(((Integer)input.get(i)).toString());
			}else{
				t.append(input.get(i));
			}
		}
		
    	return t;
	}
}

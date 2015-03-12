/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

/**
 *
 * @author pdendek
 */
public class ByteArrayToText extends EvalFunc<Tuple> {
    
	@Override
	public Schema outputSchema(Schema p_input) {
		try {
			return Schema.generateNestedSchema(DataType.TUPLE,
					DataType.CHARARRAY);
		} catch (FrontendException e) {
			throw new IllegalStateException(e);
		}
	}
	
    @Override
    public Tuple exec(Tuple input) throws IOException {
    	Tuple t = TupleFactory.getInstance().newTuple();
    	t.append(((DataByteArray) input.get(0)).toString());
        return t;
    }
    
    public static void main(String[] args){
    	String s = "bla";
    	DataByteArray dba = new DataByteArray();
    	dba.set(s.getBytes());
    	System.out.println(dba.toString());
    }
}

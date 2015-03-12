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

package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

public class SortLexicographicly extends EvalFunc<Tuple> {

    public Tuple exec(Tuple input) throws IOException {
        try {
        	TupleFactory tf = TupleFactory.getInstance(); 
        	String docA = (String) input.get(0);
        	String docB = (String) input.get(1);
        	Tuple out = tf.newTuple();
        	if(docA.compareTo(docB)<0){
        		out.append(docA);
        		out.append(docB);
        	}else if(docA.compareTo(docB)>0){
        		out.append(docB);
        		out.append(docA);
        	}else{
        		throw new Exception("DocIdA == DocIdB");
        	}
        	out.append(input.get(2));
            return out;
        } catch (Exception e) {
            System.out.println(StackTraceExtractor.getStackTrace(e));
            return null;
        }
    }

	private Float getNumber(Object o) {
		if(o instanceof Float){
			return (Float) o;
		}else if(o instanceof Double){
			return new Float((Double)o);
		}
		return null;
	}
}

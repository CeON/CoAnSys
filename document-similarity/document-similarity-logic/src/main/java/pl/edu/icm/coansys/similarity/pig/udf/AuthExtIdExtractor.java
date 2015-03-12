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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;

public class AuthExtIdExtractor extends EvalFunc<Tuple> {

	private static final Logger logger = LoggerFactory.getLogger(AuthExtIdExtractor.class);
	
	@Override
	public Schema outputSchema(Schema p_input) {
		try {
			return Schema.generateNestedSchema(DataType.TUPLE,
					DataType.CHARARRAY, DataType.BAG);
		} catch (FrontendException e) {
			throw new IllegalStateException(e);
		}
	}

	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() != 2) {
			return null;
		}

		try{
			TupleFactory tf = TupleFactory.getInstance();
			
			String key = (String) input.get(0);
	        DocumentWrapper dw = DocumentWrapper.parseFrom(((DataByteArray) input.get(1)).get());
	        List<Author> aths = dw.getDocumentMetadata().getBasicMetadata().getAuthorList();

	        DataBag db = new DefaultDataBag(); 
	        for(Author a : aths){
	        	for(KeyValue kv : a.getExtIdList()){
	        		Tuple t = tf.newTuple();
	        		t.append(a.getKey());
	        		t.append(kv.getKey());
	        		t.append(kv.getValue());
	        		db.add(t);
	        	}
	        }
	        
	        Tuple ret = tf.newTuple();
	        ret.append(key);
	        ret.append(db);
	        return ret;
		}catch(Exception e){
			logger.error("Error in processing input row:"+ StackTraceExtractor.getStackTrace(e), e);
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}

	}
}

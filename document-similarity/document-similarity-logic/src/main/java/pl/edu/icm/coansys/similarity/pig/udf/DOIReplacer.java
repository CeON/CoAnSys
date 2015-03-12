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

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

public class DOIReplacer extends EvalFunc<Tuple> {

	private static final Logger logger = LoggerFactory.getLogger(DOIReplacer.class);
	
	@Override
	public Schema outputSchema(Schema p_input) {
		try {
			return Schema.generateNestedSchema(DataType.TUPLE,
					DataType.CHARARRAY, DataType.BYTEARRAY);
		} catch (FrontendException e) {
			throw new IllegalStateException(e);
		}
	}

	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() != 3) {
			return null;
		}

		try{
			String key = (String) input.get(0);
	        DocumentWrapper dw = DocumentWrapper.parseFrom(((DataByteArray) input.get(1)).get());
	        String correctedDoi = (String) input.get(2);
			        
	        DocumentWrapper.Builder dwb = DocumentWrapper.newBuilder(dw); 
	        DocumentMetadata.Builder dmb = DocumentMetadata.newBuilder(dw.getDocumentMetadata());
	        BasicMetadata.Builder bmb = BasicMetadata.newBuilder(dmb.getBasicMetadata());
	        bmb.setDoi(correctedDoi);
	        
	        dmb.setBasicMetadata(bmb);
	        dwb.setDocumentMetadata(dmb);
	        Tuple ret = TupleFactory.getInstance().newTuple();
	        ret.append(key);
	        ret.append(new DataByteArray(dwb.build().toByteArray()));

	        return ret;
		}catch(Exception e){
			logger.error("Error in processing input row:"+ StackTraceExtractor.getStackTrace(e), e);
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}

	}
}

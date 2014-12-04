/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.icm.coansys.similarity.pig.serializers;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentSimilarityProtos.DocumentSimilarityInfo;
import pl.edu.icm.coansys.models.DocumentSimilarityProtos.SecondDocInfo;

/**
 * 
 * @author pdendek
 */
public class SERIALIZE_RESULTS extends EvalFunc<Tuple> {

	private static final Logger logger = LoggerFactory.getLogger(SERIALIZE_RESULTS.class);
	
	@Override
	public Schema outputSchema(Schema input) {
		try {
			return Schema.generateNestedSchema(DataType.TUPLE, DataType.CHARARRAY,  DataType.BYTEARRAY);
		} catch (FrontendException e) {
			logger.error("Error in creating output schema:", e);
			throw new IllegalStateException(e);
		}
	}

	/*
	 * an input should follow schema (docId:chararray,{(docIdB,sim)}) 
	 */
	@Override
	public Tuple exec(Tuple input) throws IOException {
		
		if (input == null || input.size() == 0) {
			return null;
		}

		try {
			DocumentSimilarityInfo.Builder outb = DocumentSimilarityInfo.newBuilder();
			String docIdA = (String) input.get(0);
			outb.setDocIdA(docIdA);
			String type = (String) input.get(2);
			outb.setType(type);
			
			DataBag bd = (DataBag)input.get(1);
			ArrayList<SecondDocInfo> sdil = new ArrayList<SecondDocInfo>(); 
			for(Tuple in : bd){
				SecondDocInfo.Builder sdib = SecondDocInfo.newBuilder();
				sdib.setDocIdB((String) in.get(1));
				sdib.setSimilarity((Float) in.get(2));
				sdil.add(sdib.build());
			}
			outb.addAllSecondDocInfo(sdil);
			
			Tuple result = TupleFactory.getInstance().newTuple();
            result.append(docIdA);
            result.append(new DataByteArray(outb.build().toByteArray()));
            return result;

		} catch (Exception e) {
			logger.error("Error in processing input row:"+ StackTraceExtractor.getStackTrace(e), e);
			throw new IOException("Caught exception processing input row:\n"
					+ StackTraceExtractor.getStackTrace(e));
		}
	}
}
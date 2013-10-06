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

package pl.edu.icm.coansys.classification.documents.pig.proceeders;

import java.io.IOException;
import java.util.HashSet;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
public class FLAT_POS_NEG extends EvalFunc<DataBag> {

    private static final Logger logger = LoggerFactory.getLogger(FLAT_POS_NEG.class);
    private TupleFactory tf = TupleFactory.getInstance();

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
        	Schema.FieldSchema keyA = new Schema.FieldSchema("keyA",DataType.CHARARRAY); 
        	Schema.FieldSchema categ = new Schema.FieldSchema("categ",DataType.CHARARRAY);
        	Schema.FieldSchema pos = new Schema.FieldSchema("pos",DataType.INTEGER);
        	Schema.FieldSchema neg = new Schema.FieldSchema("neg",DataType.INTEGER);
        	
        	Schema tupleSchema = new Schema();
        	tupleSchema.add(keyA);
        	tupleSchema.add(categ);
        	tupleSchema.add(pos);
        	tupleSchema.add(neg);
        	
        	Schema.FieldSchema innerTup = new Schema.FieldSchema("tuple_of_categs_eval",tupleSchema,DataType.TUPLE);
        	Schema bagSchema = new Schema(innerTup);
        	
        	Schema.FieldSchema general = new Schema.FieldSchema("bag_of_tuples",bagSchema,DataType.BAG);
        	return new Schema(general);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }

    
    
    @Override
    public DataBag exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
        	StringBuffer sb = new StringBuffer();
        	sb.append(FLAT_POS_NEG.class.getSimpleName()+":\t");
        	sb.append("Problematic input tuple detected");
        	sb.append("\tinput==null ? "+input==null);
        	sb.append("\tinput.size()==0 ? "+(input.size()==0));
        	sb.append("\nreturning NULL value");
        	logger.error(sb.toString());
            return null;
        }
        try {
            String keyA = (String) input.get(0);
            DataBag categsA = (DataBag) input.get(2);
            DataBag categsB = (DataBag) input.get(3);

            if (keyA == null || categsA == null || categsB == null) {
            	StringBuffer sb = new StringBuffer();
            	sb.append("Some of following values are null:\t");
            	sb.append("keyA="+keyA+"\t");
            	sb.append("categsA="+categsA+"\t");
            	sb.append("categsB="+categsB+"\t");
            	sb.append("\nreturning NULL value");
            	logger.error(sb.toString());
                return null;
            }

            HashSet<String> hsAi = new HashSet<String>();
            HashSet<String> hsBi = new HashSet<String>();

            for (Tuple t : categsA) {
                 hsAi.add((String) t.get(0));
            }
            for (Tuple t : categsB) {
                hsBi.add((String) t.get(0));
            }

            HashSet<String> hsAs = new HashSet<String>(hsAi);
            HashSet<String> hsBs = new HashSet<String>(hsBi);
            
            hsAi.retainAll(hsBi);
            DataBag ret = new DefaultDataBag();
            for(String categ : hsAi){
            	Tuple t = tf.newTuple();
            	t.append(keyA);
            	t.append(categ);
            	t.append(1);
            	t.append(0);
            	ret.add(t);
            }
           
            hsBs.removeAll(hsAs);
            for(String categ : hsBs){
            	Tuple t = tf.newTuple();
            	t.append(keyA);
            	t.append(categ);
            	t.append(0);
            	t.append(1);
            	ret.add(t);
            }
            
            return ret;
        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\t"
                    + StackTraceExtractor.getStackTrace(e).replace("\n", "\t"));
        }
    }
}

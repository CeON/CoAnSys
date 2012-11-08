/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.proceeders;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;

/**
*
* @author pdendek
*/
public class CHECK_CLASSIF extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.INTEGER);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			DataBag categReal = (DataBag) input.get(0);
			DataBag categNeigh = (DataBag) input.get(1);
			DataBag categClassif = (DataBag) input.get(2);
			
			ArrayList<String> T = new ArrayList<String>();
			ArrayList<String> P = new ArrayList<String>();
			ArrayList<String> toChoose = new ArrayList<String>();
			
			ArrayList<String> F = null;
			ArrayList<String> N = null;
			int tp,tn,fp,fn;

			for(Tuple t : categReal) T.add((String) t.get(0));
			for(Tuple t : categNeigh){
				toChoose.add((String) t.get(0));
			}
			for(Tuple t : categClassif) P.add((String) t.get(0));
			
			F = cloneArrayList(toChoose);
			F.removeAll(T);
			N = cloneArrayList(toChoose);
			N.removeAll(P);
			
			tp = intersectSize(T,P);
			fp = intersectSize(F,P);
			tn = intersectSize(T,N);
			fn = intersectSize(F,N);
			
			Object[] to = new Object[]{tp,tn,fp,fn};
		    return TupleFactory.getInstance().newTuple(Arrays.asList(to));
			
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
		}
	}

	private int intersectSize(ArrayList<String> X, ArrayList<String> Y) {
		ArrayList<String> local = cloneArrayList(X);
		local.retainAll(Y);
		return local.size();
	}

	private ArrayList<String> cloneArrayList(ArrayList<String> in){
		ArrayList<String> ret = new ArrayList<String>();
		for(String s : in) ret.add(s);
		return ret;
	}
}

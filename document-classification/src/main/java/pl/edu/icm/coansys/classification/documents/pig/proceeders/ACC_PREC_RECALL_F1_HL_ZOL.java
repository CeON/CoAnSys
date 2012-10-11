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

public class ACC_PREC_RECALL_F1_HL_ZOL extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.INTEGER, DataType.INTEGER,
					DataType.INTEGER, DataType.INTEGER,
					DataType.INTEGER, DataType.DOUBLE);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			DataBag categReal = (DataBag) input.get(0);
			DataBag categClassif = (DataBag) input.get(1);
			
			ArrayList<String> real = new ArrayList<String>();
			ArrayList<String> classif = new ArrayList<String>();

			for(Tuple t : categReal) real.add((String) t.get(0));
			for(Tuple t : categClassif) classif.add((String) t.get(0));
			
			int is = intersectSize(real, classif);
			double acc = is/sumSize(real, classif);
			double p = is/classif.size();
			double r = is/real.size();
			
			Double f1 = p+r != 0 ? 2*p*r/(p+r) : null;
			
			double hl = sumSize(subs(real,classif), subs(classif,real));
			int zol = is == real.size() && is == classif.size() ? 1 : 0;
			
			Object[] obj = new Object[]{acc,p,r,f1,hl,zol};
			return TupleFactory.getInstance().newTuple(Arrays.asList(obj));
		}catch(Exception e){
			throw new IOException("Caught exception processing input row ", e);
		}
	}

	private int intersectSize(ArrayList<String> X, ArrayList<String> Y) {
		ArrayList<String> local = cloneArrayList(X);
		local.retainAll(Y);
		return local.size();
	}
	
	private int sumSize(ArrayList<String> X, ArrayList<String> Y) {
		ArrayList<String> local = cloneArrayList(X);
		local.addAll(Y);
		return local.size();
	}

	private ArrayList<String> subs(ArrayList<String> X, ArrayList<String> Y) {
		ArrayList<String> local = cloneArrayList(X);
		local.removeAll(Y);
		return local;
	}
	
	private ArrayList<String> cloneArrayList(ArrayList<String> in){
		ArrayList<String> ret = new ArrayList<String>();
		for(String s : in) ret.add(s);
		return ret;
	}
}

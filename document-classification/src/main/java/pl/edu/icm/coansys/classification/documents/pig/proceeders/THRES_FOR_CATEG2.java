/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.proceeders;


import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;

/**
*
* @author pdendek
*/
public class THRES_FOR_CATEG2 extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.CHARARRAY, DataType.INTEGER, DataType.DOUBLE);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}

	//C1: {categ: chararray,count: long,occ_pos: long,occ_neg: long}
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			Integer num = (Integer) input.get(0);
			String categ = (String) input.get(1);
			DataBag db = (DataBag) input.get(2);
			
			long[] pos = new long[num+1];
			long[] neg = new long[num+1];
			Arrays.fill(pos, 0);
			Arrays.fill(neg, 0);
			for(Tuple t : db){
				pos[(Integer)t.get(0)]=(Long)t.get(1);
				neg[(Integer)t.get(0)]=(Long)t.get(2);
			}

			int thres = -1;
			double bestF1 = 0;
			
			
			for(int i = 1; i<num;i++){
				int TP = countLess(i,pos);
				int TN = countEqMore(i,pos);
				int FP = countLess(i,neg);
				int FN = countEqMore(i,neg);
				double F1 = countF1(TP,TN,FP,FN);
				if(F1>bestF1){
					thres = i;
					bestF1 = F1;
				}
			}
			System.out.println("Calculated the best threshold");
			if(thres!=-1){
				Object[] to = new Object[]{categ,thres, bestF1};
		        return TupleFactory.getInstance().newTuple(Arrays.asList(to));
			}else{
				return null;
			}
		}catch(Exception e){
			// Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
		}
	}

	private double countF1(int tp, int tn, int fp, int fn) {
		double denominator = (tp+fp);
		double p = denominator!=0 ? tp/denominator : Double.POSITIVE_INFINITY;
		denominator = (tp+fn);
		double r = denominator!=0 ? tp/denominator : Double.POSITIVE_INFINITY;
		denominator = p!=Double.POSITIVE_INFINITY && r!=Double.POSITIVE_INFINITY ? (p+r) : -1;
		return denominator!=0 ? 2*(p*r)/denominator :0;
	}

	private int countEqMore(int curr, long[] posc) {
		int ret = 0;
		for(int i = curr; i<posc.length; i++) ret+=posc[i];
		return ret;
	}

	private int countLess(int curr, long[] posc) {
		int ret = 0;
		for(int i = 0; i<curr; i++) ret+=posc[i];
		return ret;
	}

}

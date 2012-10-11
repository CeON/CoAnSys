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

public class THRES_FOR_CATEG extends EvalFunc<Tuple>{

	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.TUPLE, 
					DataType.CHARARRAY, DataType.INTEGER, DataType.DOUBLE);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	
	public Tuple exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			Object o0 = input.get(0);
			String categA = o0==null ? "" : (String) input.get(0); //categPOS
			Object o1 = input.get(1);
			DataBag pos = o1==null ? new DefaultDataBag() : (DataBag) input.get(1);

			Object o2 = input.get(2);
			String categB = o2==null ? "" : (String) input.get(2); //categPOS
			Object o3 = input.get(3);
			DataBag neg = o3==null ? new DefaultDataBag() : (DataBag) input.get(3);
			
			Integer neight_max = Integer.parseInt(input.get(4).toString());
			
			String categ = "".equals(categA)? categB : categA;
			
			int[] posc = new int[neight_max];
			int[] negc = new int[neight_max];
			Arrays.fill(posc, 0);
			Arrays.fill(negc, 0);
			
			System.out.println("Start");
			for(Tuple t : pos){
				long t1 = (Long) t.get(1);
				long t2 = (Long) t.get(2);
				posc[(int)t1] = (int) t2;
			}
			System.out.println("Constructed pos array");
			for(Tuple t : neg){
				long t1 = (Long) t.get(1);
				long t2 = (Long) t.get(2);
				negc[(int)t1] = (int) t2;
			}
			System.out.println("Constructed neg array");
			int thres = -1;
			double bestF1 = 0;
			
			
			for(int i = 1; i<neight_max;i++){
				int TP = countLess(i,posc);
				int TN = countEqMore(i,posc);
				int FP = countLess(i,negc);
				int FN = countEqMore(i,negc);
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
			throw new IOException("Caught exception processing input row "+e);
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

	private int countEqMore(int curr, int[] posc) {
		int ret = 0;
		for(int i = curr; i<posc.length; i++) ret+=posc[i];
		return ret;
	}

	private int countLess(int curr, int[] posc) {
		int ret = 0;
		for(int i = 0; i<curr; i++) ret+=posc[i];
		return ret;
	}

}

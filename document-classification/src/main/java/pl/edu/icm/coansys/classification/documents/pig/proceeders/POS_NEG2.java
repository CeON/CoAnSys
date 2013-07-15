/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.classification.documents.pig.proceeders;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.PorterStemmer;
import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.classification.documents.auxil.StopWordsRemover;

/**
*
* @author pdendek
*/
public class POS_NEG2 extends EvalFunc<DataBag>{
	
	private class Pair<X,Y>{
		X x;
		Y y;
		
		public Pair(X x, Y y){
			this.x = x;
			this.y = y;
		}
	}
	
	@Override
	public Schema outputSchema(Schema p_input){
		try{
			return Schema.generateNestedSchema(DataType.BAG, 
					DataType.CHARARRAY, DataType.INTEGER, DataType.INTEGER);
		}catch(FrontendException e){
			throw new IllegalStateException(e);
		}
	}
	//AA: {group: chararray,A: {(keyA: chararray,keyB: chararray,sim: double,categsA: {(categA: chararray)},categsB: {(categB: chararray)})}}
	public DataBag exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			//group: chararray
			String keyA = (String) input.get(0);
			//A: {(keyA: chararray,keyB: chararray,sim: double,categsA: {(categA: chararray)},categsB: {(categB: chararray)})}
			DataBag bag = (DataBag) input.get(1); 
			
			ArrayList<String> neighCCList = new ArrayList<String>();
			HashSet<String> neighCCSet = new HashSet<String>(); 
			HashSet<String> docCCSet = new HashSet<String>();
			
			boolean haveNotDataAboutDocA = true;
			//0 keyA: chararray
			//1 keyB: chararray
			//2 sim: double
			//3 categsA: {(categA: chararray)}
			//4 categsB: {(categB: chararray)})
			for(Tuple t : bag){
				if(haveNotDataAboutDocA){ 
					for(Tuple ccA : (DataBag)t.get(3)){
						docCCSet.add((String)ccA.get(0));
					}
					haveNotDataAboutDocA = false;
				} 
				try{
					for(Tuple ccA : (DataBag)t.get(4)){
						String cc = (String)ccA.get(0);
						neighCCList.add(cc);
						neighCCSet.add(cc);
					}
				}catch(NullPointerException npe){
					try{
						System.out.println("The (neighbour) document "+(String)t.get(2)+" associated with the document "+keyA+" has no classification codes!!! Ignoring issue...");
						System.out.println("Caught exception processing input row:\t"
		            		+StackTraceExtractor.getStackTrace(npe).replace("\n", "\t"));
					}catch(Exception e){
						System.out.println("Could not log the NullPointerException (probably in pl.edu.icm.coansys.classification.documents.pig.proceeders.POS_NEG2.java:81)");
						System.out.println("This issue will be ignored");
					}
				}
			}
			
			ArrayList<Pair<String,Integer>> posPairs = new ArrayList<Pair<String,Integer>>();
			ArrayList<Pair<String,Integer>> negPairs = new ArrayList<Pair<String,Integer>>();
			
			for(String cc : docCCSet)
				posPairs.add(new Pair<String,Integer>(cc,Collections.frequency(neighCCList, cc)));
			neighCCSet.removeAll(docCCSet);
			for(String cc : neighCCSet)
				negPairs.add(new Pair<String,Integer>(cc,Collections.frequency(neighCCList, cc)));
		
			List<Tuple> alt = new ArrayList<Tuple>(); 
			for(Pair<String,Integer> p : posPairs){
				Object[] to = new Object[]{p.x,p.y,0};
		        alt.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
			}
			for(Pair<String,Integer> p : negPairs){
				Object[] to = new Object[]{p.x,0,p.y};
		        alt.add(TupleFactory.getInstance().newTuple(Arrays.asList(to)));
			}			
			return new DefaultDataBag(alt);
			
		}catch(Exception e){
            throw new IOException("Caught exception processing input row:\t"
            		+StackTraceExtractor.getStackTrace(e).replace("\n", "\t"));
		}
	}
}

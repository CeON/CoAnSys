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

package pl.edu.icm.coansys.similarity.pig.serializers;

import java.io.IOException;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.builtin.PigStorage;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.tools.pigstats.PigStatusReporter;

/**
 * The extension for PigStorage, 
 * which add in the first column
 * rank number against selected column, 
 * in the selected direction (ASC,DESC)
 * with the selected sparsity (DENSE,SPARSE).
 * 
 * Data to be stored should be
 * (a) previously sorted in the same direction!
 * (b) grouped with the parallelity level equal to 1
 */
public class RankStorage extends PigStorage {

	TupleFactory tf = TupleFactory.getInstance();
	
	enum PACKING{
		DENSE,
		SPARSE
	}
	
	enum SORT_ORDER{
		ASC,
		DESC
	}
	
	volatile static PigStatusReporter mReporter =  null;
	
	protected int fieldNumber = 0;
	protected SORT_ORDER sortOrder = SORT_ORDER.ASC;
	protected PACKING packing = PACKING.SPARSE;
	
//	static protected Long lastValue = null;
	
	public static Long getLastValue() {
		Long l = mReporter.getCounter("RankVals", "lastVal").getValue();
//		System.out.println("=========== getLastValue:"+l+" ===========");
		return l;
				
	}

	public static void setLastValue(Long lastValue) {
		mReporter.getCounter("RankVals", "lastVal").setValue(lastValue);
	}

	public static Long getRank() {
		long l = mReporter.getCounter("RankVals", "rankVal").getValue();
//		System.out.println("=========== getRank:"+l+" ===========");
		return l;
	}

	public static void setRank(Long rank) {
		mReporter.getCounter("RankVals", "rankVal").setValue(rank);
	}

	public static Long getInterval() {
		Long l = mReporter.getCounter("RankVals", "interval").getValue();
//		System.out.println("=========== getInterval:"+l+" ===========");
		return l;
	}

	public static void setInterval(Long interval) {
		mReporter.getCounter("RankVals", "interval").setValue(interval);
	}

//	static protected Long rank = 0l;
//	static protected Long interval = 1l;
	
	public RankStorage(String delim, int fieldNumber, String sortOrder, String packing){
		super(delim);
		this.fieldNumber = fieldNumber;
		this.sortOrder = SORT_ORDER.valueOf(sortOrder.toUpperCase());
		this.packing = PACKING.valueOf(packing.toUpperCase());
	}
	
	public RankStorage(String delim){
		super(delim);
	}
	
	public RankStorage(int fieldNumber){
		super();
		this.fieldNumber=fieldNumber;
	}
	
	public RankStorage(){
		super();
	}
	
	@Override
    public void putNext(Tuple t) throws IOException {
        try {
        	if(mReporter==null){
        		mReporter = PigStatusReporter.getInstance();
        		mReporter.getCounter("RankVals", "lastVal").setValue(Long.MIN_VALUE);
        		mReporter.getCounter("RankVals", "rankVal").setValue(0);
        		mReporter.getCounter("RankVals", "interval").setValue(1);
        	}
        	if(sortOrder != SORT_ORDER.ASC 
    				|| packing != PACKING.SPARSE){
    			throw new Exception(
    					"Only SORT_ORDER.ASC "
    					+ "and PACKING.SPARSE "
    					+ "are currently supported!");
    		}
        	t = rankAscDense(t);   	
            writer.write(null, t);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

	private Tuple rankAscDense(Tuple t) throws ExecException {
		
		Tuple outT = tf.newTuple();
		Long currValue = (Long) t.get(this.fieldNumber);
//		System.out.println("************ currValue: "+currValue+" ***********");
		if(!currValue.equals(getLastValue())){
			setRank(getRank()+getInterval());
			setInterval(1l);
			setLastValue(currValue);
			outT.append(getRank());
			for(Object o : t){
				outT.append(o);
			}
		}else{
			setInterval(getInterval()+1);
			outT.append(getRank());
			for(Object o : t){
				outT.append(o);
			}
		}
		return outT;
	}
}


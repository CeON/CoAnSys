/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

public class IntWritablePair extends Pair<IntWritable, IntWritable> implements Writable {
	
	public IntWritablePair() {
		super();
	}
	
	public IntWritablePair(IntWritable n, IntWritable k) {
		super(n,k);
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		first = new IntWritable(arg0.readInt());
		second = new IntWritable(arg0.readInt());
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(first.get());
		arg0.writeInt(second.get());
		
	}
}

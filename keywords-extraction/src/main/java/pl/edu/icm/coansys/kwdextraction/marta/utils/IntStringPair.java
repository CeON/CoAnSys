/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Comparator;
import org.apache.hadoop.io.WritableComparable;

public class IntStringPair implements WritableComparable<IntStringPair> {

	protected Integer num;
	protected String text;

	
	public IntStringPair() {
		num = new Integer(0);
		text = new String();
	}
	
	public IntStringPair(Integer k, String s) {
		set (k, s);
	}
	
	public Integer getFirst() {
		return num;
	}
	
	public String getSecond() {
		return text;
	}
	
	public void setFirst (Integer k) {
		num = k;
	}
	public void setSecond (String s) {
		text = s;
	}
	public void set (Integer k, String s) {
		num = k;
		text = s;
	}
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		num = arg0.readInt();
		text = arg0.readUTF();

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeInt(num);
		arg0.writeUTF(text);

	}
	
	public static Comparator<IntStringPair> getComparator() {
		return new Comparator<IntStringPair>() {

			@Override
			public int compare(IntStringPair o1, IntStringPair o2) {
				// TODO Auto-generated method stub
				int cmp = o1.getFirst().compareTo(o2.getFirst());
				if (cmp != 0)
					return -cmp;
				return (o1.getSecond().compareTo(o2.getSecond()));
				
			}
		};
	}

	@Override
	public String toString() {
		return num.toString().concat(" ").concat(text);
	}

	
	@Override
	public int compareTo(IntStringPair arg0) {
		// TODO Auto-generated method stub
		return num.compareTo(arg0.getFirst());
	}

}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

public class StringPair implements Writable {
	
	private String first;
	private String second;

	public StringPair() {
		first = "";
		second = "";
	}
	
	public StringPair(String s1, String s2) {
		set (s1, s2);
	}
	
	public String getFirst() {
		return first;
	}
	
	public String getSecond() {
		return second;
	}
	
	public void setFirst (String s) {
		first = s;
	}
	public void setSecond (String s) {
		second = s;
	}
	public void set (String s1, String s2) {
		first = s1;
		second = s2;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		first = arg0.readUTF();
		second = arg0.readUTF();

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(first);
		arg0.writeUTF(second);
	}

}

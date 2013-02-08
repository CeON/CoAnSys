/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class TextPair implements Writable {
	
	  private Text first;
	  private Text second;
	  
	  public TextPair() {
	    set(new Text(), new Text());
	  }
	  
	  public TextPair(String first, String second) {
	    set(new Text(first), new Text(second));
	  }
	  
	  public TextPair(Text first, Text second) {
	    set(first, second);
	  }
	  
	  public void set(Text first, Text second) {
	    this.first = first;
	    this.second = second;
	  }
	  
	  public Text getFirst() {
	    return first;
	  }

	  public Text getSecond() {
	    return second;
	  }

	  @Override
	  public void write(DataOutput out) throws IOException {
	    first.write(out);
	    second.write(out);
	  }

	  @Override
	  public void readFields(DataInput in) throws IOException {
	    first.readFields(in);
	    second.readFields(in);
	  }

}

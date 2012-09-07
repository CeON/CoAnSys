/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.similarity.documents.auxil;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.Writable;

/**
 * 
 * @author pdendek
 *
 */
public class TextTextWritable implements Writable, Serializable {

	private static final long serialVersionUID = 8606642353828143464L;
	private String textA;
	private String textB;
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeChars(textA);
		out.writeChars(textB);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		textA = in.readLine();
		textB = in.readLine();
	}
}

    
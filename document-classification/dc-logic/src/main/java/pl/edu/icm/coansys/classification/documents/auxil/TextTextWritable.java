/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.classification.documents.auxil;

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

    
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

package pl.edu.icm.coansys.classification.documents.auxil;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Writable;

/**
 * 
 * @author pdendek
 *
 */
public class StringListIntListWritable implements Writable, Serializable {

	private static final long serialVersionUID = 7342631270434741460L;

	private List<String> stringList;
	private int slLength;
	private List<Integer> intList;
	private int ilLength;
	
	public StringListIntListWritable(){
		stringList = new ArrayList<String>();
		slLength = 0;
		intList = new ArrayList<Integer>();
		ilLength = 0;
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(stringList.size()); 
		for(String s : stringList){
			out.writeChars(s);
		}
		out.writeInt(intList.size());
		for(Integer i : intList){
			out.writeInt(i);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		slLength = in.readInt();
		for(int i = 0; i<slLength;i++){
			stringList.add(in.readLine());
		}
		ilLength = in.readInt();
		for(int i = 0; i<ilLength;i++){
			intList.add(in.readInt());
		}
	}
	
	public void setStringList(List<String> stringList){
		this.stringList = stringList;
		slLength = stringList.size();
	}	
	
	public void addString(String s){
		stringList.add(s);
		slLength++;
	}
	
	public void clearStringList(){
		stringList.clear();
		slLength = 0;
	}
	
	public void addAllStrings(List<String> stringList) {
		this.stringList.addAll(stringList);
	}
	
	public List<Integer> getIntList(){
		return intList;
	}
	
	public void setIntList(List<Integer> intList){
		this.intList = intList;
		ilLength = intList.size();
	}	
	
	public void addInt(Integer i){
		intList.add(i);
		ilLength++;
	}
	
	public void clearIntList(){
		intList.clear();
		ilLength = 0;
	}
	
	
	public List<String> getStringList(){
		return stringList;
	}

	public void addAllInt(List<Integer> intList) {
		this.intList.addAll(intList);
	}
}

    
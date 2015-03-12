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

package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.Tuple;
//import org.apache.pig.piggybank.storage.XMLLoader;

public class XmlStorageWithInputPath { 
	/*extends XMLLoader {
	
    
	private Path path;

	public XmlStorageWithInputPath(){
		super();
	}
	
	public XmlStorageWithInputPath(String str){
		super(str);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void prepareToRead(RecordReader reader, PigSplit split)
            throws IOException {
		super.prepareToRead(reader, split);
		this.path = ((FileSplit)split.getWrappedSplit()).getPath();
    }
    
    @Override
    public Tuple getNext() throws IOException {
        Tuple myTuple = super.getNext();
        if (myTuple != null)
           myTuple.append(path.toString());
        return myTuple;
    }
} */
}

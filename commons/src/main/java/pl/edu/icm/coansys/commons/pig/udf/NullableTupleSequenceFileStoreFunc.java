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
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.pig.StoreFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.io.NullableTuple;

/**
 * A Storer for Hadoop-Standard SequenceFiles. 
 * Data given for write are encapsulated into NullableTuple.
 * Created in such a way SequenceFile can be read by {@link RichSequenceFileLoader}
 */
public class NullableTupleSequenceFileStoreFunc extends StoreFunc {
    private RecordWriter<NullableTuple, NullableTuple> writer;
    
	@Override
	public OutputFormat<NullableTuple, NullableTuple> getOutputFormat() throws IOException {
		return new SequenceFileOutputFormat<NullableTuple,NullableTuple>();
	}

	@Override
	public void setStoreLocation(String location, Job job) throws IOException {
		FileOutputFormat.setOutputPath(job, new Path(location));
		job.setOutputValueClass(NullableTuple.class);
		job.setOutputKeyClass(NullableTuple.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareToWrite(@SuppressWarnings("rawtypes") RecordWriter writer) throws IOException {
		this.writer = writer;
	}

	@Override
	public void putNext(Tuple t) throws IOException {
		if(t.size() != 2) {
			throw new ExecException("Output tuple has wrong size: is " + t.size() + ", should be 2");
		}
		byte[] keyBytes = ((DataByteArray) t.get(0)).get();
		byte[] valueBytes = ((DataByteArray) t.get(1)).get();
		if (keyBytes == null || valueBytes == null) {
			throw new ExecException("Output tuple contains null");
		}

		ArrayList<byte[]> alk = new ArrayList<byte[]>();
		alk.add(keyBytes);
	    NullableTuple key = new NullableTuple(TupleFactory.getInstance().newTuple(alk));
	    ArrayList<byte[]> alv = new ArrayList<byte[]>();
		alv.add(valueBytes);
		NullableTuple val = new NullableTuple(TupleFactory.getInstance().newTuple(alv));

		try {
			writer.write(key, val);
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}
}

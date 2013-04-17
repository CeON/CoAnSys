package pl.edu.icm.coansys.importers.pig.udf;

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

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
		SequenceFileOutputFormat<NullableTuple,NullableTuple> out = new SequenceFileOutputFormat<NullableTuple,NullableTuple>();
		return out;
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
                Object keyItem = t.get(0);
                Object valueItem = t.get(1);
                DataByteArray keyDBA;
                DataByteArray valueDBA;

                if (DataByteArray.class.isAssignableFrom(keyItem.getClass())) {
                    keyDBA = (DataByteArray) keyItem;
                } else {
                    keyDBA = new DataByteArray(keyItem.toString().getBytes());
                }
                
                if (DataByteArray.class.isAssignableFrom(valueItem.getClass())) {
                    valueDBA = (DataByteArray) valueItem;
                } else {
                    valueDBA = new DataByteArray(valueItem.toString().getBytes());
                }
                
		if (keyDBA == null || valueDBA == null) {
			throw new ExecException("Output tuple contains null");
		}

		ArrayList alk = new ArrayList();
		alk.add(keyDBA);
                NullableTuple key = new NullableTuple(TupleFactory.getInstance().newTuple(alk));
                ArrayList alv = new ArrayList();
		alv.add(valueDBA);
		NullableTuple val = new NullableTuple(TupleFactory.getInstance().newTuple(alv));

		try {
			writer.write(key, val);
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}
}
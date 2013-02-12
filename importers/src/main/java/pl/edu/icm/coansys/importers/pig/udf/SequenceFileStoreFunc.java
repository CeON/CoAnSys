package pl.edu.icm.coansys.importers.pig.udf;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.pig.StoreFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;

public class SequenceFileStoreFunc extends StoreFunc {
	RecordWriter<BytesWritable, BytesWritable> writer;

	@Override
	public OutputFormat getOutputFormat() throws IOException {
		return new SequenceFileOutputFormat<BytesWritable, BytesWritable>();
	}

	@Override
	public void setStoreLocation(String location, Job job) throws IOException {
		FileOutputFormat.setOutputPath(job, new Path(location));
		job.setOutputKeyClass(BytesWritable.class);
		job.setOutputValueClass(BytesWritable.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareToWrite(@SuppressWarnings("rawtypes") RecordWriter writer) throws IOException {
		this.writer = (RecordWriter<BytesWritable, BytesWritable>) writer;
	}

	@Override
	public void putNext(Tuple t) throws IOException {
		if(t.size() != 4) {
			throw new ExecException("Output tuple has wrong size: is " + t.size() + ", should be 4");
		}
		String keyString = (String) t.get(0);
		DataByteArray valueBytes = (DataByteArray) t.get(1);
		
		if (keyString == null || valueBytes == null) {
			throw new ExecException("Output tuple contains null");
		}
		
	    BytesWritable key = new BytesWritable();
	    BytesWritable val = new BytesWritable();
	    
		key.set(keyString.getBytes(), 0, keyString.length());
		val.set(valueBytes.get(), 0, valueBytes.get().length);
 		
		try {
			writer.write(key, val);
		} catch (InterruptedException e) {
			throw new IOException(e);
		}
	}
}

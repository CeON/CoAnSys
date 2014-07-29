package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.Tuple;
import org.apache.pig.piggybank.storage.XMLLoader;

public class XmlStorageWithInputPath extends XMLLoader {
	
    
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
}

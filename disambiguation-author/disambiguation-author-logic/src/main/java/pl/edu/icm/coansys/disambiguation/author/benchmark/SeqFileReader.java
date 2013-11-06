package pl.edu.icm.coansys.disambiguation.author.benchmark;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

public class SeqFileReader {
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, URISyntaxException{
		
		
            Configuration config = new Configuration();
	    Path path = new Path("/home/pdendek/icm_dane/pbn_20130827/pbn-20130729.sf");
	    SequenceFile.Reader reader = new SequenceFile.Reader(FileSystem.get(new URI("file:///"), config), path, config);
	    BytesWritable key = (BytesWritable) reader.getKeyClass().newInstance();
	    BytesWritable value = (BytesWritable) reader.getValueClass().newInstance();
	    int max = Integer.MIN_VALUE;
	    while(reader.next(key,value))
	    {
	    	int size = DocumentWrapper.parseFrom(value.copyBytes()).getDocumentMetadata().getBasicMetadata().getClassifCodeCount();
	    	max = max < size ? size : max;
	    }
	    System.out.println("MaxSize equals to: "+max);
	}
}

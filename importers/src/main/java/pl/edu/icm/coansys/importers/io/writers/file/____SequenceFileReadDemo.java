package pl.edu.icm.coansys.importers.io.writers.file;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

public class ____SequenceFileReadDemo {
	  
	  public static void main(String[] args) throws IOException {
	    String uri = "/home/pdendek/mproto-m-00000";
	    Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(URI.create(uri), conf);
	    Path path = new Path(uri);

	    SequenceFile.Reader reader = null;
	    try {
	      reader = new SequenceFile.Reader(fs, path, conf);
	      BytesWritable key = new BytesWritable();
	      BytesWritable value = new BytesWritable();
	      long position = reader.getPosition();
	      while (reader.next(key, value)) {
	        String syncSeen = reader.syncSeen() ? "*" : "";
	        
	        System.out.println("[KEY] "+new String(key.copyBytes(),"UTF-8")); 
	        System.out.println("[VAL] "+new String(value.copyBytes(),"UTF-8"));
	        position = reader.getPosition(); // beginning of next record
	        break;
	      }
	    } finally {
	      IOUtils.closeStream(reader);
	    }
	  }
	}

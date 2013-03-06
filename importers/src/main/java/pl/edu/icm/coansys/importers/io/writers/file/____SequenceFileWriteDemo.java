package pl.edu.icm.coansys.importers.io.writers.file;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

public class ____SequenceFileWriteDemo {
	  
	  private static final String[] DATA = {
	    "One, two, buckle my shoe",
	    "Three, four, shut the door",
	    "Five, six, pick up sticks",
	    "Seven, eight, lay them straight",
	    "Nine, ten, a big fat hen"
	  };
	  
	  public static void main(String[] args) throws IOException {
	    String uri = "/home/pdendek/seq_output";
	    Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(URI.create(uri), conf);
	    Path path = new Path(uri);

	    BytesWritable key = new BytesWritable();
	    BytesWritable val = new BytesWritable();
	    SequenceFile.Writer writer = null;
	    try {
	      writer = SequenceFile.createWriter(fs, conf, path,
	          key.getClass(), val.getClass());
	      
	      for (int i = 0; i < 100; i++) {
		    String keyText = (100 - i)+"";
		    String valText = DATA[i % DATA.length];
		    
		    key.set(keyText.getBytes(), 0, keyText.length());
		    val.set(valText.getBytes(), 0, valText.length());
		    
		    
		    System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), keyText, valText);
//		    System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), 
//		    		new String(key.copyBytes(), "UTF-8"), new String(val.copyBytes(), "UTF-8"));
		    writer.append(key, val);
	      }
	    } finally {
	      IOUtils.closeStream(writer);
	    }
	  }
	}
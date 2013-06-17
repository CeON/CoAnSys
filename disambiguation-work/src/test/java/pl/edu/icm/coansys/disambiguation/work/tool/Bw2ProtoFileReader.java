package pl.edu.icm.coansys.disambiguation.work.tool;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.beust.jcommander.internal.Lists;

public class Bw2ProtoFileReader {

    
    public static List<DocumentWrapper> readDocWrappers(String inputFilePath) {
        
        List<DocumentWrapper> docWrappers = Lists.newArrayList();
        
        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            URI uri = URI.create(inputFilePath);
            FileSystem fs = FileSystem.get(uri, conf);
            Path path = new Path(uri);
        
            reader = new SequenceFile.Reader(fs, path, conf);
            Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            while (reader.next(key, value)) {
                DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(((BytesWritable)value).copyBytes());
                docWrappers.add(docWrapper);
            }
            
        }
        catch (IOException e) {}
        finally {
            IOUtils.closeStream(reader);
        }
        return docWrappers;
    }
        
}

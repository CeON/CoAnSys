package pl.edu.icm.coansys.disambiguation.work.tool;

import static java.lang.System.out;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import pl.edu.icm.coansys.disambiguation.work.DocumentWrapperUtils;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;


public class Bw2ProtoFileUtils {

    public static List<DocumentWrapper> readDocWrappers(String inputFileUri) {
        
        List<DocumentWrapper> docWrappers = Lists.newArrayList();
        
        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            reader = getSequenceFileReader(inputFileUri, conf);
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

    
    public static void formatAndPrintToConsole(String inputFileUri) throws IOException {
        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            reader = getSequenceFileReader(inputFileUri, conf);
            Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            while (reader.next(key, value)) {
                DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(((BytesWritable)value).copyBytes());
                out.println(format(key, docWrapper));
            }
            
        }
        finally {
            IOUtils.closeStream(reader);
        }
    }
    
    
    
    //******************** PRIVATE ********************
    
    private static String format(Writable key, DocumentWrapper documentWrapper) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------------------\n");
        sb.append("key    : " + key + "\n");
        sb.append("rowid  : " + documentWrapper.getRowId() + "\n");
        sb.append("title0 : " + DocumentWrapperUtils.getMainTitle(documentWrapper) + "\n");
        sb.append("year   : " + DocumentWrapperUtils.getPublicationYear(documentWrapper) + "\n");
        for (Author author : documentWrapper.getDocumentMetadata().getBasicMetadata().getAuthorList()) {
            sb.append(author.getPositionNumber() + ". " + author.getName() + " " + author.getSurname() + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }
    
   
    @SuppressWarnings("deprecation")
    private static SequenceFile.Reader getSequenceFileReader(String inputFileUri, Configuration conf) throws IOException {
        SequenceFile.Reader reader;
        URI uri = URI.create(inputFileUri);
        FileSystem fs = FileSystem.get(uri, conf);
        Path path = new Path(uri);
      
        reader = new SequenceFile.Reader(fs, path, conf);
        return reader;
    }
        
}

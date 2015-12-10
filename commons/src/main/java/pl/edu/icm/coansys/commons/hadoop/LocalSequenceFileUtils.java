package pl.edu.icm.coansys.commons.hadoop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import com.google.common.collect.Lists;

/**
 * Util class for reading sequence files from local filesystem
 * 
 * @author madryk
 *
 */
public class LocalSequenceFileUtils {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    private LocalSequenceFileUtils() {
        throw new IllegalArgumentException("Can't instantiate " + LocalSequenceFileUtils.class.getName() + " class");
    }
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Reads sequence file from local filesystem.
     * Passed file object can be single file or a directory
     * that contains sequence file splitted into parts (part-* files).
     * Sequence file must contain keys and values of types
     * specified as method parameters. 
     */
    public static <K extends Writable, V extends Writable> List<Pair<K, V>> readSequenceFile(File sequenceFile, Class<K> keyClass, Class<V> valueClass) throws IOException {
        
        if (sequenceFile.isFile()) {
            return readSequenceFile(new Path("file://" + sequenceFile.getAbsolutePath()), keyClass, valueClass);
        }
        
        List<Pair<K, V>> records = Lists.newArrayList();
        
        for (File f : FileUtils.listFiles(sequenceFile, null, true)) {
            if (f.isFile() && f.getName().startsWith("part-")) {
                
                Path path = new Path("file://" + f.getAbsolutePath());
                List<Pair<K, V>> singleFileRecords = readSequenceFile(path, keyClass, valueClass);
                
                records.addAll(singleFileRecords);
            }
        }
        
        return records;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static <K extends Writable, V extends Writable> List<Pair<K, V>> readSequenceFile(Path path, Class<K> keyClass, Class<V> valueClass) throws IOException {
        List<Pair<K, V>> records = Lists.newArrayList();
        Configuration conf = new Configuration();
        
        try (SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path))) {
            
            K key = ReflectionUtils.newInstance(keyClass, conf);
            V value = ReflectionUtils.newInstance(valueClass, conf);
            
            while (reader.next(key, value)) {
                records.add(new ImmutablePair<K, V>(key, value));
            }
            
        }
        
        return records;
    }
    
}

package pl.edu.icm.coansys.commons.hbase;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

/**
 *
 * @author akawa
 */
public class SequenceFileSplitAlgorithm implements SplitAlgorithm {

    private static final String SPLIT_KEY_FILENAME_PROPERTY_NAME = "split.region.keys.file.name";
    public static final String SPLIT_KEY_FILE_DV = "keys";

    @Override
    public byte[] split(byte[] bytes, byte[] bytes1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[][] split(int numRegions) {

        Configuration conf = new Configuration();
        SequenceFile.Reader reader = null;
        String splitKeysFile = System.getProperty(SPLIT_KEY_FILENAME_PROPERTY_NAME, SPLIT_KEY_FILE_DV);
        
        List<byte[]> regions = new ArrayList<byte[]>();
        
        try {
            FileSystem fs = FileSystem.get(URI.create(splitKeysFile), conf);
            reader = new SequenceFile.Reader(fs, new Path(splitKeysFile), conf);
            BytesWritable key = (BytesWritable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);

            while (reader.next(key, value)) {
                regions.add(key.copyBytes());
            }

        } catch (Exception ex) {
            System.out.println("Be silent like a ninja: " + ex);
        } finally {
            IOUtils.closeStream(reader);
        }
        
        return regions.toArray(new byte[0][]);
    }

    @Override
    public byte[] firstRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] lastRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] strToRow(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String rowToStr(byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String separator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
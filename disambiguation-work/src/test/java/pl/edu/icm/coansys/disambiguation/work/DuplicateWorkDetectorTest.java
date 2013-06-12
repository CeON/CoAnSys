package pl.edu.icm.coansys.disambiguation.work;

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
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public class DuplicateWorkDetectorTest {

    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectorTest.class);
    
    private MapReduceDriver<Writable, BytesWritable, Text, BytesWritable, Text, BytesWritable> mapReduceDriver;
    
    @Before
    public void before() {
        Mapper<Writable, BytesWritable, Text, BytesWritable> mapper = new DuplicateWorkDetectMapper();
        Reducer<Text, BytesWritable, Text, BytesWritable> reducer = new DuplicateWorkDetectReducer();
        mapReduceDriver = new MapReduceDriver<Writable, BytesWritable, Text, BytesWritable, Text, BytesWritable>(mapper, reducer);
        //Configuration conf = mapReduceDriver.getConfiguration();
    }
    
    @Test
    public void test() throws IOException {
        addInputs(mapReduceDriver);
        List<Pair<Text, BytesWritable>> list = mapReduceDriver.run();
        for (Pair<Text, BytesWritable> pair : list) {
            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(pair.getSecond().copyBytes());
            log.info("key: {}, value: {}", pair.getFirst().toString(), DocumentWrapperHelper.getMainTitle(docWrapper));
        }
    }

    
    
    private void addInputs(MapReduceDriver<Writable, BytesWritable, Text, BytesWritable, Text, BytesWritable> mapReduceDriver) {
        SequenceFile.Reader reader = null;
        try {
            Configuration conf = new Configuration();
            URL url = this.getClass().getResource("/generated/ambiguous-publications.seq");
            URI uri = URI.create(url.getPath());
            FileSystem fs = FileSystem.get(uri, conf);
            Path path = new Path(uri);
        
        
            reader = new SequenceFile.Reader(fs, path, conf);
            Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
            while (reader.next(key, value)) {
                DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(((BytesWritable)value).copyBytes());
                mapReduceDriver.addInput(new Text(docWrapper.getRowId()), new BytesWritable(docWrapper.toByteArray()));
        
            }
            
        }
        catch (IOException e) {}
        finally {
                IOUtils.closeStream(reader);
        }
        
    }
}

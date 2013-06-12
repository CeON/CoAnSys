package pl.edu.icm.coansys.disambiguation.work;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;

import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public class DuplicateWorkDetectMapper  extends Mapper<Writable, BytesWritable, Text, BytesWritable>  {
    //private static Logger log = LoggerFactory.getLogger(FuzzyDuplicateWorkMapper.class);
    
    private static String KEY_START_SUBSTRING_LENGTH = "keyStartSubstringLength";
    
    @Override
    protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
        
        int keyLength = context.getConfiguration().getInt(KEY_START_SUBSTRING_LENGTH, 3);
        
        String hashKey = docWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText().substring(0, keyLength);
        
        context.write(new Text(hashKey), new BytesWritable(value.copyBytes()));
        
        
    }
}

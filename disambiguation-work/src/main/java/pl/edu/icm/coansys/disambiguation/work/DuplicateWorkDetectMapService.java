package pl.edu.icm.coansys.disambiguation.work;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.commons.spring.DiMapService;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("duplicateWorkDetectMapService")
public class DuplicateWorkDetectMapService implements DiMapService<Writable, BytesWritable, Text, BytesWritable> {

    private static String KEY_LENGTH = "keyLength";
    
    @Override
    public void map(Writable key, BytesWritable value, Mapper<Writable, BytesWritable, Text, BytesWritable>.Context context)
            throws IOException, InterruptedException {
        
        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
        
        int keyLength = context.getConfiguration().getInt(KEY_LENGTH, 3);
        
        String hashKey = docWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText().substring(0, keyLength);
        
        context.write(new Text(hashKey), new BytesWritable(value.copyBytes()));
        
        
    }

}

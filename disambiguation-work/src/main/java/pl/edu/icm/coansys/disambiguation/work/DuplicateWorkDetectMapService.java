package pl.edu.icm.coansys.disambiguation.work;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.commons.spring.DiMapService;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
@Service("duplicateWorkDetectMapService")
public class DuplicateWorkDetectMapService implements DiMapService<Writable, BytesWritable, Text, BytesWritable> {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectMapService.class);
    
    
    @Override
    public void map(Writable key, BytesWritable value, Mapper<Writable, BytesWritable, Text, BytesWritable>.Context context)
            throws IOException, InterruptedException {
        
        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
        
        String docKey = WorkKeyGenerator.generateKey(docWrapper, 0);
        
        DocumentWrapper thinDocWrapper = DocumentWrapperUtils.cloneDocumentMetadata(docWrapper);
        
        context.write(new Text(docKey), new BytesWritable(thinDocWrapper.toByteArray()));
        
        
    }

    
    //******************** PRIVATE ********************
    
    
}

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
import pl.edu.icm.coansys.disambiguation.work.tool.StringUtils;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("duplicateWorkDetectMapService")
public class DuplicateWorkDetectMapService implements DiMapService<Writable, BytesWritable, Text, BytesWritable> {

    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectMapService.class);
    
    public static String KEY_LENGTH = "keyLength";
    
    @Override
    public void map(Writable key, BytesWritable value, Mapper<Writable, BytesWritable, Text, BytesWritable>.Context context)
            throws IOException, InterruptedException {
        
        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
        
        int keyLength = context.getConfiguration().getInt(KEY_LENGTH, 5);
        
        String title = docWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText();
        
        String docKey = generateDocumentKey(keyLength, title);
        
        log.info("{}:{}", docKey, title);
        
        context.write(new Text(docKey), new BytesWritable(value.copyBytes()));
        
        
    }

    //******************** PRIVATE ********************
    
    private String generateDocumentKey(int keyLength, String title) {
        title = StringUtils.normalize(title);
        String docKey = title; 
        if (title.length() > keyLength) {
            docKey = docKey.substring(0, keyLength);
        }
        return docKey;
    }

   

}

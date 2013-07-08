package pl.edu.icm.coansys.disambiguation.work.tool;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.models.DocumentProtos;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 * A mapper that writes only every n-th record according to percentOfWritten attribute in context
 * Note this not thread safe implementation
 * @author Łukasz Dumiszewski
 *
 */
public class PartExtractingMapper extends Mapper<Writable, BytesWritable, Text, BytesWritable> {
    
    private static Logger log = LoggerFactory.getLogger(PartExtractingMapper.class);
    
    private int i = 1;
    
    @Override
    protected void map(Writable key, BytesWritable value, Context context) throws IOException, InterruptedException {

        int percentOfWritten = context.getConfiguration().getInt("percentOfWritten", 100);
        
        DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
        
        log.info("work title = " + docWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText());
        
                
        if ((i%101) > 100-percentOfWritten) {
            log.info("writing...");
            context.write(new Text(docWrapper.getRowId()), new BytesWritable(value.copyBytes()));
        }
        
        i++;
    }
}

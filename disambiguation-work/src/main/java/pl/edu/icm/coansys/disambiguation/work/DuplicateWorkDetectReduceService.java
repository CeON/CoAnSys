package pl.edu.icm.coansys.disambiguation.work;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.coansys.commons.spring.DiReduceService;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

@Service("duplicateWorkDetectReduceService")
public class DuplicateWorkDetectReduceService implements DiReduceService<Text, BytesWritable, Text, BytesWritable> {
    
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectReduceService.class);
    
    
    @Autowired
    private DuplicateWorkService duplicateWorkService;
        
    
    
    //******************** DiReduceService Implementation ********************
    
    @Override
    public void reduce(Text key, Iterable<BytesWritable> values, Reducer<Text, BytesWritable, Text, BytesWritable>.Context context) throws IOException, InterruptedException {
        
        List<DocumentWrapper> documents = DocumentWrapperUtils.extractDocumentWrappers(key, values);
        
        Map<Integer, Set<DocumentWrapper>> duplicateWorksMap = duplicateWorkService.findDuplicates(documents);
        
        saveDuplicatesToContext(duplicateWorksMap, key, context);
        
    }




    
    //******************** PRIVATE ********************
    
    
    private void saveDuplicatesToContext(Map<Integer, Set<DocumentWrapper>> sameWorksMap, Text key, Reducer<Text, BytesWritable, Text, BytesWritable>.Context context)
            throws IOException, InterruptedException {
        
        for (Map.Entry<Integer, Set<DocumentWrapper>> entry : sameWorksMap.entrySet()) {
            String sameWorksKey = key.toString() + "_" + entry.getKey();
            for (DocumentWrapper doc : entry.getValue()) {
                context.write(new Text(sameWorksKey), new BytesWritable(doc.toByteArray()));
            }
        }
        
    }
}

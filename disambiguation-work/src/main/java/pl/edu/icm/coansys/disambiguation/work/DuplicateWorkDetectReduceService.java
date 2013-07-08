package pl.edu.icm.coansys.disambiguation.work;

import java.io.IOException;
import java.util.Date;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * 
 * @author Łukasz Dumiszewski
 *
 */

@Service("duplicateWorkDetectReduceService")
public class DuplicateWorkDetectReduceService implements DiReduceService<Text, BytesWritable, Text, BytesWritable> {
    
    //@SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectReduceService.class);
    
    
    @Autowired
    private DuplicateWorkService duplicateWorkService;
        
    
    
    //******************** DiReduceService Implementation ********************
    
    @Override
    public void reduce(Text key, Iterable<BytesWritable> values, Reducer<Text, BytesWritable, Text, BytesWritable>.Context context) throws IOException, InterruptedException {
        
        List<DocumentWrapper> documents = DocumentWrapperUtils.extractDocumentWrappers(key, values);
        
        long startTime = new Date().getTime();
        
        process(key, context, documents, 0);
        
        log.info("time [sec]: " + (new Date().getTime()-startTime)/1000);
        
    }



    
    //******************** PRIVATE ********************
    

    private void process(Text key,  Reducer<Text, BytesWritable, Text, BytesWritable>.Context context,  List<DocumentWrapper> documents, int level) throws IOException, InterruptedException {
        log.info("reduce, key: {}, number of documents: {}", key.toString(), documents.size());
        
        level++;
        
        if (documents.size()>1000) {
            Map<Text, List<DocumentWrapper>> documentPacks = splitDocuments(key, documents, level);
            log.info("documents splitted into: {} packs", documentPacks.size());
            
            for (Map.Entry<Text, List<DocumentWrapper>> docs : documentPacks.entrySet()) {
                if (docs.getValue().size()<2) {
                    process(docs.getKey(), context, docs.getValue(), level);
                }
            }
            
            
        } else {
            Map<Integer, Set<DocumentWrapper>> duplicateWorksMap = duplicateWorkService.findDuplicates(documents);
            saveDuplicatesToContext(duplicateWorksMap, key, context);
            context.progress();
        }
    }




    
    private Map<Text, List<DocumentWrapper>> splitDocuments(Text key, List<DocumentWrapper> documents, int level) {
        Map<Text, List<DocumentWrapper>> splitDocuments = Maps.newHashMap();
        for (DocumentWrapper doc : documents) {
            Text newKey = new Text(WorkKeyGenerator.generateKey(doc, level));
            List<DocumentWrapper> list = splitDocuments.get(newKey);
            if (list == null) {
                list = Lists.newArrayList();
                splitDocuments.put(newKey, list);
            }
            list.add(doc);
        }
        
        return splitDocuments;
    }


    
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

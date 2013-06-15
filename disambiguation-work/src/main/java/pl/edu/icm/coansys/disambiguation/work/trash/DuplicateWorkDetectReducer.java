package pl.edu.icm.coansys.disambiguation.work.trash;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.edu.icm.coansys.disambiguation.work.DuplicateWorkVoter;
import pl.edu.icm.coansys.disambiguation.work.DuplicateWorkVoterConfiguration;
import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class DuplicateWorkDetectReducer extends Reducer<Text, BytesWritable, Text, BytesWritable> {

    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectReducer.class);
    
    @Autowired
    private DuplicateWorkVoter duplicateWorkVoter;
    
        
   
    
    @Override
    protected void reduce(Text key, Iterable<BytesWritable> values, Context context) throws IOException, InterruptedException {
        
        List<DocumentWrapper> documents = Lists.newArrayList();
        
        log.debug("--------\nkey: {}", key);
        for (BytesWritable value : values) {
            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            log.debug("value: {}", docWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText());
            documents.add(docWrapper);
        }
        
        
        Map<Integer, Set<DocumentWrapper>> sameWorksMap = Maps.newHashMap();
        
        List<DocumentWrapper> documentsCopy = Lists.newArrayList(documents);
        
        int i=0;
        for (DocumentWrapper document : documents) {
           
           for (DocumentWrapper other : new ArrayList<DocumentWrapper>(documentsCopy)) {
                
                if (document.getRowId().equals(other.getRowId())) {
                    documentsCopy.remove(other);
                } else {
                    if (duplicateWorkVoter.isDuplicate(document, other, DuplicateWorkVoterConfiguration.create())) {
                        addSameWorks(sameWorksMap, i++, document, other);
                        documentsCopy.remove(other);
                    }
                }
            }    
        }
        
        for (Map.Entry<Integer, Set<DocumentWrapper>> entry : sameWorksMap.entrySet()) {
            String sameWorksKey = key.toString() + "_" + entry.getKey();
            for (DocumentWrapper doc : entry.getValue()) {
                context.write(new Text(sameWorksKey), new BytesWritable(doc.toByteArray()));
            }
        }
        
        
    }

    private void addSameWorks(Map<Integer, Set<DocumentWrapper>> sameWorksMap, int i, DocumentWrapper document, DocumentWrapper other) {
        Set<DocumentWrapper> sameWorks = sameWorksMap.get(i);
        if (sameWorks==null) {
            sameWorks = Sets.newHashSet();
            sameWorksMap.put(i, sameWorks);
        }
        sameWorks.add(document);
        sameWorks.add(other);
    }

    
}

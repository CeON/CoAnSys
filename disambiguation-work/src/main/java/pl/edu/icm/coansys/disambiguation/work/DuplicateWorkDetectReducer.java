package pl.edu.icm.coansys.disambiguation.work;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Sets;

import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

public class DuplicateWorkDetectReducer extends Reducer<Text, BytesWritable, Text, BytesWritable> {

    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectReducer.class);
    
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
                    if (isSame(context, document, other)) {
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

    private boolean isSame(Context context, DocumentWrapper document, DocumentWrapper other) {
        int maxDistance = context.getConfiguration().getInt("MAX_DISTANCE", 5);
        
        String docTitle = DocumentWrapperHelper.getMainTitle(document);
        String otherTitle = DocumentWrapperHelper.getMainTitle(other);
        log.info("comparing:\n");
        log.info(docTitle);
        log.info(otherTitle);
        
        int distance = StringUtils.getLevenshteinDistance(docTitle, otherTitle);
        if (distance<maxDistance) {
            return true;
        }
        return false;
    }
}

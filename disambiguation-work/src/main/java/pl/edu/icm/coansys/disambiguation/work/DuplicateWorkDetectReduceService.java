/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.disambiguation.work;

import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
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
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

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
        
        process(key, context, documents, 0, 1000);
        
        log.info("time [msec]: " + (new Date().getTime()-startTime));
        
    }



    
    //******************** PRIVATE ********************
    
    /**
     * Processes the given documents: finds duplicates and saves them to the context under the same, common key.
     * If the number of the passed documents is greater than the <b>maxNumberOfDocuments</b> then the documents are split into smaller parts and
     * then the method is recursively invoked for each one of those parts.
     * @param key a common key of the documents
     * @param level the recursive depth of the method used to generate a proper key of the passed documents. The greater the level the longer (and more unique) the
     * generated key.
     */
    void process(Text key,  Reducer<Text, BytesWritable, Text, BytesWritable>.Context context,  List<DocumentWrapper> documents, int level, int maxNumberOfDocuments) throws IOException, InterruptedException {
        String dashes = getDashes(level);
        log.info(dashes+ "start process, key: {}, number of documents: {}", key.toString(), documents.size());
        if (documents.size()<2) {
            log.info(dashes+ "one document only, ommiting");
            return;
        }
        
        int lev = level + 1;
        int maxNumOfDocs = maxNumberOfDocuments;
        
        if (documents.size()>maxNumOfDocs) {
            Map<Text, List<DocumentWrapper>> documentPacks = splitDocuments(key, documents, lev);
            log.info(dashes+ "documents split into: {} packs", documentPacks.size());
            
            for (Map.Entry<Text, List<DocumentWrapper>> docs : documentPacks.entrySet()) {
                if (docs.getValue().size()==documents.size()) { // docs were not splitted, the generated key is the same for all the titles, may happen if the documents have the same short title, e.g. news in brief
                   maxNumOfDocs+=maxNumOfDocs; 
                }
                process(docs.getKey(), context, docs.getValue(), lev, maxNumOfDocs);
            }
            
            
        } else {
            Map<Integer, Set<DocumentWrapper>> duplicateWorksMap = duplicateWorkService.findDuplicates(documents);
            saveDuplicatesToContext(duplicateWorksMap, key, context);
            context.progress();
        }
        log.info(dashes+ "end process, key: {}", key);
        
    }
    
    private String getDashes(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<=level; i++) {
            sb.append("-");
        }
        return sb.toString();
    }
    
    /**
     * Splits the passed documents into smaller parts. The documents are divided into smaller packs according to the generated keys.
     * The keys are generated by using the {@link WorkKeyGenerator.generateKey(doc, level)} method.
     */
    Map<Text, List<DocumentWrapper>> splitDocuments(Text key, List<DocumentWrapper> documents, int level) {
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

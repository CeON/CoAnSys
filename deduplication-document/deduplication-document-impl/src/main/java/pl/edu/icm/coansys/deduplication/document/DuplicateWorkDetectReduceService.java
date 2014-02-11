/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.deduplication.document;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Value;
import pl.edu.icm.coansys.deduplication.document.keygenerator.WorkKeyGenerator;
import pl.edu.icm.coansys.models.DocumentProtos;


/**
 * 
 * @author Łukasz Dumiszewski
 *
 */

@Service("duplicateWorkDetectReduceService")
public class DuplicateWorkDetectReduceService implements DiReduceService<Text, BytesWritable, Text, Text> {
    
    //@SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DuplicateWorkDetectReduceService.class);
    
    
    @Autowired
    private DuplicateWorkService duplicateWorkService;
    
    @Autowired
    private WorkKeyGenerator keyGen;
        
    private int initialMaxDocsSetSize;
    private int maxDocsSetSizeInc;
    private int maxSplitLevel;
    
    //******************** DiReduceService Implementation ********************
    
    @Override
    public void reduce(Text key, Iterable<BytesWritable> values, Reducer<Text, BytesWritable, Text, Text>.Context context) throws IOException, InterruptedException {

        log.info("starting reduce, key: " + key.toString());
        
        List<DocumentProtos.DocumentMetadata> documents = DocumentWrapperUtils.extractDocumentMetadata(key, values);
        
        long startTime = new Date().getTime();

        Configuration conf = context.getConfiguration();
        initialMaxDocsSetSize = conf.getInt("INITIAL_MAX_DOCS_SET_SIZE", initialMaxDocsSetSize);
        maxDocsSetSizeInc = conf.getInt("MAX_DOCS_SET_SIZE_INC", maxDocsSetSizeInc);
        maxSplitLevel = conf.getInt("MAX_SPLIT_LEVEL", maxSplitLevel);
        
        process(key, context, documents, 0, initialMaxDocsSetSize);
        
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
    void process(Text key,  Reducer<Text, BytesWritable, Text, Text>.Context context,  List<DocumentProtos.DocumentMetadata> documents, int level, int maxNumberOfDocuments) throws IOException, InterruptedException {
        String dashes = getDashes(level);
        log.info(dashes+ "start process, key: {}, number of documents: {}", key.toString(), documents.size());
        if (documents.size()<2) {
            log.info(dashes+ "one document only, ommiting");
            return;
        }
        
        int lev = level + 1;
        int maxNumOfDocs = maxNumberOfDocuments;
        
        if (documents.size()>maxNumOfDocs) {
            Map<Text, List<DocumentProtos.DocumentMetadata>> documentPacks = splitDocuments(key, documents, lev);
            log.info(dashes+ "documents split into: {} packs", documentPacks.size());
            
            for (Map.Entry<Text, List<DocumentProtos.DocumentMetadata>> docs : documentPacks.entrySet()) {
                if (docs.getValue().size()==documents.size()) { // docs were not splitted, the generated key is the same for all the titles, may happen if the documents have the same short title, e.g. news in brief
                   maxNumOfDocs+=maxDocsSetSizeInc;
                }
                process(docs.getKey(), context, docs.getValue(), lev, maxNumOfDocs);
            }
            

        } else {
            if (isDebugMode(context.getConfiguration())) {
                duplicateWorkService.findDuplicates(documents, context);
            } else {
                Map<Integer, Set<DocumentProtos.DocumentMetadata>> duplicateWorksMap = duplicateWorkService.findDuplicates(documents, null);
                saveDuplicatesToContext(duplicateWorksMap, key, context);
            }
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
    
    private boolean isDebugMode(Configuration conf) {
        if (conf == null) {
            return false;
        }
        String debugOptionValue = conf.get("DEDUPLICATION_DEBUG_MODE", "false");
        return debugOptionValue.equals("true");
    }
    
    /**
     * Splits the passed documents into smaller parts. The documents are divided into smaller packs according to the generated keys.
     * The keys are generated by using the {@link WorkKeyGenerator.generateKey(doc, level)} method.
     */
    Map<Text, List<DocumentProtos.DocumentMetadata>> splitDocuments(Text key, List<DocumentProtos.DocumentMetadata> documents, int level) {
        
        // check if set was forced to split; if yes, keep the suffix
        String keyStr = key.toString();
        String suffix = "";
        if (keyStr.contains("-")) {
            String[] parts = keyStr.split("-");
            suffix = parts[1];
        }
        
        Map<Text, List<DocumentProtos.DocumentMetadata>> splitDocuments = Maps.newHashMap();
        for (DocumentProtos.DocumentMetadata doc : documents) {
            String newKeyStr = keyGen.generateKey(doc, level);
            if (!suffix.isEmpty()) {
                newKeyStr = newKeyStr + "-" + suffix;
            }
            Text newKey = new Text(newKeyStr);
            List<DocumentProtos.DocumentMetadata> list = splitDocuments.get(newKey);
            if (list == null) {
                list = Lists.newArrayList();
                splitDocuments.put(newKey, list);
            }
            list.add(doc);
        }

        if (level > maxSplitLevel && splitDocuments.size() == 1) {
            //force split into 2 parts
            Text commonKey = splitDocuments.keySet().iterator().next();
            String commonKeyStr = commonKey.toString();
            if (!commonKeyStr.contains("-")) {
                commonKeyStr += "-";
            }
            Text firstKey = new Text(commonKeyStr + "0");
            Text secondKey = new Text(commonKeyStr + "1");
            List<DocumentProtos.DocumentMetadata> fullList = splitDocuments.get(commonKey);
            int items = fullList.size();
            List<DocumentProtos.DocumentMetadata> firstHalf = fullList.subList(0, items/2);
            List<DocumentProtos.DocumentMetadata> secondHalf = fullList.subList(items/2, items);
            splitDocuments.clear();
            splitDocuments.put(firstKey, firstHalf);
            splitDocuments.put(secondKey, secondHalf);
        }
        
        return splitDocuments;
    }


    
    private void saveDuplicatesToContext(Map<Integer, Set<DocumentProtos.DocumentMetadata>> sameWorksMap, Text key, Reducer<Text, BytesWritable, Text, Text>.Context context)
            throws IOException, InterruptedException {
        
        for (Map.Entry<Integer, Set<DocumentProtos.DocumentMetadata>> entry : sameWorksMap.entrySet()) {
            String sameWorksKey = key.toString() + "_" + entry.getKey();
            for (DocumentProtos.DocumentMetadata doc : entry.getValue()) {
                context.write(new Text(sameWorksKey), new Text(doc.getKey()));
            }
        }
        
    }

    @Value("1000")
    public void setBeginPackSize(int beginPackSize) {
        this.initialMaxDocsSetSize = beginPackSize;
    }

    @Value("200")
    public void setPackSizeInc(int packSizeInc) {
        this.maxDocsSetSizeInc = packSizeInc;
    }

    @Value("10")
    public void setMaxSplitLevels(int maxSplitLevels) {
        this.maxSplitLevel = maxSplitLevels;
    }
    
    static enum UnparseableIssue { UNPARSEABLE };
}

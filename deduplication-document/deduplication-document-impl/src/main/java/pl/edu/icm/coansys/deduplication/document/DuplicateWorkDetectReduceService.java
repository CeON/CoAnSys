/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.icm.coansys.commons.java.DocumentWrapperUtils;
import pl.edu.icm.coansys.commons.spring.DiReduceService;
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
    //private int maxDocsSetSizeInc;
    //private int maxSplitLevel;

    //******************** DiReduceService Implementation ********************
    @Override
    public void reduce(Text key, Iterable<BytesWritable> values, Reducer<Text, BytesWritable, Text, Text>.Context context) throws IOException, InterruptedException {

        log.info("starting reduce, key: " + key.toString());

        //List<DocumentProtos.DocumentMetadata> documents = DocumentWrapperUtils.extractDocumentMetadata(key, values);
        long startTime = new Date().getTime();

        Configuration conf = context.getConfiguration();
        initialMaxDocsSetSize = conf.getInt("INITIAL_MAX_DOCS_SET_SIZE", initialMaxDocsSetSize);
        //maxDocsSetSizeInc = conf.getInt("MAX_DOCS_SET_SIZE_INC", maxDocsSetSizeInc);
        //maxSplitLevel = conf.getInt("MAX_SPLIT_LEVEL", maxSplitLevel);

        process(key, context, values, initialMaxDocsSetSize);

        log.info("time [msec]: " + (new Date().getTime() - startTime));

    }

    //******************** PRIVATE ********************
    /**
     * Processes the given documents: finds duplicates and saves them to the
     * context under the same, common key. If the number of the passed documents
     * is greater than the <b>maxNumberOfDocuments</b> then the documents are
     * split into smaller parts and then the method is recursively invoked for
     * each one of those parts.
     *
     * @param key a common key of the documents
     * @param level the recursive depth of the method used to generate a proper
     * key of the passed documents. The greater the level the longer (and more
     * unique) the generated key.
     */
    void process(Text key, Reducer<Text, BytesWritable, Text, Text>.Context context, Iterable<BytesWritable> documents, int maxNumberOfDocuments) throws IOException, InterruptedException {

        Iterator<BytesWritable> docsIterator = documents.iterator();
        log.info("-- start process, key: {}", key.toString());
        
        int partNb = 1;
        while (docsIterator.hasNext()) {
            int counter = 0;
            List<DocumentProtos.DocumentMetadata> metadataList = new ArrayList<DocumentProtos.DocumentMetadata>();
            while (counter < maxNumberOfDocuments && docsIterator.hasNext()) {
                DocumentProtos.DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(docsIterator.next().copyBytes());
                metadataList.add(docWrapper.getDocumentMetadata());
                counter++;
            }
            if (counter > 0) {
                log.info("---- key " + key.toString() + ", part "+ partNb + ", documents {} " + counter);
                log.info(" **************** first: " + metadataList.get(0).getKey());
                log.info(" **************** last: " + metadataList.get(metadataList.size() - 1).getKey());
                if (isDebugMode(context.getConfiguration())) {
                    duplicateWorkService.findDuplicates(metadataList, context);
                } else {
                    Map<Integer, Set<DocumentProtos.DocumentMetadata>> duplicateWorksMap = duplicateWorkService.findDuplicates(metadataList, null);
                    saveDuplicatesToContext(duplicateWorksMap, key, context);
                }
                context.progress();
            }
            partNb++;
        }

        log.info("-- end process, key: {}", key);
    }

    private boolean isDebugMode(Configuration conf) {
        if (conf == null) {
            return false;
        }
        String debugOptionValue = conf.get("DEDUPLICATION_DEBUG_MODE", "false");
        return debugOptionValue.equals("true");
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

    static enum UnparseableIssue {

        UNPARSEABLE
    };
}

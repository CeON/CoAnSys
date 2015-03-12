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

package pl.edu.icm.coansys.classification.documents.jobs;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.commons.java.PorterStemmer;
import pl.edu.icm.coansys.commons.java.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;
import pl.edu.icm.coansys.models.constants.HBaseConstant;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordCountMapper_Proto extends TableMapper<TextArrayWritable, IntWritable> {

	private static Logger logger = LoggerFactory.getLogger(WordCountMapper_Proto.class);
	private final static IntWritable one = new IntWritable(1);
	private Text key = null;
    

    @Override
    /**
     * (IN) accepts key-value pairs containing K:rowId, V:protocol buffer message with document metadata
     * (PROCESS) takes meaningful text parts from a document metadata concatenates them and stem
     * (OUT) emits key-value pairs containing K:docId + word from this document, V: value 1
     */
    protected void map(ImmutableBytesWritable rowId, Result documentMetadataColumn, Context context) throws IOException, InterruptedException { 
        
        DocumentMetadata dm = DocumentMetadata.parseFrom(documentMetadataColumn.
        		getValue(Bytes.toBytes(HBaseConstant.FAMILY_METADATA), 
        				 Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO)));

        key = new Text(dm.getKey());
        
        StringBuilder inputDataStringBuilder = new StringBuilder();
        for(TextWithLanguage docAbstract : dm.getDocumentAbstractList()) {
            inputDataStringBuilder.append(docAbstract.getText()).append(" ");
        }
        for (KeywordsList keywordsList : dm.getKeywordsList()) {
            inputDataStringBuilder.append(Joiner.on(" ").join(keywordsList.getKeywordsList())).append(" ");
        }
        
        List<String> titles = new ArrayList<String>();
        for (TextWithLanguage title : dm.getBasicMetadata().getTitleList()) {
            titles.add(title.getText());
        }
        inputDataStringBuilder.append(Joiner.on(" ").join(titles));
        
        PorterStemmer stemmer = new PorterStemmer();
        for(String string : DiacriticsRemover.removeDiacritics(inputDataStringBuilder.toString())
        											.toLowerCase().split(" ")){ 
        	stemmer.add(string.toCharArray(), 0);
        	stemmer.stem();

        	context.write(new TextArrayWritable(new Text[]{key, new Text(stemmer.toString())}),
        			one);
        }
    }
    
    
	protected void logAllFeaturesExtractedForOneAuthor(String authId,
			TextTextArrayMapWritable featureName2FeatureValuesMap) {
		logger.debug("MAPPER: output key: " + authId);
		logger.debug("MAPPER: output value: "+featureName2FeatureValuesMap);
	}
}

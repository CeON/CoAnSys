/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.classification.documents.auxil.PorterStemmer;
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;
import pl.edu.icm.coansys.disambiguation.auxil.TextTextArrayMapWritable;
import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordCountMapper_Proto extends TableMapper<TextArrayWritable, IntWritable> {

	private static Logger logger = LoggerFactory.getLogger(LoggingInDisambiguation.class);
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
        inputDataStringBuilder.append(Joiner.on(" ").join(dm.getKeywordList())).append(" ");
        
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

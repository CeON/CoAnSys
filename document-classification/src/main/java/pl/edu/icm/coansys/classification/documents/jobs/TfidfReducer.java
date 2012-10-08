/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;
import pl.edu.icm.coansys.disambiguation.features.FeatureInfo;

/**
 * 
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class TfidfReducer extends Reducer<Text, StringListIntListWritable, TextArrayWritable, DoubleWritable> {

	private static Logger logger = Logger.getLogger(LoggingInDisambiguation.class);
	protected String reducerId = new Date().getTime() + "_" + new Random().nextFloat();
	private int docs_num=1;
    
    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        logger.setLevel(Level.DEBUG);
        
        Configuration conf = context.getConfiguration();
        docs_num = Integer.parseInt(conf.get("DOCS_NUM"));
    }

    @Override
    /**
     * (IN) accepts key-value pairs containing K:word, V: docId + number of word occurrences in the document + no. of all words in doc
     * (OUT) emit key-value pairs containing K:docId + word, V: tfidf  
     */
    public void reduce(Text key, Iterable<StringListIntListWritable> values, Context context) {
    	
    	int docsWithTerm = 0;
        for (Iterator it = values.iterator(); it.hasNext();) {
            docsWithTerm++;
        }
        double idf = Math.log(docs_num/(double)docsWithTerm);
    	
    	for(final StringListIntListWritable v : values){
    		double tf = (double)(v.getIntList().get(0))/(double)(v.getIntList().get(1));
    		try {
				context.write(new TextArrayWritable(new Text[]{new Text(v.getStringList().get(0)),
																		key}),
							new DoubleWritable(tf*idf));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}

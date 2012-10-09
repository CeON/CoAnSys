/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.jobs;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.icm.coansys.classification.documents.auxil.StringListIntListWritable;
import pl.edu.icm.coansys.disambiguation.auxil.LoggingInDisambiguation;
import pl.edu.icm.coansys.disambiguation.auxil.TextArrayWritable;

/**
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class WordPerDocCountReducer extends Reducer<Text, StringListIntListWritable, TextArrayWritable, StringListIntListWritable> {

    private static Logger logger = Logger.getLogger(LoggingInDisambiguation.class);
    //protected String reducerId = new Date().getTime() + "_" + new Random().nextFloat();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        logger.setLevel(Level.DEBUG);
    }

    @Override
    /**
     * (IN) accepts key-value pairs containing K:docId, V: word + its number of
     * occurrences in the document (OUT) emit key-value pairs containing K:docId
     * + word, V: the number of word occurrences in the document + no. of all
     * words in doc
     */
    public void reduce(Text key, Iterable<StringListIntListWritable> values, Context context) {


        int dwc = 0;
        for (StringListIntListWritable slilw : values) {
            dwc += slilw.getIntList().get(0);
        }

        for (StringListIntListWritable slilw : values) {
            StringListIntListWritable out_slilw = new StringListIntListWritable();
            out_slilw.addInt(slilw.getIntList().get(0));
            out_slilw.addInt(dwc);
            try {
                context.write(
                        new TextArrayWritable(new Text[]{key,
                            new Text(slilw.getStringList().get(0))}),
                        out_slilw);
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

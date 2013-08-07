/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;

/**
 *
 * @author pdendek
 */
@SuppressWarnings("rawtypes")
public class REPLACE_FIELD extends EvalFunc<Map> {

    private static final Logger logger = LoggerFactory.getLogger(REPLACE_FIELD.class);
    
    @Override
    public Map exec(Tuple input) throws IOException {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) input.get(0);
            DataBag db = (DataBag) input.get(1);
            String fieldName = (String) input.get(2);

            map.put(fieldName, db);

            return map;

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

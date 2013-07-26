/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author pdendek
 */
public class EXTRACT_KEY_TI_ABS_KW_CATEG_WHEN_CATEG extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_KEY_TI_ABS_KW_CATEG_WHEN_CATEG.class);
    
    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.CHARARRAY, DataType.CHARARRAY, DataType.BAG, DataType.INTEGER);
        } catch (FrontendException e) {
            logger.error("Error in creating output schema:", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Tuple exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            Object obj = input.get(1);
            if(!(obj instanceof DataByteArray)){
            	logger.error("Error in casting Object (" + input.getType(1) + ") to DataByteArray");
              	return null;
            }
            DataByteArray dba = (DataByteArray) obj;

            DocumentMetadata dm = DocumentMetadata.parseFrom(dba.get());
          
            String key = dm.getKey();

            DataBag db = new DefaultDataBag();
            int bagsize = 0;
            for (ClassifCode code : dm.getBasicMetadata().getClassifCodeList()) {
                for (String co_str : code.getValueList()) {
                    bagsize++;
                    db.add(TupleFactory.getInstance().newTuple(co_str));
                }
            }
            
            if (bagsize > 0) {
                
                String titles;
                String abstracts;

                List<String> titleList = new ArrayList<String>();
                for (TextWithLanguage title : dm.getBasicMetadata().getTitleList()) {
                    titleList.add(title.getText());
                }
                titles = Joiner.on(" ").join(titleList);

                List<String> abstractsList = new ArrayList<String>();
                for (TextWithLanguage documentAbstract : dm.getBasicMetadata().getTitleList()) {
                    abstractsList.add(documentAbstract.getText());
                }
                abstracts = Joiner.on(" ").join(abstractsList);

                List<String> allKeywords = new ArrayList<String>();
                for (KeywordsList keywordsList : dm.getKeywordsList()) {
                    allKeywords.addAll(keywordsList.getKeywordsList());
                }
                Object[] to = new Object[]{key, titles, abstracts, Joiner.on(" ").join(allKeywords), db, bagsize};
                
                return TupleFactory.getInstance().newTuple(Arrays.asList(to));
            }
            return null;

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import org.apache.pig.backend.executionengine.ExecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author pdendek
 */
public class EXTRACT_KEY_TI_ABS_KW extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_KEY_TI_ABS_KW.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE, DataType.CHARARRAY,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.CHARARRAY);
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
            Object obj = null;
            try {
                obj = (DataByteArray) input.get(1);
            } catch (ExecException e) {
                logger.error("Error in reading field rowId:", e);
                throw e;
            }

            DataByteArray dba = null;
            try {
                dba = (DataByteArray) obj;
            } catch (Exception e) {
                logger.error("Error in casting Object (" + input.getType(1) + ") to DataByteArray:", e);
                throw e;
            }

            DocumentMetadata dm = null;
            try {
                dm = DocumentMetadata.parseFrom(dba.get());
            } catch (Exception e) {
                logger.error("Error in reading ByteArray to DocumentMetadata:", e);
                throw e;
            }

            String key = dm.getKey();

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
            String[] to = new String[]{key, titles, abstracts, Joiner.on(" ").join(allKeywords)};

            Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));

            return t;

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.Arrays;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.constants.ProtoConstants;

/**
 *
 * @author pdendek
 */
public class EXTRACT_KEY_MSCCATEG extends EvalFunc<Tuple> {

    private static final Logger logger = LoggerFactory.getLogger(EXTRACT_KEY_MSCCATEG.class);

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.BAG);
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
            Object obj = (DataByteArray) input.get(1);

            DataByteArray dba = (DataByteArray) obj;

            DocumentMetadata dm = DocumentMetadata.parseFrom(dba.get());

            String key = dm.getKey();
            DataBag db = new DefaultDataBag();

            for (ClassifCode code : dm.getBasicMetadata().getClassifCodeList()) {
                if (ProtoConstants.documentClassifCodeMsc.equals(code.getSource())) {
                    db.add(TupleFactory.getInstance().newTuple(code.getValueList()));
                }
            }
            Object[] to = new Object[]{key, db};
            return TupleFactory.getInstance().newTuple(Arrays.asList(to));

        } catch (Exception e) {
            logger.error("Error in processing input row:", e);
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

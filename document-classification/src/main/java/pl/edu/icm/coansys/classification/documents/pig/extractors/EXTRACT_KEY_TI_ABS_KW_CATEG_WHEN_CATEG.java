/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.Arrays;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author pdendek
 */
public class EXTRACT_KEY_TI_ABS_KW_CATEG_WHEN_CATEG extends EvalFunc<Tuple> {

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.CHARARRAY, DataType.CHARARRAY, DataType.CHARARRAY, DataType.CHARARRAY, DataType.BAG, DataType.INTEGER);
        } catch (FrontendException e) {
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
            } catch (Exception e) {
                System.out.println("Trying to read field rowId");
                System.out.println("Failure!");
                e.printStackTrace();
                throw e;
            }

            DataByteArray dba = null;
            try {
                dba = (DataByteArray) obj;
            } catch (Exception e) {
                System.out.println("Trying to cast Object (" + input.getType(1) + ") to DataByteArray");
                System.out.println("Failure!");
                e.printStackTrace();
                throw e;
            }

            DocumentMetadata dm = null;
            try {
                dm = DocumentMetadata.parseFrom(dba.get());
            } catch (Exception e) {
                System.out.println("Trying to read ByteArray to DocumentMetadata");
                System.out.println("Failure!");
                e.printStackTrace();
                throw e;
            }

            String key = dm.getKey();

            DataBag db = new DefaultDataBag();
            int bagsize = 0;
//	        System.out.print(key+":");
            for (ClassifCode code : dm.getClassifCodeList()) {
                for (String co_str : code.getValueList()) {
                    bagsize++;
//	        		System.out.print(" "+co_str);
                    db.add(TupleFactory.getInstance().newTuple(co_str));
                }
            }
//	        System.out.println("");
            if (bagsize > 0) {
                Object[] to = new Object[]{key, dm.getTitle(), dm.getAbstrakt(), Joiner.on(" ").join(dm.getKeywordList()), db, bagsize};
                Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
                return t;
            }
            return null;

        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

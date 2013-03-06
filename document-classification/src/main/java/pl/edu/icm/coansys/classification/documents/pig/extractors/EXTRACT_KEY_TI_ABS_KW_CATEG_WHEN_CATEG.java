/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.classification.documents.auxil.StackTraceExtractor;
import pl.edu.icm.coansys.importers.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import pl.edu.icm.coansys.importers.models.DocumentProtos.TextWithLanguage;

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
            
            obj = input.get(1);
            if(!(obj instanceof DataByteArray)){
            	System.out.println("Trying to cast Object (" + input.getType(1) + ") to DataByteArray");
                System.out.println("Failure!");
            	return null;
            }
            DataByteArray dba = (DataByteArray) obj;


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
            for (ClassifCode code : dm.getBasicMetadata().getClassifCodeList())
                for (String co_str : code.getValueList()) {
                    bagsize++;
                    db.add(TupleFactory.getInstance().newTuple(co_str));
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
                
                Object[] to = new Object[]{key, titles, abstracts, Joiner.on(" ").join(dm.getKeywordList()), db, bagsize};
                Tuple t = TupleFactory.getInstance().newTuple(Arrays.asList(to));
                return t;
            }
            return null;

        } catch (Exception e) {
            // Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
                    + StackTraceExtractor.getStackTrace(e));
        }
    }
}

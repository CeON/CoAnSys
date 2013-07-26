/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.classification.documents.pig.extractors;

import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.*;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.KeywordsList;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;

/**
 *
 * @author pdendek
 */
@SuppressWarnings("rawtypes")
public class EXTRACT_MAP_CATEGOCC extends EvalFunc<Tuple> {

    @Override
    public Schema outputSchema(Schema p_input) {
        try {
            return Schema.generateNestedSchema(DataType.TUPLE,
                    DataType.MAP, DataType.LONG);
        } catch (FrontendException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Tuple exec(Tuple input) throws IOException {
        try {
            DataByteArray protoMetadata = (DataByteArray) input.get(0);
            DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMetadata.get());
            
            String titles;
            String abstracts;

            List<String> titleList = new ArrayList<String>();
            for (TextWithLanguage title : metadata.getBasicMetadata().getTitleList()) {
                titleList.add(title.getText());
            }
            titles = Joiner.on(" ").join(titleList);

            List<String> abstractsList = new ArrayList<String>();
            for (TextWithLanguage documentAbstract : metadata.getBasicMetadata().getTitleList()) {
                abstractsList.add(documentAbstract.getText());
            }
            abstracts = Joiner.on(" ").join(abstractsList);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", metadata.getKey());
            map.put("title", titles);
            map.put("keywords", getConcatenated(metadata.getKeywordsList()));
            map.put("abstract", abstracts);
            DataBag db = getCategories(metadata.getBasicMetadata().getClassifCodeList());
            map.put("categories", db);
            long num = db.size();

            Object[] to = new Object[]{map, num};
            return TupleFactory.getInstance().newTuple(Arrays.asList(to));
        } catch (Exception e) {
        	// Throwing an exception will cause the task to fail.
            throw new IOException("Caught exception processing input row:\n"
            		+ StackTraceExtractor.getStackTrace(e));
        }
    }

    private DataBag getCategories(List<ClassifCode> classifCodeList) {
        DataBag db = new DefaultDataBag();
        for (ClassifCode code : classifCodeList) {
            for (String co_str : code.getValueList()) {
//       		System.out.print(" "+co_str);
                db.add(TupleFactory.getInstance().newTuple(co_str));
            }
        }
        return db;
    }

    private String getConcatenated(List<KeywordsList> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<String> allKeywords = new ArrayList<String>();
        if (allKeywords.isEmpty()) {
            return null;
        } else {
            return Joiner.on(" ").join(allKeywords);
        }
    }
}

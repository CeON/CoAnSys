/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import pl.edu.icm.coansys.commons.java.PorterStemmer;
import pl.edu.icm.coansys.commons.java.StopWordsRemover;
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;

public class StemmedPairs extends EvalFunc<DataBag> {

    @Override
    public Schema outputSchema(Schema input) {
        try {

            Schema termSchema = new Schema(new Schema.FieldSchema("term",
                    new Schema(new Schema.FieldSchema("value", DataType.CHARARRAY)),
                    DataType.TUPLE));

            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),
                    termSchema, DataType.BAG));
        } catch (Exception e) {
            return null;
        }
    }
    private final String SPACE = " ";

    public List<String> getStemmedPairs(final String text) throws IOException {
        String tmp  = text.toLowerCase();
        tmp = DiacriticsRemover.removeDiacritics(tmp);
        tmp = tmp.replaceAll("_", SPACE);
        tmp = tmp.replaceAll("\n", SPACE);
        tmp = tmp.replaceAll("[^a-z\\d-_/ ]", "");

        List<String> strings = new ArrayList<String>();
        PorterStemmer ps = new PorterStemmer();
        for (String s : StringUtils.split(tmp, SPACE)) {
            if (!StopWordsRemover.isAnEnglishStopWords(s)) {;
                ps.add(s.toCharArray(), s.length());
                ps.stem();
                strings.add(ps.toString());
            }
        }

        return strings;
    }

    @Override
    public DataBag exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0 || input.get(0) == null) {
            return null;
        }

        try {
            List<Tuple> tuples = new ArrayList<Tuple>();

            String terms = (String) input.get(0);
            for (String s : getStemmedPairs(terms)) {
                tuples.add(TupleFactory.getInstance().newTuple(s));
            }

            return new DefaultDataBag(tuples);
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

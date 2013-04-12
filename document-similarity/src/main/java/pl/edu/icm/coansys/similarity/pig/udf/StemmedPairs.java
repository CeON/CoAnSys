/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;
import pl.edu.icm.coansys.similarity.documents.auxil.PorterStemmer;
import pl.edu.icm.coansys.similarity.documents.auxil.StopWordsRemover;

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

    public List<String> getStemmedPairs(String text) throws IOException {
        text = text.toLowerCase();
        text = DiacriticsRemover.removeDiacritics(text);
        text = text.replaceAll("_", SPACE);
        text = text.replaceAll("\n", SPACE);
        text = text.replaceAll("[^a-z\\d-_/ ]", "");

        List<String> strings = new ArrayList<String>();
        PorterStemmer ps = new PorterStemmer();
        for (String s : StringUtils.split(text, SPACE)) {
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

            DataBag bd = new DefaultDataBag(tuples);
            return bd;
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.similarity.documents.auxil.PorterStemmer;
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;
import pl.edu.icm.coansys.similarity.documents.auxil.StopWordsRemover;

public class StemmedPairs extends EvalFunc<DataBag> {

    private final String SPACE = " ";

    public List<String[]> getStemmedPairs(String text) throws IOException {
        text = text.toLowerCase();
        text = DiacriticsRemover.removeDiacritics(text);
        text = text.replaceAll("_", SPACE);
        text = text.replaceAll("\n", SPACE);
        text = text.replaceAll("[^a-z\\d-_/ ]", "");

        ArrayList<String[]> strings = new ArrayList<String[]>();
        PorterStemmer ps = new PorterStemmer();
        for (String s : StringUtils.split(text, SPACE)) {
            if (!StopWordsRemover.isAnEnglishStopWords(s)) {;
                ps.add(s.toCharArray(), s.length());
                ps.stem();
                String[] oneFieldTupleArray = new String[]{ps.toString()};
                if (oneFieldTupleArray[0].length() > 1) {
                    //String[] oneFieldTupleArray = new String[]{s};
                    strings.add(oneFieldTupleArray);
                }
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
            ArrayList<Tuple> tuples = new ArrayList<Tuple>();

            String terms = (String) input.get(0);
            for (String[] s : getStemmedPairs(terms)) {
                tuples.add(TupleFactory.getInstance().newTuple(Arrays.asList(s)));
            }

            DataBag bd = new DefaultDataBag(tuples);
            return bd;
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

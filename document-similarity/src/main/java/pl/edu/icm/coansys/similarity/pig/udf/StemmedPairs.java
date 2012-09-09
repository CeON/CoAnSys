/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.similarity.pig.udf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import pl.edu.icm.coansys.similarity.documents.auxil.PorterStemmer;
import pl.edu.icm.coansys.disambiguation.auxil.DiacriticsRemover;

public class StemmedPairs extends EvalFunc<DataBag> {

    @Override
    public DataBag exec(Tuple input) throws IOException {
        if (input == null || input.size() == 0) {
            return null;
        }
        try {
            String terms = (String) input.get(0);

            terms = terms.toLowerCase();
            terms = DiacriticsRemover.removeDiacritics(terms);
            terms = terms.replaceAll("[^a-z ]", "");

            PorterStemmer ps = new PorterStemmer();
            ArrayList<Tuple> strings = new ArrayList<Tuple>();
            for (String s : terms.split(" ")) {
                ps.add(s.toCharArray(), s.length());
                ps.stem();
                String[] oneFieldTupleArray = new String[]{ps.toString()};
                strings.add(TupleFactory.getInstance().newTuple(Arrays.asList(oneFieldTupleArray)));
            }

            DataBag bd = new DefaultDataBag(strings);
            return bd;
        } catch (Exception e) {
            throw new IOException("Caught exception processing input row ", e);
        }
    }
}

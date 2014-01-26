/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
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
import pl.edu.icm.coansys.commons.java.StackTraceExtractor;
import pl.edu.icm.coansys.commons.java.StopWordsRemover;
import pl.edu.icm.coansys.commons.java.DiacriticsRemover;

public class ExtendedStemmedPairs extends EvalFunc<DataBag> {

    @Override
    public Schema outputSchema(Schema input) {
        try {
            Schema termSchema = new Schema(new Schema.FieldSchema("term",
                    new Schema(new Schema.FieldSchema("value", DataType.CHARARRAY)),
                    DataType.TUPLE));

            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),
                    termSchema, DataType.BAG));
        } catch (Exception e) {
        	log.error("Error in the output Schema creation",e);
        	log.error(StackTraceExtractor.getStackTrace(e));
            return null;
        }
    }
    private static final String SPACE = " ";

    public List<String> getStemmedPairs(final String text) throws IOException {
        String tmp  = text.toLowerCase();
        tmp = DiacriticsRemover.removeDiacritics(tmp);
        tmp = tmp.replaceAll("[_]+", SPACE);
        tmp = tmp.replaceAll("[-]+", "-");
        tmp = tmp.replaceAll("\\s+", SPACE);
        tmp = tmp.replaceAll("[^a-z\\d-_/ ]+", "");
        tmp = tmp.trim();
        List<String> strings = new ArrayList<String>();
        
        if(tmp.length()==0){
        	return strings;
        }
        
        PorterStemmer ps = new PorterStemmer();
        for (String s : StringUtils.split(tmp, SPACE)) {
        	s = s.replaceAll("^[-]+", "");
        	s = s.replaceAll("[-]+$", "");
        	s = s.replaceAll("^[0-9]+$", "");
        	if(s.length()==0){
        		continue;
        	}
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

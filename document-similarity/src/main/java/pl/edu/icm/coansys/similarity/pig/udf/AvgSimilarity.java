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

/**
 *
 * @author akawa
 */
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.schema.Schema;

import pl.edu.icm.coansys.similarity.documents.function.AvgSimilarityFunction;
import pl.edu.icm.coansys.similarity.documents.function.SimilarityFunction;

public class AvgSimilarity extends EvalFunc<Double> {

    private static final String DOCUMENTS_KEYWORD_SIMILARITY = "dks";
    private static final String DOCUMENTS_KEYWORDS_COMBINED_SIMILARITY = "dkcs";
    private String type = null;
    private static final SimilarityFunction simFunct = new AvgSimilarityFunction();

    public AvgSimilarity() {
    }

    public AvgSimilarity(String type) {
        this.type = type;
    }

    /*
     * Takes input as <keyword, doc1key, doc1kwW, doc2key, doc2kwW>;
     * and produces the similarity for documents: doc1key and doc2key
     * based on single keyword
     */
    private Double getDocumentsKeywordSimilarity(Tuple input) throws ExecException {
            String keyword = (String) input.get(0);
            String doc1Key = (String) input.get(1);
            double doc1KeywordWeight = (Double) input.get(2);
            String doc2Key = (String) input.get(3);
            double doc2KeywordWeight = (Double) input.get(4);

            return simFunct.getDocumentsKeywordSimilarity(keyword, doc1Key, doc1KeywordWeight, doc2Key, doc2KeywordWeight);
       
    }

    /*
     * Takes input as a bag of <similarity> and produces the combined similarity.
     */
    private Double getDocumentsKeywordsCombinedSimilarity(Tuple input) throws ExecException {
            DataBag bag1 = (DataBag) input.get(0);
            String doc1Key = (String) bag1.iterator().next().get(0);
            DataBag bag2 = (DataBag) input.get(1);
            String doc2Key = (String) bag2.iterator().next().get(0);

            DataBag bag = (DataBag) input.get(2);
            Iterator<Tuple> iterator = bag.iterator();
            List<Double> list = new LinkedList<Double>();
            while (iterator.hasNext()) {
                Tuple tuple = iterator.next();
                double similarity = (Double) tuple.get(0);
                list.add(similarity);
            }

            return simFunct.getDocumentsTotalSimilarity(doc1Key, doc2Key, list);
    }

    @Override
    public Double exec(Tuple input) throws IOException {
        if (type.equals(DOCUMENTS_KEYWORD_SIMILARITY)) {
            return getDocumentsKeywordSimilarity(input);
        } else if (type.equals(DOCUMENTS_KEYWORDS_COMBINED_SIMILARITY)) {
            return getDocumentsKeywordsCombinedSimilarity(input);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
    
    @Override
    public Schema outputSchema(Schema input) {
        try{
            Schema doubleSchema = new Schema();
            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),doubleSchema, DataType.DOUBLE));
        }catch (Exception e){
                return null;
        }
    }
}
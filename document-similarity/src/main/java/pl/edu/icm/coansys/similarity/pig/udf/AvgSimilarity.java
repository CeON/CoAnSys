/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    private Double getDocumentsKeywordSimilarity(Tuple input) {
        try {
            String keyword = (String) input.get(0);
            String doc1Key = (String) input.get(1);
            double doc1KeywordWeight = (Double) input.get(2);
            String doc2Key = (String) input.get(3);
            double doc2KeywordWeight = (Double) input.get(4);

            return simFunct.getDocumentsKeywordSimilarity(keyword, doc1Key, doc1KeywordWeight, doc2Key, doc2KeywordWeight);
        } catch (ExecException ex) {
            throw new RuntimeException("Error while calculation of getDocumentsKeywordSimilarity", ex);
        }
    }

    /*
     * Takes input as a bag of <similarity> and produces the combined similarity.
     */
    private Double getDocumentsKeywordsCombinedSimilarity(Tuple input) {
        try {
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

            Double totalSimilarity = simFunct.getDocumentsTotalSimilarity(doc1Key, doc2Key, list);
            return totalSimilarity;

        } catch (ExecException ex) {
            throw new RuntimeException("Error while calculation of getDocumentsKeywordsCombinedSimilarity", ex);
        }
    }

    @Override
    public Double exec(Tuple input) throws IOException {
        if (type.equals(DOCUMENTS_KEYWORD_SIMILARITY)) {
            return getDocumentsKeywordSimilarity(input);
        } else if (type.equals(DOCUMENTS_KEYWORDS_COMBINED_SIMILARITY)) {
            return getDocumentsKeywordsCombinedSimilarity(input);
        }
        throw new RuntimeException("Unsupported type: " + type);
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
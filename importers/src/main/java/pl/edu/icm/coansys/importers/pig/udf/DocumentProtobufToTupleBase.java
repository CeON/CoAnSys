/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.Bag;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.importers.models.DocumentProtos.MediaContainer;

/**
 *
 * @author akawa
 */
public abstract class DocumentProtobufToTupleBase extends EvalFunc<Tuple> {

    Map<String, Integer> fieldNumberMap = new HashMap<String, Integer>() {
        {
            put("key", 0);
            put("title", 1);
            put("abstract", 2);
            put("keywords", 3);
            put("contributors", 4);
        }
    };

    @Override
    public Schema outputSchema(Schema input) {
        try {

            Schema keywordSchema = new Schema(new Schema.FieldSchema("keyword",
                    new Schema(new Schema.FieldSchema("value", DataType.CHARARRAY)),
                    DataType.TUPLE));
            Schema contributorSchema = new Schema(new Schema.FieldSchema("contributor",
                    new Schema(Arrays.asList(
                    new Schema.FieldSchema("key", DataType.CHARARRAY),
                    new Schema.FieldSchema("name", DataType.CHARARRAY))),
                    DataType.TUPLE));

            Schema tupleSchema = new Schema();
            tupleSchema.add(new Schema.FieldSchema("key", DataType.CHARARRAY));
            tupleSchema.add(new Schema.FieldSchema("title", DataType.CHARARRAY));
            tupleSchema.add(new Schema.FieldSchema("abstract", DataType.CHARARRAY));
            tupleSchema.add(new Schema.FieldSchema("keywords", keywordSchema, DataType.BAG));
            tupleSchema.add(new Schema.FieldSchema("contributors", contributorSchema, DataType.BAG));

            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input),
                    tupleSchema, DataType.TUPLE));
        } catch (Exception e) {
            return null;
        }
    }

    private <T> DataBag listToDataBag(List<T> list) {
        DataBag output = BagFactory.getInstance().newDefaultBag();
        for (T l : list) {
            output.add(TupleFactory.getInstance().newTuple(l));
        }

        return output;

    }

    private <T1, T2> DataBag listToDataBag(List<T1> list1, List<T2> list2) throws ExecException {
        DataBag output = BagFactory.getInstance().newDefaultBag();
        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
            Tuple t = TupleFactory.getInstance().newTuple(2);
            t.set(0, list1.get(i));
            t.set(1, list2.get(i));
            output.add(t);
        }
        return output;
    }

    private Tuple addDocumentMetatdataFields(DocumentMetadata metadata, Tuple output) throws ExecException {

        output.set(fieldNumberMap.get("key"), metadata.getKey());
        output.set(fieldNumberMap.get("title"), metadata.getTitle());
        output.set(fieldNumberMap.get("abstract"), metadata.getAbstrakt());
        output.set(fieldNumberMap.get("keywords"), listToDataBag(metadata.getKeywordList()));

        List<String> authorKyes = new ArrayList<String>();
        List<String> authorNames = new ArrayList<String>();
        for (Author author : metadata.getAuthorList()) {
            authorKyes.add(author.getKey());
            authorNames.add(author.getName());
        }

        output.set(fieldNumberMap.get("contributors"), listToDataBag(authorKyes, authorNames));

        return output;
    }

    public abstract DocumentMetadata getDocumentMetadata(Tuple input) throws ExecException, InvalidProtocolBufferException;

    public abstract MediaContainer getDocumentMedia(Tuple input) throws ExecException, InvalidProtocolBufferException;

    @Override
    public Tuple exec(Tuple input) throws IOException {

        Tuple output = TupleFactory.getInstance().newTuple(fieldNumberMap.size());
        DocumentMetadata metadata = getDocumentMetadata(input);
        output = addDocumentMetatdataFields(metadata, output);
        if (input.size() > 1) {
//            MediaContainer media = getDocumentMedia(input);
        }
        return output;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author akawa
 */
public class DocumentProtobufBytesToTuple extends EvalFunc<Map> {

    private <T> DataBag getBag(List<T> items) throws ExecException {
        DataBag bag = null;
        if (items != null) {
            bag = BagFactory.getInstance().newDefaultBag();
            for (T item : items) {
                Tuple tuple = TupleFactory.getInstance().newTuple(2);
                tuple.set(0, item);
                bag.add(tuple);
            }
        }
        return bag;
    }

    @Override
    public Map exec(Tuple input) throws IOException {
        try {
            DataByteArray protoMessage = (DataByteArray) input.get(0);

            DocumentMetadata metadata = DocumentMetadata.parseFrom(protoMessage.get());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key", metadata.getKey());
            map.put("title", metadata.getTitle());
            map.put("keywordList", getBag(metadata.getKeywordList()));

            return map;

        } catch (Exception e) {
            // Throwing an exception will cause the task to fail.
            throw new RuntimeException("Error while parsing DocumentMetadata", e);
        }
    }
}
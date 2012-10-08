/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper.DocumentWrapper;

/**
 *
 * @author akawa
 */
public class DocumentProtoToTuple extends EvalFunc<Tuple> {

    @Override
    public Tuple exec(Tuple input) throws IOException {
        DataByteArray documentProto = (DataByteArray) input.get(0);
        DocumentWrapper document = DocumentWrapper.parseFrom(documentProto.get());
        Tuple output = TupleFactory.getInstance().newTuple(3);
        output.set(0, document.getRowId());
        output.set(1, document.getMproto());
        output.set(2, document.getCproto());
        return output;
    }

    @Override
    public Schema outputSchema(Schema input) {
        try {
            Schema tupleSchema = new Schema();
            tupleSchema.add(input.getField(0));
            tupleSchema.add(input.getField(1));
            tupleSchema.add(input.getField(2));
            return new Schema(new Schema.FieldSchema(getSchemaName(this.getClass().getName().toLowerCase(), input), tupleSchema, DataType.TUPLE));
        } catch (Exception e) {
            return null;
        }
    }
}
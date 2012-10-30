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
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author akawa
 */
public class DocumentProtoPartsTupler extends EvalFunc<Tuple> {
    
    private DataByteArray rowId = new DataByteArray();
    private DataByteArray mproto = new DataByteArray();
    private DataByteArray cproto = new DataByteArray();
    private Tuple output = TupleFactory.getInstance().newTuple(3);

    @Override
    public Tuple exec(Tuple input) throws IOException {
        byte[] documentProto = (byte[]) input.get(0);
        DocumentWrapper document = DocumentWrapper.parseFrom(documentProto);
        
        rowId.set(document.getRowId());
        mproto.set(document.getDocumentMetadata().toByteArray());
        cproto.set(document.getMediaContainer().toByteArray());
        
        output.set(0, rowId);
        output.set(1, mproto);
        output.set(2, cproto);
        
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
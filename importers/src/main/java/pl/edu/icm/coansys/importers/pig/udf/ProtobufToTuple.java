/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.transaction.NotSupportedException;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.*;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;

/**
 * Pig UDF converting protocol buffers messages to pig tuple.
 *
 * @author acz
 */
public class ProtobufToTuple extends EvalFunc<Tuple> {

    private Class protobufClass;
    private Schema schema;

    /**
     * This constructor cannot be called directly in pig latin scripts, but it
     * can be used in default constructor of a subclass.
     *
     * @param protobufClass
     */
    public ProtobufToTuple(Class protobufClass) {
        if (!Message.class.isAssignableFrom(protobufClass)) {
            throw new IllegalArgumentException("Argument must be a subclass of com.google.protobuf.Message");
        }
        this.protobufClass = protobufClass;
    }

    /**
     * Constructor with a protobuf class name. It can be called directly from
     * pig latin scripts, i.e.: <p> define myUDF
     * pl.edu.icm.coansys.importers.pig.udf.ProtobufToTuple("protobufClassName");
     * <p> FOREACH data GENERATE myUDF($0);
     *
     * @param protobufClassName
     * @throws ClassNotFoundException
     */
    public ProtobufToTuple(String protobufClassName) throws ClassNotFoundException {
        this(Class.forName(protobufClassName));
    }

    private ProtobufToTuple() {
    }

    @Override
    public Schema outputSchema(Schema input) {
        if (schema == null) {
            schema = ProtobufToTuple.protobufToSchema(protobufClass, getSchemaName(this.getClass().getName().toLowerCase(), input));
        }
        return schema;
    }

    @Override
    public Tuple exec(Tuple input) throws IOException, ExecException {

        try {
            DataByteArray protoMetadata = (DataByteArray) input.get(0);
            Message metadata = (Message) protobufClass.getMethod("parseFrom", byte[].class).invoke(null, protoMetadata.get());

            return ProtobufToTuple.messageToTuple(metadata);
        } catch (Exception ex) {
            throw new ExecException(ex);
        }
    }

    /**
     * Generates the pig schema with one field of type TUPLE from a protocol
     * buffers message class
     *
     * @param messageClass class of a protocol buffer message
     * @param tupleName name for returned tuple
     * @return pig schema generated from protocol buffers message class
     */
    private static Schema protobufToSchema(Class messageClass, String tupleName) {
        Descriptor descr;
        try {
            descr = (Descriptor) messageClass.getMethod("getDescriptor").invoke(null);
            return new Schema(new FieldSchema(tupleName.toLowerCase(), protoDescriptorToSchema(descr), DataType.TUPLE));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Generates the pig schema from a protocol buffers message descriptor
     *
     * @param descr
     * @return
     */
    private static Schema protoDescriptorToSchema(Descriptor descr) {
        try {
            Schema schema = new Schema();

            for (FieldDescriptor fd : descr.getFields()) {
                if (fd.isRepeated()) {
                    Schema subSchema = new Schema();
                    addFieldToSchema(subSchema, fd);
                    /*
                     * BAG can contain only tuples. Wrap schema by a tuple if
                     * necessary...
                     */
                    if (subSchema.getField(0).type != DataType.TUPLE) {
                        subSchema = new Schema(new FieldSchema(fd.getName().toLowerCase(), subSchema, DataType.TUPLE));
                    }
                    schema.add(new FieldSchema(fd.getName().toLowerCase(), subSchema, DataType.BAG));
                } else {
                    addFieldToSchema(schema, fd);
                }
            }
            return schema;

        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Adds one field to pig schema generated from protobuf FieldDescriptor
     *
     * @param schema
     * @param fd
     * @throws FrontendException
     */
    private static void addFieldToSchema(Schema schema, FieldDescriptor fd) throws FrontendException, NotSupportedException {
        Type type = fd.getType();
        if (type.equals(Type.STRING)) {
            schema.add(new FieldSchema(fd.getName().toLowerCase(), DataType.CHARARRAY));
        } else if (type.equals(Type.INT32)) {
            schema.add(new FieldSchema(fd.getName().toLowerCase(), DataType.INTEGER));
        } else if (type.equals(Type.INT64)) {
            schema.add(new FieldSchema(fd.getName().toLowerCase(), DataType.LONG));
        } else if (type.equals(Type.MESSAGE)) {
            Schema messageSchema = protoDescriptorToSchema(fd.getMessageType());
            schema.add(new FieldSchema(fd.getName().toLowerCase(), messageSchema, DataType.TUPLE));
        } else {
            throw new NotSupportedException();
        }
    }

    /**
     * Converts a protocol buffers message object (deserialized) to pig Tuple
     *
     * @param message a protocol buffers message
     * @return pig tuple generated from protocol buffers message
     * @throws NoSuchMethodException
     * @throws IllegalAccessException @Override
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NotSupportedException
     * @throws ExecException
     */
    private static Tuple messageToTuple(MessageOrBuilder message) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NotSupportedException, ExecException {
        Descriptor descriptor = (Descriptor) message.getClass().getMethod("getDescriptor").invoke(null);
        int fieldsCount = descriptor.getFields().size();
        Tuple output = TupleFactory.getInstance().newTuple(fieldsCount);

        for (FieldDescriptor fd : message.getAllFields().keySet()) {
            Type type = fd.getType();
            if (fd.isRepeated()) {
                List l = (List) message.getField(fd);
                if (l.isEmpty()) {
                    continue;
                }
                DataBag db = BagFactory.getInstance().newDefaultBag();
                for (Object o : l) {
                    if (type.equals(Type.STRING) || type.equals(Type.INT32) || type.equals(Type.INT64)) {
                        Tuple subtuple = TupleFactory.getInstance().newTuple(o);
                        db.add(subtuple);
                    } else if (type.equals(Type.MESSAGE)) {
                        db.add(messageToTuple((Message) o));
                    }
                }
                output.set(fd.getIndex(), db);
            } else {
                if (type.equals(Type.STRING) || type.equals(Type.INT32) || type.equals(Type.INT64)) {
                    output.set(fd.getIndex(), message.getField(fd));
                } else if (type.equals(Type.MESSAGE)) {
                    Message m = (Message) message.getField(fd);
                    output.set(fd.getIndex(), messageToTuple(m));
                }
            }
        }
        return output;
    }
}

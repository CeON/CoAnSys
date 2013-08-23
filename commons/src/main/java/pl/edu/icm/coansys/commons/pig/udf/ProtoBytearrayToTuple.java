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
package pl.edu.icm.coansys.commons.pig.udf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.pig.EvalFunc;
import org.apache.pig.PigException;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;

/**
 * Pig UDF converting protocol buffers messages to pig tuple.
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl> 
 */
public class ProtoBytearrayToTuple extends EvalFunc<Tuple> {

    private Class<? extends Message> protobufClass;
    private Schema schema;
    
    /**
     * A map between protobuf and pig types
     */
    private static final Map<Type, Byte> typesMap = new EnumMap<Type, Byte>(Type.class);
    static {
        typesMap.put(Type.STRING, DataType.CHARARRAY);
        typesMap.put(Type.INT32, DataType.INTEGER);
        typesMap.put(Type.SINT32, DataType.INTEGER);
        typesMap.put(Type.UINT32, DataType.INTEGER);
        typesMap.put(Type.INT64, DataType.LONG);
        typesMap.put(Type.SINT64, DataType.LONG);
        typesMap.put(Type.UINT64, DataType.LONG);
        typesMap.put(Type.FLOAT, DataType.FLOAT);
        typesMap.put(Type.DOUBLE, DataType.DOUBLE);
        typesMap.put(Type.BOOL, DataType.BOOLEAN);
        typesMap.put(Type.ENUM, DataType.CHARARRAY);
        typesMap.put(Type.MESSAGE, DataType.TUPLE);
        typesMap.put(Type.BYTES, DataType.BYTEARRAY);
    }

    /**
     * This constructor cannot be called directly in pig latin scripts, but it
     * can be used in default constructor of a subclass.
     *
     * @param protobufClass a class of protocol buffers messages
     */
    public ProtoBytearrayToTuple(Class<? extends Message> protobufClass) {
        this.protobufClass = protobufClass;
    }

    /**
     * Constructor with a protobuf class name. It can be called directly from
     * pig latin scripts, i.e.: <p> define myUDF
     * pl.edu.icm.coansys.commons.pig.udf.ProtoBytearrayToTuple("protobufClassName");
     * <p> FOREACH data GENERATE myUDF($0);
     *
     * @param protobufClassName
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public ProtoBytearrayToTuple(String protobufClassName) throws ClassNotFoundException {
        this((Class<? extends Message>) Class.forName(protobufClassName));
    }

    /**
     * Returns a data schema to pig scripts
     * 
     * @param input
     * @return 
     */
    @Override
    public Schema outputSchema(Schema input) {
        if (schema == null) {
            String mainTupleName = getSchemaName(this.getClass().getName().toLowerCase(), input);
            schema = ProtoBytearrayToTuple.protobufToSchema(protobufClass, mainTupleName);
        }
        return schema;
    }

    @Override
    public Tuple exec(Tuple input) throws ExecException {
        try {
            DataByteArray protoMetadata = (DataByteArray) input.get(0);
            Method parseFromMethod = protobufClass.getMethod("parseFrom", byte[].class);
            Message metadata = (Message) parseFromMethod.invoke(null, protoMetadata.get());

            return ProtoBytearrayToTuple.messageToTuple(metadata);
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
    private static Schema protobufToSchema(Class<? extends Message> messageClass, String tupleName) {
        try {
            Descriptor descr = (Descriptor) messageClass.getMethod("getDescriptor").invoke(null);
            String fieldName = tupleName.toLowerCase(Locale.ENGLISH);
            return new Schema(new FieldSchema(fieldName, protoDescriptorToSchema(descr), DataType.TUPLE));
        } catch (IllegalAccessException ex) {
            return null;
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (InvocationTargetException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        } catch (PigException ex) {
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
                String fieldName = fd.getName().toLowerCase(Locale.ENGLISH);
                
                if (fd.isRepeated()) {
                    Schema subSchema = new Schema();
                    addFieldToSchema(subSchema, fd);
                    //BAG can contain only tuples. Wrap schema by a tuple if necessary
                    if (subSchema.getField(0).type != DataType.TUPLE) {
                        subSchema = new Schema(new FieldSchema(fieldName, subSchema, DataType.TUPLE));
                    }
                    schema.add(new FieldSchema(fieldName, subSchema, DataType.BAG));
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
    private static void addFieldToSchema(Schema schema, FieldDescriptor fd) throws FrontendException, 
            IllegalArgumentException {
        
        Type protobufType = fd.getType();
        String fieldName = fd.getName().toLowerCase(Locale.ENGLISH);
        Byte pigType;
        if (typesMap.containsKey(protobufType)) {
            pigType = typesMap.get(protobufType);
        } else {
            throw new IllegalArgumentException();
        }
        
        if (protobufType.equals(Type.MESSAGE)) {
            Schema messageSchema = protoDescriptorToSchema(fd.getMessageType());
            schema.add(new FieldSchema(fieldName, messageSchema));
        } else {
            schema.add(new FieldSchema(fieldName, pigType));
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
    private static Tuple messageToTuple(MessageOrBuilder message) 
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, 
            ExecException {
        
        Descriptor descriptor = (Descriptor) message.getClass().getMethod("getDescriptor").invoke(null);
        int fieldsCount = descriptor.getFields().size();
        Tuple output = TupleFactory.getInstance().newTuple(fieldsCount);

        for (FieldDescriptor fd : message.getAllFields().keySet()) {
            Type type = fd.getType();
            if (fd.isRepeated()) {
                List<?> l = (List<?>) message.getField(fd);
                if (l.isEmpty()) {
                    continue;
                }
                DataBag db = BagFactory.getInstance().newDefaultBag();
                for (Object messageField : l) {
                    db.add((Tuple) messageFieldToTupleField(messageField, type, true));
                }
                output.set(fd.getIndex(), db);
            } else {
                output.set(fd.getIndex(), messageFieldToTupleField(message.getField(fd), type, false));
            }
        }
        return output;
    }

    private static Object messageFieldToTupleField(Object messageField, Type type, boolean enforceTuple) 
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, 
            ExecException {

        if (type.equals(Type.MESSAGE)) {
            return messageToTuple((Message) messageField);
        } else {
            Object retObject = null;
            if (type.equals(Type.BYTES)) {
                retObject = new DataByteArray(((ByteString) messageField).toByteArray());
            } else if (type.equals(Type.ENUM)) {
                retObject = ((EnumValueDescriptor) messageField).getName();
            } else if (typesMap.containsKey(type)) {
                retObject = messageField;
            }
            if (retObject != null && enforceTuple) {
                retObject = TupleFactory.getInstance().newTuple(retObject);
            }
            return retObject;
        }
    }
}

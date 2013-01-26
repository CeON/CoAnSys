/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;

/**
 * Pig UDF converting pig tuple to protocol buffers message.
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class TupleToProtobuf extends EvalFunc<DataByteArray> {

    private Class<? extends Message> protobufClass;
    /**
     * A map between protobuf types and java class used in pig data
     */
    private static final Map<Type, Class> protobufToJavaTypes = new EnumMap<Type, Class>(Type.class);

    static {
        protobufToJavaTypes.put(Type.STRING, String.class);
        protobufToJavaTypes.put(Type.INT32, Integer.class);
        protobufToJavaTypes.put(Type.SINT32, Integer.class);
        protobufToJavaTypes.put(Type.UINT32, Integer.class);
        protobufToJavaTypes.put(Type.INT64, Long.class);
        protobufToJavaTypes.put(Type.SINT64, Long.class);
        protobufToJavaTypes.put(Type.UINT64, Long.class);
        protobufToJavaTypes.put(Type.FLOAT, Float.class);
        protobufToJavaTypes.put(Type.DOUBLE, Double.class);
        protobufToJavaTypes.put(Type.BOOL, Boolean.class);
        protobufToJavaTypes.put(Type.ENUM, String.class);
        protobufToJavaTypes.put(Type.MESSAGE, Tuple.class);
        protobufToJavaTypes.put(Type.BYTES, DataByteArray.class);
    }

    /**
     * This constructor cannot be called directly in pig latin scripts, but it
     * can be used in default constructor of a subclass.
     *
     * @param protobufClass a class of protocol buffers messages
     */
    public TupleToProtobuf(Class<? extends Message> protobufClass) {
        this.protobufClass = protobufClass;
    }

    /**
     * Constructor with a protobuf class name. It can be called directly from
     * pig latin scripts, i.e.: <p> define myUDF
     * pl.edu.icm.coansys.importers.pig.udf.TupleToProtobuf("protobufClassName");
     * <p> FOREACH data GENERATE myUDF($0);
     *
     * @param protobufClassName
     * @throws ClassNotFoundException
     */
    public TupleToProtobuf(String protobufClassName) throws ClassNotFoundException {
        this((Class<? extends Message>) Class.forName(protobufClassName));
    }

    private TupleToProtobuf() {
    }

    /**
     * Returns a data schema to pig scripts (with a single BYTEARRAY field)
     *
     * @param input
     * @return
     */
    @Override
    public Schema outputSchema(Schema input) {
        return new Schema(new FieldSchema(protobufClass.getName().toLowerCase(Locale.ENGLISH), DataType.BYTEARRAY));
    }

    /**
     * Converts data from tuple to serialized protocol buffers message
     * 
     * @param input
     * @return
     * @throws ExecException 
     */
    @Override
    public DataByteArray exec(Tuple input) throws ExecException {
        
        Method method;
        try {
            method = protobufClass.getMethod("newBuilder");
        } catch (NoSuchMethodException ex) {
            throw new ExecException(ex);
        } catch (SecurityException ex) {
            throw new ExecException(ex);
        }

        Builder builder;
        try {
            builder = (Builder) method.invoke(null);
        } catch (IllegalArgumentException ex) {
            throw new ExecException(ex);
        } catch (InvocationTargetException ex) {
            throw new ExecException(ex);
        } catch (SecurityException ex) {
            throw new ExecException(ex);
        } catch (IllegalAccessException ex) {
            throw new ExecException(ex);
        }
        
        Message message = recursiveConvert(input, builder);
        return new DataByteArray(message.toByteArray());
    }

    private Message recursiveConvert(Tuple input, Builder builder) throws ExecException {

        Descriptor descr = builder.getDescriptorForType();
        if (descr.getFields().size() != input.size()) {
            throw new ExecException("Input tuple size doesn't match protobuf schema size");
        }

        for (FieldDescriptor protobufField : descr.getFields()) {
            Object tupleField = input.get(protobufField.getIndex());
            Type protobufType = protobufField.getType();

            if (tupleField == null) {
                if (protobufField.isRequired()) {
                    throw new ExecException("There is no data for required field " + protobufField.getName());
                }
                continue;
            }


            if (protobufField.isRepeated()) {
                //repeated field
                if (!(tupleField instanceof DataBag)) {
                    throw new ExecException("Data for repeated field must be in a DataBag, instead of" + tupleField.getClass().getName());
                }

                for (Tuple tpl : ((DataBag) tupleField)) {
                    if (!protobufToJavaTypes.containsKey(protobufType)) {
                        throw new ExecException("Type not supported: " + protobufType);
                    }

                    if (protobufType.equals(Type.MESSAGE)) {
                        Builder subbuilder = DynamicMessage.newBuilder(protobufField.getMessageType());
                        builder.addRepeatedField(protobufField, recursiveConvert(tpl, subbuilder));
                    } else { //scalar type
                        Object tplObj = tpl.get(0);
                        if (!protobufToJavaTypes.get(protobufType).equals(tplObj.getClass())) {
                            throw new ExecException("Data type not compatible: " + protobufType + ", " + tplObj.getClass().getName());
                        }
                        if (protobufType.equals(Type.ENUM)) {
                            EnumDescriptor enumDescr = protobufField.getEnumType();
                            EnumValueDescriptor enumValueDescr = enumDescr.findValueByName((String) tplObj);
                            builder.addRepeatedField(protobufField, enumValueDescr);
                        } else {
                            builder.addRepeatedField(protobufField, tplObj);
                        }
                    }
                }
            } else {
                //not repeated
                if (!protobufToJavaTypes.get(protobufType).equals(tupleField.getClass())) {
                    throw new ExecException("Data type not compatible: " + protobufType + ", " + tupleField.getClass().getName());
                }

                if (protobufType.equals(Type.MESSAGE)) {
                    Builder subbuilder = DynamicMessage.newBuilder(protobufField.getMessageType());
                    builder.setField(protobufField, recursiveConvert((Tuple) tupleField, subbuilder));
                } else if (protobufType.equals(Type.ENUM)) {
                    EnumDescriptor enumDescr = protobufField.getEnumType();
                    EnumValueDescriptor enumValueDescr = enumDescr.findValueByName((String) tupleField);
                    builder.setField(protobufField, enumValueDescr);
                } else { //scalar type
                    builder.setField(protobufField, tupleField);
                }
            }
        }

        return builder.build();
    }
}

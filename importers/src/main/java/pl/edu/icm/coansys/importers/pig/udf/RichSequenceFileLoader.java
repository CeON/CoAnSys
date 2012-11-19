package pl.edu.icm.coansys.importers.pig.udf;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.serializer.SerializationFactory;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileRecordReader;
import org.apache.pig.FileInputLoadFunc;
import org.apache.pig.backend.BackendException;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

/**
 * A Loader for Hadoop-Standard SequenceFiles. able to work with the following
 * types as keys or values: Text, IntWritable, LongWritable, FloatWritable,
 * DoubleWritable, BooleanWritable, ByteWritable
 *
 */
public class RichSequenceFileLoader extends FileInputLoadFunc {

    public static final byte BYTES_WRITABLE = 70;
    public static final byte RESULT = 80;
    
    private SequenceFileRecordReader<Writable, Writable> reader;
    private Writable key;
    private Writable value;
    private ArrayList<Object> mProtoTuple = null;
    private static final Log LOG = LogFactory.getLog(RichSequenceFileLoader.class);
    private TupleFactory mTupleFactory = TupleFactory.getInstance();
    private SerializationFactory serializationFactory;
    private byte keyType = DataType.UNKNOWN;
    private byte valType = DataType.UNKNOWN;
    private DataByteArray dataByteArray = new DataByteArray();

    public RichSequenceFileLoader() {
        mProtoTuple = new ArrayList<Object>(2);
    }

    protected void setKeyType(Class<?> keyClass) throws BackendException {
        this.keyType |= inferPigDataType(keyClass);
        if (keyType == DataType.ERROR) {
            LOG.warn("Unable to translate key " + key.getClass() + " to a Pig datatype");
            throw new BackendException("Unable to translate " + key.getClass() + " to a Pig datatype");
        }
    }

    protected void setValueType(Class<?> valueClass) throws BackendException {
        this.valType |= inferPigDataType(valueClass);
        if (keyType == DataType.ERROR) {
            LOG.warn("Unable to translate key " + key.getClass() + " to a Pig datatype");
            throw new BackendException("Unable to translate " + key.getClass() + " to a Pig datatype");
        }
    }

    protected byte inferPigDataType(Type t) {
        if (t == DataByteArray.class) {
            return DataType.BYTEARRAY;
        } else if (t == Text.class) {
            return DataType.CHARARRAY;
        } else if (t == IntWritable.class) {
            return DataType.INTEGER;
        } else if (t == LongWritable.class) {
            return DataType.LONG;
        } else if (t == FloatWritable.class) {
            return DataType.FLOAT;
        } else if (t == DoubleWritable.class) {
            return DataType.DOUBLE;
        } else if (t == BooleanWritable.class) {
            return DataType.BOOLEAN;
        } else if (t == ByteWritable.class) {
            return DataType.BYTE;
        } else if (t == BytesWritable.class) {
            return BYTES_WRITABLE;
        } else if (t == Result.class) {
            return RESULT;
        } // not doing maps or other complex types for now
        else {
            return DataType.ERROR;
        }
    }

    protected Object translateWritableToPigDataType(Writable w, byte dataType) {
        switch (dataType) {
            case DataType.CHARARRAY:
                return ((Text) w).toString();
            case DataType.BYTEARRAY:
                return ((DataByteArray) w).get();
            case DataType.INTEGER:
                return ((IntWritable) w).get();
            case DataType.LONG:
                return ((LongWritable) w).get();
            case DataType.FLOAT:
                return ((FloatWritable) w).get();
            case DataType.DOUBLE:
                return ((DoubleWritable) w).get();
            case DataType.BYTE:
                return ((ByteWritable) w).get();
            case BYTES_WRITABLE:
                dataByteArray.set(((BytesWritable) w).copyBytes());
                return dataByteArray.get();
        }
        return null;
    }

    @Override
    public Tuple getNext() throws IOException {
        boolean next = false;
        try {
            next = reader.nextKeyValue();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }

        if (!next) {
            return null;
        }

        key = reader.getCurrentKey();
        value = reader.getCurrentValue();

        if (keyType == DataType.UNKNOWN && key != null) {
            setKeyType(key.getClass());
        }
        if (valType == DataType.UNKNOWN && value != null) {
            setValueType(value.getClass());
        }

        mProtoTuple.add(translateWritableToPigDataType(key, keyType));
        mProtoTuple.add(translateWritableToPigDataType(value, valType));
        Tuple t = mTupleFactory.newTuple(mProtoTuple);
        mProtoTuple.clear();
        return t;
    }

    @SuppressWarnings("unchecked")
    @Override
    public InputFormat getInputFormat() throws IOException {
        return new SequenceFileInputFormat<Writable, Writable>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void prepareToRead(RecordReader reader, PigSplit split)
            throws IOException {
        this.reader = (SequenceFileRecordReader) reader;
    }

    @Override
    public void setLocation(String location, Job job) throws IOException {
        FileInputFormat.setInputPaths(job, location);
    }
}
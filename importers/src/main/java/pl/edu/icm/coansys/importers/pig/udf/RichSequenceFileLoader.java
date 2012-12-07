package pl.edu.icm.coansys.importers.pig.udf;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileRecordReader;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.pig.FileInputLoadFunc;
import org.apache.pig.LoadFunc;
import org.apache.pig.ResourceSchema;
import org.apache.pig.StoreFunc;
import org.apache.pig.StoreFuncInterface;
import org.apache.pig.backend.BackendException;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.PigSplit;
import org.apache.pig.backend.hadoop.executionengine.util.MapRedUtil;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.PigContext;
import org.apache.pig.impl.util.UDFContext;

/**
 * A Loader for Hadoop-Standard SequenceFiles. able to work with the following
 * types as keys or values: Text, IntWritable, LongWritable, FloatWritable,
 * DoubleWritable, BooleanWritable, ByteWritable
 *
 */
public class RichSequenceFileLoader extends FileInputLoadFunc implements StoreFuncInterface {

    private static final Log LOG = LogFactory.getLog(RichSequenceFileLoader.class);
    private SequenceFileRecordReader<Writable, Writable> reader;
    private RecordWriter<Writable, Writable> writer;
    private Writable key;
    private Writable value;
    private ArrayList<Object> mProtoTuple = null;
    private TupleFactory mTupleFactory = TupleFactory.getInstance();
    private byte keyType = DataType.UNKNOWN;
    private byte valType = DataType.UNKNOWN;
    private DataByteArray dataByteArray = new DataByteArray();
    private Configuration config = new Configuration();
    private Class<?> keyClass;
    private Class<?> valueClass;

    public RichSequenceFileLoader() {
        mProtoTuple = new ArrayList<Object>(2);
    }

    public RichSequenceFileLoader(String keyClassName, String valueClassName) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        this();
        keyClass = config.getClassByName(keyClassName);
        valueClass = config.getClassByName(valueClassName);

        key = (Writable) keyClass.newInstance();
        value = (Writable) valueClass.newInstance();
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
            return DataType.BYTEARRAY;
        } else {
            return DataType.ERROR;
        }
    }

    protected Object translateWritableToPigDataType(Writable w, byte dataType) {
        switch (dataType) {
            case DataType.CHARARRAY:
                return ((Text) w).toString();
            case DataType.BYTEARRAY:
                if (w instanceof BytesWritable) {
                    dataByteArray.set(((BytesWritable) w).copyBytes());
                    return dataByteArray.get();
                } else {
                    return ((DataByteArray) w).get();
                }
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
        }
        return null;
    }

    protected void translatePigDataTypeToWritable(Tuple t, int fieldNum, Writable writable) throws ExecException {
        byte dataType = t.getType(fieldNum);
        Object dataValue = t.get(fieldNum);

        switch (dataType) {
            case DataType.CHARARRAY:
                ((Text) writable).set(dataValue.toString());
                break;
            case DataType.BYTEARRAY:
                byte[] data = ((DataByteArray) dataValue).get();
                ((BytesWritable) writable).set(data, 0, data.length);
                break;
            case DataType.INTEGER:
                ((IntWritable) writable).set(((Integer) dataValue).intValue());
                break;
            case DataType.LONG:
                ((LongWritable) writable).set(((Long) dataValue).longValue());
                break;
            case DataType.FLOAT:
                ((FloatWritable) writable).set(((Float) dataValue).floatValue());
                break;
            case DataType.DOUBLE:
                ((DoubleWritable) writable).set(((Double) dataValue).doubleValue());
                break;
            case DataType.BYTE:
                ((ByteWritable) writable).set(((Byte) dataValue).byteValue());
                break;
        }
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

    @Override
    public String relToAbsPathForStoreLocation(String location, Path curDir) throws IOException {
        return LoadFunc.getAbsolutePath(location, curDir);
    }

    @Override
    public OutputFormat getOutputFormat() throws IOException {
        return new SequenceFileOutputFormat<Writable, Writable>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setStoreLocation(String location, Job job) throws IOException {
        ensureUDFContext(job.getConfiguration());
        job.setOutputKeyClass(keyClass);
        job.setOutputValueClass(valueClass);
        FileOutputFormat.setOutputPath(job, new Path(location));
        if ("true".equals(job.getConfiguration().get("output.compression.enabled"))) {
            FileOutputFormat.setCompressOutput(job, true);
            String codec = job.getConfiguration().get("output.compression.codec");
            FileOutputFormat.setOutputCompressorClass(job,
                    PigContext.resolveClassName(codec).asSubclass(CompressionCodec.class));
        } else {
            // This makes it so that storing to a directory ending with ".gz" or ".bz2" works.
            setCompression(new Path(location), job);
        }
    }

    private void ensureUDFContext(Configuration conf) throws IOException {
        if (UDFContext.getUDFContext().isUDFConfEmpty()
                && conf.get("pig.udf.context") != null) {
            MapRedUtil.setupUDFContext(conf);
        }
    }

    /**
     * @param path
     * @param job
     */
    private void setCompression(Path path, Job job) {
        CompressionCodecFactory codecFactory = new CompressionCodecFactory(job.getConfiguration());
        CompressionCodec codec = codecFactory.getCodec(path);
        if (codec != null) {
            FileOutputFormat.setCompressOutput(job, true);
            FileOutputFormat.setOutputCompressorClass(job, codec.getClass());
        } else {
            FileOutputFormat.setCompressOutput(job, false);
        }
    }

    @Override
    public void checkSchema(ResourceSchema s) throws IOException {
    }

    @Override
    public void prepareToWrite(RecordWriter writer) throws IOException {
        this.writer = writer;
    }

    @Override
    public void putNext(Tuple t) throws ExecException, IOException {
        try {

            translatePigDataTypeToWritable(t, 0, key);
            translatePigDataTypeToWritable(t, 1, value);
            writer.write(key, value);

        } catch (Exception ex) {
            String message = "Unable to write key/value pair to output, key: " + key.getClass() + ", value: " + value.getClass()
                    + ", writer " + writer + " ex " + ex;
            LOG.warn(message);
            throw new BackendException(message);
        }
    }

    @Override
    public void setStoreFuncUDFContextSignature(String signature) {
    }

    @Override
    public void cleanupOnFailure(String location, Job job) throws IOException {
        StoreFunc.cleanupOnFailureImpl(location, job);
    }
}
/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class SequenceFileRecordWriter<K extends Writable, V extends Writable>
        extends RecordWriter<K, V> {

    private Writer writer;

    @SuppressWarnings("deprecation")
    public SequenceFileRecordWriter(Configuration conf, Class<?> keyClass, Class<?> valueClass)
            throws IOException, ClassNotFoundException {
        Path location = new Path(conf.get("location") + "/_tmp" + (new Date()).getTime());
        
        writer = SequenceFile.createWriter(location.getFileSystem(conf), conf, location, keyClass, valueClass);
    }

    @Override
    public void write(K key, V value) throws IOException {
        writer.append(key, value);
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        writer.close();
    }
}
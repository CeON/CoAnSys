/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.io.writers.hbase;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

/**
 *
 * @author akawa
 */
public class BytesWritableSeqFileToHBasePutMapper extends Mapper<BytesWritable, BytesWritable, ImmutableBytesWritable, Put> {

    private byte[] columnFamily;
    private byte[] columnQualifier;
    private ImmutableBytesWritable key = new ImmutableBytesWritable();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        String fullColumnName = configuration.get("hbase.table.full.column.name");
        String[] parts = fullColumnName.split(":");
        columnFamily = Bytes.toBytes(parts[0]);
        columnQualifier = Bytes.toBytes(parts[1]);
    }

    @Override
    protected void map(BytesWritable rowKey, BytesWritable value, Context context)
            throws IOException, InterruptedException {

        Put put = new Put(rowKey.copyBytes());
        put.add(columnFamily, columnQualifier, value.copyBytes());

        key.set(rowKey.copyBytes());
        context.write(key, put);
    }
}
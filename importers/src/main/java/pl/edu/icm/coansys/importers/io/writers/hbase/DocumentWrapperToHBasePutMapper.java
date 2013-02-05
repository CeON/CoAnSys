/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.io.writers.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.mapreduce.Mapper;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.importers.transformers.DocumentWrapper2HBasePut;

/**
 *
 * @author akawa
 */
public class DocumentWrapperToHBasePutMapper extends Mapper<BytesWritable, BytesWritable, ImmutableBytesWritable, Put> {

        private ImmutableBytesWritable docWrapRowKey = new ImmutableBytesWritable();

        @Override
        protected void map(BytesWritable rowKey, BytesWritable documentWrapperBytes, Context context)
                throws IOException, InterruptedException {

            DocumentWrapper documentWrapper = DocumentWrapper.parseFrom(documentWrapperBytes.copyBytes());
            Put put = DocumentWrapper2HBasePut.translate(documentWrapper);
            docWrapRowKey.set(put.getRow());

            context.write(docWrapRowKey, put);
        }
    }

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.transformers;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;


/**
 *
 * @author akawa
 */
public class DocumentWrapper2HBasePut {

    public static Put translate(DocumentWrapper documentWrapper) {
        byte[] row = documentWrapper.getRowId().getBytes();
        Put put = new Put(row);
        put = addMetadataFamily(put, documentWrapper);
        put = addContentFamily(put, documentWrapper);
        return put;
    }

    private static Put addContentFamily(Put put, DocumentWrapper documentWrapper) {
        byte[] bytes = documentWrapper.getMediaContainer().toByteArray();
        put.add(Bytes.toBytes(HBaseConstant.FAMILY_CONTENT), Bytes.toBytes(HBaseConstant.FAMILY_CONTENT_QUALIFIER_PROTO), bytes);
        return put;
    }

    private static Put addMetadataFamily(Put put, DocumentWrapper documentWrapper) {
        byte[] bytes = documentWrapper.getDocumentMetadata().toByteArray();
        put.add(Bytes.toBytes(HBaseConstant.FAMILY_METADATA), Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO), bytes);
        return put;
    }

    
}

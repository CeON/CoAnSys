/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers.transformers;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import pl.edu.icm.coansys.importers.constants.HBaseConstant;
import pl.edu.icm.coansys.importers.models.DocumentDTO;

/**
 * @author pdendek
 * @author akawa
 */
public class DocumentDTO2HBasePut {
	
	public static Put translate(DocumentDTO docDTO) {
        byte[] row = Bytes.toBytes(RowComposer.composeRow(docDTO));
        Put put = new Put(row);
        put = addMetadataFamily(put, docDTO);
        put = addContentFamily(put, docDTO);
        return put;
    }

    private static Put addContentFamily(Put put, DocumentDTO docDTO) {
        put.add(Bytes.toBytes(HBaseConstant.FAMILY_CONTENT), 
        		Bytes.toBytes(HBaseConstant.FAMILY_CONTENT_QUALIFIER_PROTO), 
                docDTO.getMediaConteiner().toByteArray());
        return put;
    }

    private static Put addMetadataFamily(Put put, DocumentDTO docDTO) {
        put.add(Bytes.toBytes(HBaseConstant.FAMILY_METADATA), 
        		Bytes.toBytes(HBaseConstant.FAMILY_METADATA_QUALIFIER_PROTO),
                docDTO.getDocumentMetadata().toByteArray());
        return put;
    }

}

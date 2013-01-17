/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.pig.udf;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author acz
 */
public class DocumentMetadataToTuple extends ProtobufToTuple {
    
    private static final Class PROTOBUF_CLASS = DocumentMetadata.class;
    
    public DocumentMetadataToTuple() {
        super(PROTOBUF_CLASS);
    }
}

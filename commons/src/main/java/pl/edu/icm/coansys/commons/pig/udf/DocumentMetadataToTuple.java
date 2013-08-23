/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.pig.udf;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 *
 * @author acz
 */
public class DocumentMetadataToTuple extends ProtoBytearrayToTuple {
    
    private static final Class<DocumentMetadata> PROTOBUF_CLASS = DocumentMetadata.class;
    
    public DocumentMetadataToTuple() {
        super(PROTOBUF_CLASS);
    }
}

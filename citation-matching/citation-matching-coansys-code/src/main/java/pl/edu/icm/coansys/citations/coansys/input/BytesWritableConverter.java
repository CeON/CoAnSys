package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;

import org.apache.hadoop.io.BytesWritable;

import com.google.common.base.Preconditions;

import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
 * Converter of {@link BytesWritable} to {@link DocumentWrapper}
 * 
* @author Łukasz Dumiszewski
*/

class BytesWritableConverter implements Serializable {

    private static final long serialVersionUID = 1L;

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link BytesWritable} to {@link DocumentWrapper}
     */
    public DocumentWrapper convertToDocumentWrapper(BytesWritable bytesWritable) {
        
        Preconditions.checkNotNull(bytesWritable);
        
        DocumentWrapper docWrapper = null;
        try {
            docWrapper = DocumentWrapper.parseFrom(bytesWritable.copyBytes());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return docWrapper;
    }
    

    /**
     * Converts {@link BytesWritable} to the pair ({@link DocumentMetadata#getKey()}, {@link DocumentWrapper})
     */
    public Tuple2<String, DocumentWrapper> convertToDocumentWrapperTuple2(BytesWritable bytesWritable) {
        DocumentWrapper docWrapper = convertToDocumentWrapper(bytesWritable);
        return new Tuple2<>(docWrapper.getDocumentMetadata().getKey(), docWrapper);
    }
}

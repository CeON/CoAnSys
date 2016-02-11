package pl.edu.icm.coansys.citations.coansys.input;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.python.google.common.base.Preconditions;

import pl.edu.icm.coansys.citations.InputDocumentReader;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 * Coansys implementation of {@link InputDocumentReader}
 * 
* @author Łukasz Dumiszewski
*/

public class CoansysInputDocumentReader implements InputDocumentReader<String, DocumentWrapper>, Serializable {


    private static final long serialVersionUID = 1L;
    
    private BytesWritableConverter bytesWritableConverter = new BytesWritableConverter();
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Reads documents from the given path as pairs of ({@link DocumentMetadata#getKey()}, {@link DocumentWrapper}) 
     */
    @Override
    public JavaPairRDD<String, DocumentWrapper> readDocuments(JavaSparkContext sparkContext, String inputDocumentPath, Integer numberOfPartitions) {
        
        Preconditions.checkArgument(StringUtils.isNotBlank(inputDocumentPath));
        
        return sparkContext.sequenceFile(inputDocumentPath, Writable.class, BytesWritable.class, numberOfPartitions)
                           .mapToPair(bw -> bytesWritableConverter.convertToDocumentWrapperTuple2(bw._2()));
    
    }

    //------------------------ SETTERS --------------------------

   
    public void setBytesWritableConverter(BytesWritableConverter bytesWritableConverter) {
        this.bytesWritableConverter = bytesWritableConverter;
    }
    
}


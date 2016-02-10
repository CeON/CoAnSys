package pl.edu.icm.coansys.citations.coansys.input;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import pl.edu.icm.coansys.citations.InputCitationReader;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.ReferenceMetadata;

/**
* Coansys implementation of {@link InputCitationReader}
* 
* @author Łukasz Dumiszewski
* 
*/

public class CoansysInputCitationReader implements InputCitationReader<String, ReferenceMetadata> {

    private JavaSparkContext sparkContext;
    
    private int maxSupportedCitationLength = 2000;
    
    private BytesWritableConverter bytesWritableConverter = new BytesWritableConverter();

    private ReferenceExtractor referenceExtractor = new ReferenceExtractor();

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Reads citations from the given path as {@link DocumentWrapper}s and converts them to pairs of ({@link DocumentMetadata#getKey()}, {@link ReferenceMetadata})
     */
    @Override
    public JavaPairRDD<String, ReferenceMetadata> readCitations(String inputCitationPath, Integer numberOfPartitions) {
        
        JavaPairRDD<Writable, BytesWritable> rawCitations = sparkContext.sequenceFile(inputCitationPath, Writable.class, BytesWritable.class, numberOfPartitions);
        
        JavaRDD<DocumentWrapper> docWrappers = rawCitations.map(bw -> bytesWritableConverter.convertToDocumentWrapper(bw._2()));
        
        final int maxSupportedCitationLength = this.maxSupportedCitationLength;
        
        JavaPairRDD<String, ReferenceMetadata> docReferences = docWrappers.flatMapToPair(docWrapper -> referenceExtractor.extractReferences(docWrapper, maxSupportedCitationLength));
        
        return docReferences;
    }

    
    //------------------------ SETTERS --------------------------

    @Override
    public void setSparkContext(JavaSparkContext sparkContext) {
        this.sparkContext = sparkContext;
    }

    public void setBytesWritableConverter(BytesWritableConverter bytesWritableConverter) {
        this.bytesWritableConverter = bytesWritableConverter;
    }
    
    public void setReferenceExtractor(ReferenceExtractor referenceExtractor) {
        this.referenceExtractor = referenceExtractor;
    }

    public void setMaxSupportedCitationLength(int maxSupportedCitationLength) {
        this.maxSupportedCitationLength = maxSupportedCitationLength;
    }
    
    
    
}


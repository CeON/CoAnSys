package pl.edu.icm.coansys.citations;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Reader of input documents
 * 
 * @author madryk
 */
public interface InputDocumentReader<K, V> {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns spark rdd containing documents
     * 
     * @param inputDocumentPath - path to documents file
     * @param numberOfPartitions - how many partitions should be created in citations rdd
     */
    JavaPairRDD<K, V> readDocuments(String inputDocumentPath, Integer numberOfPartitions);
    
    
    //------------------------ SETTERS --------------------------
    
    /**
     * Sets spark context for this class
     */
    void setSparkContext(JavaSparkContext sparkContext);
}

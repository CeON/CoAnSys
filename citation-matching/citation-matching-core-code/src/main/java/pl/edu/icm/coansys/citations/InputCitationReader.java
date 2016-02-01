package pl.edu.icm.coansys.citations;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Reader of input citations
 * 
 * @author madryk
 */
public interface InputCitationReader<K,V> {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns spark rdd containing citations
     * 
     * @param inputCitationPath - path to citations file
     * @param numberOfPartitions - how many partitions should be created in citations rdd
     */
    JavaPairRDD<K,V> readCitations(String inputCitationPath, Integer numberOfPartitions);
    
    
    //------------------------ SETTERS --------------------------
    
    /**
     * Sets spark context for this class
     */
    void setSparkContext(JavaSparkContext sparkContext);
}

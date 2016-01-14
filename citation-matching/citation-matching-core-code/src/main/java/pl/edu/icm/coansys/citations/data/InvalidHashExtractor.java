package pl.edu.icm.coansys.citations.data;

import org.apache.spark.api.java.JavaPairRDD;

import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class InvalidHashExtractor {

    
    /**
     * Extracts hashes that are invalid. The invalid hash is one whose number of appearances in the hashIdPairs1 
     * multiplied by the number in the hashIdPairs2 is bigger than maxHashBucketSize or equal 0 (zero)
     * 
     * @param hashIdPairs1 pair rdd that has tuples containing id and hash
     * @param hashIdPairs2 pair rdd that has tuples containing id and hash 
     * @param maxHashBucketSize max number of pairs of the same hash that have been found in hashIdPairs1 and hashIdPairs2 that will be considered valid
     */
    public JavaPairRDD<String, Long> extractInvalidHashes(JavaPairRDD<String, String> hashIdPairs1, JavaPairRDD<String, String> hashIdPairs2, long maxHashBucketSize) {
        
        JavaPairRDD<String, Long> hash1Count = hashIdPairs1.mapToPair(kv->new Tuple2<String, Long>(kv._1(), 1l)).reduceByKey((x,y)->x+y);
        JavaPairRDD<String, Long> hash2Count = hashIdPairs2.mapToPair(kv->new Tuple2<String, Long>(kv._1(), 1l)).reduceByKey((x,y)->x+y);

        JavaPairRDD<String, Long> hashesSmallerThanBucketSize = hash1Count.cogroup(hash2Count).mapToPair(kv-> {
            String hash = kv._1();
            Long hash1IdCount = kv._2()._1().iterator().hasNext() ? kv._2()._1().iterator().next(): 0l;
            Long hash2IdCount = kv._2()._2().iterator().hasNext() ? kv._2()._2().iterator().next(): 0l;
            return new Tuple2<String, Long>(hash, hash1IdCount*hash2IdCount);   
        }).filter(x->(x._2()>maxHashBucketSize || x._2()==0));
        
        return hashesSmallerThanBucketSize;
        
    }
    
}

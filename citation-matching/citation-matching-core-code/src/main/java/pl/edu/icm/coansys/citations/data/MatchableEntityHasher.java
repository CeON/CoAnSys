package pl.edu.icm.coansys.citations.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.edu.icm.coansys.citations.hashers.HashGenerator;
import scala.Tuple2;
import scala.collection.JavaConversions;

/**
 * @author Łukasz Dumiszewski
 */

public class MatchableEntityHasher implements Serializable {

    private static final long serialVersionUID = 1L;
    	
    private HashGenerator hashGenerator;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    public List<Tuple2<String, String>> hashEntity(MatchableEntity entity) {
        
        List<Tuple2<String, String>> hashIdPairs = new ArrayList<>();
        
        Iterable<String> hashes = JavaConversions.asJavaIterable(hashGenerator.generate(entity));
        
        for (String hash : hashes) {
            hashIdPairs.add(createHashIdPair(hash, entity.id()));
        }
        
        return hashIdPairs;
     }

    
    //------------------------ PRIVATE --------------------------

    private Tuple2<String, String> createHashIdPair(String hash, String id) {
        //MarkedText entityId = new MarkedText(id, true);
        //MarkedText generatedHash = new MarkedText(hash, false);
        Tuple2<String, String> hashIdPair = new Tuple2<>(hash, id);
        return hashIdPair;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setHashGenerator(HashGenerator hashGenerator) {
        this.hashGenerator = hashGenerator;
    }
    
}
    

package pl.edu.icm.coansys.citations;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.api.java.JavaSparkContext;

import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.hashers.HashGenerator;


/**
 * Factory of {@link CoreCitationMatchingService} service
 * 
 * @author madryk
 */
public class CoreCitationMatchingSimpleFactory {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates and returns {@link CoreCitationMatchingService}.
     */
    public CoreCitationMatchingService createCoreCitationMatchingService(JavaSparkContext sc, long maxHashBucketSize, List<String> hashGeneratorClasses) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        CoreCitationMatchingService coreCitationMatchingService = new CoreCitationMatchingService();
        coreCitationMatchingService.setSparkContext(sc);
        coreCitationMatchingService.setMaxHashBucketSize(maxHashBucketSize);
        coreCitationMatchingService.setMatchableEntityHashers(createMatchableEntityHashers(hashGeneratorClasses));
        
        return coreCitationMatchingService;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<Pair<MatchableEntityHasher, MatchableEntityHasher>> createMatchableEntityHashers(List<String> hashGeneratorClassNames) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<Pair<MatchableEntityHasher, MatchableEntityHasher>> matchableEntityHashers = Lists.newArrayList();
        
        for (String citAndDocHashGeneratorClassNames : hashGeneratorClassNames) {
            String citationHashGeneratorClassName = citAndDocHashGeneratorClassNames.split(":")[0];
            String documentHashGeneratorClassName = citAndDocHashGeneratorClassNames.split(":")[1];
            
            MatchableEntityHasher citationHasher = createMatchableEntityHasher(citationHashGeneratorClassName);
            MatchableEntityHasher documentHasher = createMatchableEntityHasher(documentHashGeneratorClassName);
            
            matchableEntityHashers.add(new ImmutablePair<MatchableEntityHasher, MatchableEntityHasher>(citationHasher, documentHasher));
        }
        
        return matchableEntityHashers;
    }
    
    private MatchableEntityHasher createMatchableEntityHasher(String hashGeneratorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        HashGenerator hashGenerator = (HashGenerator) Class.forName(hashGeneratorClass).newInstance();
        MatchableEntityHasher matchableEntityHasher = new MatchableEntityHasher();
        matchableEntityHasher.setHashGenerator(hashGenerator);
        return matchableEntityHasher;
    }
    
}

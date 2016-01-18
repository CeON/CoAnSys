package pl.edu.icm.coansys.citations.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.collect.Lists;

import pl.edu.icm.coansys.citations.hashers.HashGenerator;

/**
 * 
 * Citation matching job.
 * 
 * @author Łukasz Dumiszewski
 */

public class CitationMatchingJob {
    
    
    //------------------------ LOGIC --------------------------
    
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        CitationMatchingJobParameters params = new CitationMatchingJobParameters();
        JCommander jcommander = new JCommander(params);
        jcommander.parse(args);
        
        
        SparkConf conf = new SparkConf();
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.registerKryoClasses(new Class[]{MatchableEntity.class});
        
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            
            // read citations and documents
            JavaPairRDD<Text, BytesWritable> citations = sc.sequenceFile(params.citationPath, Text.class, BytesWritable.class);
            //citations.cache(); cache here makes the join (a few lines below) not working - the join gives 0 results
            JavaPairRDD<Text, BytesWritable> documents = sc.sequenceFile(params.documentPath, Text.class, BytesWritable.class);
            
            
            List<Pair<MatchableEntityHasher, MatchableEntityHasher>> entitiesHashers = createMatchableEntityHashers(params.hashGeneratorClasses);
            
            
            JavaPairRDD<Text, BytesWritable> unmatchedCitations = citations;
            JavaPairRDD<Text, Text> joinedCitDocIdPairs = JavaPairRDD.fromJavaRDD(sc.emptyRDD());
            
            Iterator<Pair<MatchableEntityHasher, MatchableEntityHasher>> citAndDocHashersIterator = entitiesHashers.iterator();
            while(citAndDocHashersIterator.hasNext()) {
                Pair<MatchableEntityHasher, MatchableEntityHasher> citAndDocHashers = citAndDocHashersIterator.next();
                HashHeuristicCitationMatcher hashHeuristicCitationMatcher = new HashHeuristicCitationMatcher(citAndDocHashers.getLeft(), citAndDocHashers.getRight(), params.maxHashBucketSize);
                
                HashHeuristicResult matchedResult = 
                        hashHeuristicCitationMatcher.matchCitations(unmatchedCitations, documents, citAndDocHashersIterator.hasNext());
                
                joinedCitDocIdPairs = joinedCitDocIdPairs.union(matchedResult.getCitDocIdPairs());
                unmatchedCitations = matchedResult.getUnmatchedCitations();
            }

            // save matched citationId-docId pairs
            joinedCitDocIdPairs.saveAsNewAPIHadoopFile(params.outputDirPath, Text.class, Text.class, SequenceFileOutputFormat.class);
            
        }
        
    }




    //------------------------ PRIVATE --------------------------
    
    private static List<Pair<MatchableEntityHasher, MatchableEntityHasher>> createMatchableEntityHashers(List<String> hashGeneratorClassNames) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
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
    
    private static MatchableEntityHasher createMatchableEntityHasher(String hashGeneratorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        HashGenerator hashGenerator = (HashGenerator) Class.forName(hashGeneratorClass).newInstance();
        MatchableEntityHasher matchableEntityHasher = new MatchableEntityHasher();
        matchableEntityHasher.setHashGenerator(hashGenerator);
        return matchableEntityHasher;
    }
    
    
    @Parameters(separators = "=")
    private static class CitationMatchingJobParameters {
        
        @Parameter(names = "-citationPath", required = true, description = "path to directory/file with citations")
        private String citationPath;
        
        @Parameter(names = "-documentPath", required = true, description = "path to directory/file with documents")
        private String documentPath;
        
        
        @Parameter(names = "-hashGeneratorClasses", required = true, 
                description = "Name of classes used for generating hashes for citations and documents. Names must be seperated by colon sign (:). "
                        + "First class will be used for citations and second for documents."
                        + "Classes must implement pl.edu.icm.coansys.citations.hashers.HashGenerator interface."
                        + "Parameter may be specified multiple times for multiple heuristics.")
        private List<String> hashGeneratorClasses;
        
        @Parameter(names = "-outputDirPath", required = true, description = "path to directory with results")
        private String outputDirPath;
        
        @Parameter(names="-maxHashBucketSize", required = false, description = "max number of the citation-documents pairs for a given hash")
        private long maxHashBucketSize = 10000;
        
    }
    
    
}

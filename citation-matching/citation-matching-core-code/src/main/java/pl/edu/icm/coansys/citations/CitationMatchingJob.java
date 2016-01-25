package pl.edu.icm.coansys.citations;

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
import pl.edu.icm.coansys.citations.data.HeuristicHashMatchingResult;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;

/**
 * 
 * Citation matching job.
 * 
 * @author Łukasz Dumiszewski
 */

public class CitationMatchingJob {
    
    private static DocumentAttacher documentAttacher = new DocumentAttacher();
    
    private static CitationAttacherWithMatchedLimiter citationAttacher = new CitationAttacherWithMatchedLimiter();
    
    private static BestMatchedCitationPicker bestMatchedCitationPicker = new BestMatchedCitationPicker();
    
    
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
//            citations = citations.cache(); // cache here makes the join (a few lines below) not working - the join gives 0 results
            JavaPairRDD<Text, BytesWritable> documents = sc.sequenceFile(params.documentPath, Text.class, BytesWritable.class);
            
            
            JavaPairRDD<Text, Text> citIdDocIdPairs = matchCitDocByHashes(sc, citations, documents, createMatchableEntityHashers(params.hashGeneratorClasses), params.maxHashBucketSize);
            
            
            JavaPairRDD<Text, TextWithBytesWritable> citIdDocPairs = documentAttacher.attachDocuments(citIdDocIdPairs, documents);
            JavaPairRDD<TextWithBytesWritable, TextWithBytesWritable> citDocPairs = citationAttacher.attachCitationsAndLimitDocs(citIdDocPairs, citations);
            
            JavaPairRDD<TextWithBytesWritable, Text> matchedCitations = bestMatchedCitationPicker.pickBest(citDocPairs);
            
            // save matched citations
            matchedCitations.saveAsNewAPIHadoopFile(params.outputDirPath, TextWithBytesWritable.class, Text.class, SequenceFileOutputFormat.class);
            
        }
        
    }




    //------------------------ PRIVATE --------------------------
    
    private static JavaPairRDD<Text, Text> matchCitDocByHashes(JavaSparkContext sc,
            JavaPairRDD<Text, BytesWritable> citations, JavaPairRDD<Text, BytesWritable> documents,
            List<Pair<MatchableEntityHasher, MatchableEntityHasher>> entitiesHashers, long maxHashBucketSize) {
        
        JavaPairRDD<Text, BytesWritable> unmatchedCitations = citations;
        JavaPairRDD<Text, Text> joinedCitDocIdPairs = JavaPairRDD.fromJavaRDD(sc.emptyRDD());
        
        Iterator<Pair<MatchableEntityHasher, MatchableEntityHasher>> entitiesHashersIterator = entitiesHashers.iterator();
        while(entitiesHashersIterator.hasNext()) {
            Pair<MatchableEntityHasher, MatchableEntityHasher> citAndDocHashers = entitiesHashersIterator.next();
            HeuristicHashCitationMatcher heuristicHashCitationMatcher = new HeuristicHashCitationMatcher(citAndDocHashers.getLeft(), citAndDocHashers.getRight(), maxHashBucketSize);
            
            HeuristicHashMatchingResult matchedResult = 
                    heuristicHashCitationMatcher.matchCitations(unmatchedCitations, documents, entitiesHashersIterator.hasNext());
            
            joinedCitDocIdPairs = joinedCitDocIdPairs.union(matchedResult.getCitDocIdPairs());
            unmatchedCitations = matchedResult.getUnmatchedCitations();
        }
        
        return joinedCitDocIdPairs;
    }
    
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
                description = "Names of classes used for generating hashes for citations and documents. The names must be seperated by colon sign (:). "
                        + "The first class will be used for citations and the second for documents."
                        + "The classes must implement pl.edu.icm.coansys.citations.hashers.HashGenerator interface."
                        + "This parameter may be specified multiple times. Separately for each given heurisitic.")
        private List<String> hashGeneratorClasses;
        
        @Parameter(names = "-outputDirPath", required = true, description = "path to directory with results")
        private String outputDirPath;
        
        @Parameter(names="-maxHashBucketSize", required = false, description = "max number of the citation-documents pairs for a given hash")
        private long maxHashBucketSize = 10000;
        
    }
    
    
}

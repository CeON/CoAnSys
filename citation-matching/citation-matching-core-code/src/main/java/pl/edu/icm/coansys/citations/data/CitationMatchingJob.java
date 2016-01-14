package pl.edu.icm.coansys.citations.data;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import pl.edu.icm.coansys.citations.hashers.HashGenerator;
import scala.Tuple2;

/**
 * 
 * Citation matching job.
 * 
 * @author Łukasz Dumiszewski
 */

public class CitationMatchingJob {

    private static InvalidHashExtractor invalidHashExtractor = new InvalidHashExtractor();
    
    
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
            JavaPairRDD<Writable, BytesWritable> citations = sc.sequenceFile(params.citationPath, Writable.class, BytesWritable.class);
            //citations.cache(); cache here makes the join (a few lines below) not working - the join gives 0 results
            JavaPairRDD<Writable, BytesWritable> documents = sc.sequenceFile(params.documentPath, Writable.class, BytesWritable.class);
            
            // generate hashes
            
            JavaPairRDD<String, String> citationHashIdPairs = generateHashIdPairs(citations, params.citationHashGeneratorClass);
            JavaPairRDD<String, String> documentHashIdPairs = generateHashIdPairs(documents, params.documentHashGeneratorClass);

            // remove invalid hashes
            JavaPairRDD<String, Long> invalidHashes = invalidHashExtractor.extractInvalidHashes(citationHashIdPairs, documentHashIdPairs, params.maxHashBucketSize);

            citationHashIdPairs = citationHashIdPairs.subtractByKey(invalidHashes);
            documentHashIdPairs = documentHashIdPairs.subtractByKey(invalidHashes);
            
            // join citationIds to documentIds by hash
            JavaPairRDD<String, String> citationDocumentIdPairs = citationHashIdPairs.join(documentHashIdPairs).mapToPair(cd->cd._2()).distinct();
            
            // find and save unmatched citations
            if (StringUtils.isNotEmpty(params.unmatchedCitationDirPath)) {
                JavaPairRDD<String, BytesWritable> citationIdBytes = citations.mapToPair(c->new Tuple2<String, BytesWritable>(c._1().toString(), c._2()));
                JavaPairRDD<Text, BytesWritable> unmatchedCitations = citationIdBytes.subtractByKey(citationDocumentIdPairs).mapToPair(c->new Tuple2<Text, BytesWritable>(new Text(c._1()), c._2()));
                unmatchedCitations.saveAsNewAPIHadoopFile(params.unmatchedCitationDirPath, Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
                
            }

            // save matched citationId-docId pairs
            JavaPairRDD<Text, Text> textCitDocIdPairs = citationDocumentIdPairs.mapToPair(strCitDocId -> new Tuple2<Text, Text>(new Text(strCitDocId._1()), new Text(strCitDocId._2())));
            textCitDocIdPairs.saveAsNewAPIHadoopFile(params.outputDirPath, Text.class, Text.class, SequenceFileOutputFormat.class);
            
        }
        
    }




    //------------------------ PRIVATE --------------------------

    
    
    private static JavaPairRDD<String, String> generateHashIdPairs(JavaPairRDD<Writable, BytesWritable> matchableEntityBytes, String hashGeneratorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        MatchableEntityHasher hasher = createMatchableEntityHasher(hashGeneratorClass);
        
        JavaPairRDD<String, String> hashIdPairs = matchableEntityBytes.flatMapToPair((Tuple2<Writable, BytesWritable> keyValue)-> {
            MatchableEntity matchableEntity = MatchableEntity.fromBytes(keyValue._2().copyBytes());
            return hasher.hashEntity(matchableEntity);
        });
        
        return hashIdPairs;
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
        
        @Parameter(names = "-citationHashGeneratorClass", required = true)
        private String citationHashGeneratorClass;
        
        @Parameter(names = "-documentHashGeneratorClass", required = true)
        private String documentHashGeneratorClass;
        
        @Parameter(names = "-unmatchedCitationDirPath", required = false, description = "path to directory with unmatched citations, empty - do not look for and do not save unmatched citations")
        private String unmatchedCitationDirPath;
        
        @Parameter(names = "-outputDirPath", required = true, description = "path to directory with results")
        private String outputDirPath;
        
        @Parameter(names="-maxHashBucketSize", required = false, description = "max number of the citation-documents pairs for a given hash")
        private long maxHashBucketSize = 10000;
        
    }
    
    
}

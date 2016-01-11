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

    
    
    
    //------------------------ LOGIC --------------------------
    
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        CitationMatchingJobParameters params = new CitationMatchingJobParameters();
        JCommander jcommander = new JCommander(params);
        jcommander.parse(args);
        
        
        SparkConf conf = new SparkConf();
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.registerKryoClasses(new Class[]{MatchableEntity.class});
        
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            
            
            JavaPairRDD<Writable, BytesWritable> citations = sc.sequenceFile(params.citationPath, Writable.class, BytesWritable.class);
            citations.cache();
            
            JavaPairRDD<String, String> citationHashIdPairs = generateHashIdPairs(citations, params.citationHashGeneratorClass);
            
            JavaPairRDD<Writable, BytesWritable> documents = sc.sequenceFile(params.documentPath, Writable.class, BytesWritable.class);
            JavaPairRDD<String, String> documentHashIdPairs = generateHashIdPairs(documents, params.documentHashGeneratorClass);
            
            JavaPairRDD<String, String> citationDocumentIdPairs = citationHashIdPairs.join(documentHashIdPairs).mapToPair(cd->cd._2()).distinct();
            
            if (StringUtils.isNotEmpty(params.unmatchedCitationDirPath)) {
                // look for and save unmatched citations
            }
            
            JavaPairRDD<Text, Text> textCitDocIdPairs = citationDocumentIdPairs.mapToPair(strCitDocId -> new Tuple2<Text, Text>(new Text(strCitDocId._1()), new Text(strCitDocId._2())));
            
            textCitDocIdPairs.saveAsNewAPIHadoopFile(params.outputDirPath, Text.class, Text.class, SequenceFileOutputFormat.class);
            
            //citationDocumentIdPairs.sortByKey().foreach(System.out::println);
            //System.out.println(documentHashIdPairs.take(2).get(0));
            //SparkAvroSaver.saveJavaRDD(documentClasses, DocumentToDocumentClasses.SCHEMA$, params.outputAvroPath);
        
        }
        
    }



    private static JavaPairRDD<String, String> generateHashIdPairs(JavaPairRDD<Writable, BytesWritable> matchableEntityBytes, String hashGeneratorClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        MatchableEntityHasher hasher = createMatchableEntityHasher(hashGeneratorClass);
        
        JavaPairRDD<String, String> hashIdPairs = matchableEntityBytes.flatMapToPair((Tuple2<Writable, BytesWritable> keyValue)-> {
            MatchableEntity matchableEntity = MatchableEntity.fromBytes(keyValue._2().copyBytes());
            return hasher.hashEntity(matchableEntity);
        });
        
        return hashIdPairs;
    }


    
    //------------------------ PRIVATE --------------------------
    
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
        
        @Parameter(names="-maxBucketSize", required = false, description = "max number of the citation-documents pairs for a given hash")
        private int maxBucketSize = 10000;
        
    }
    
    
}

package pl.edu.icm.coansys.citations.datacreation;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import pl.edu.icm.coansys.citations.DefaultInputReader;
import pl.edu.icm.coansys.citations.InputDocumentReader;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.TextWithLanguage;
import scala.Tuple2;

/**
 * 
 * Doc creation job.
 * 
 * @author Łukasz Dumiszewski
 */

public class DocDataCreationJob {
    
    
    //------------------------ LOGIC --------------------------
    
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        
        
        SparkConf conf = new SparkConf();
        //conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        //conf.set("spark.kryo.registrator", "pl.edu.icm.coansys.citations.MatchableEntityKryoRegistrator");
        
        
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            
            InputDocumentReader<String, MatchableEntity> inputDocumentReader = new DefaultInputReader();
            inputDocumentReader.setSparkContext(sc);
                
            String docPath = "/C:/workspace/coansys/citation-matching/citation-matching-core-code/src/test/resources/heuristic/documents";
            
            JavaPairRDD<String, MatchableEntity> documents = inputDocumentReader.readDocuments(docPath, 1);
        
            JavaPairRDD<String, DocumentWrapper> docWrappers = documents.mapToPair(me->new Tuple2<String, DocumentWrapper>(me._2().id(), convertToDocumentWrapper(me._2())));
            
            
            
            
            docWrappers.collect().forEach(x-> {
                System.out.println(x._1());
                System.out.println(x._2());
            }
            );
            
            docWrappers.mapToPair(k->new Tuple2<Text, BytesWritable>(new Text(k._1()), new BytesWritable(k._2().toByteArray()))).saveAsNewAPIHadoopFile("/c:/temp/rawdocuments", Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
            
           
        }
    }
    
    
    
    
    private static String matchableIdToDocId(String matchableId) {
        return matchableId.substring(4).replaceAll("_", "=");
        
    }
    
    
    private static DocumentWrapper convertToDocumentWrapper(MatchableEntity matchableEntity) {
        
        DocumentMetadata.Builder docMetadataB = DocumentMetadata.newBuilder().setKey(matchableIdToDocId(matchableEntity.id()));
        
        BasicMetadata.Builder bmB = BasicMetadata.newBuilder();
        
        for (String author : matchableEntity.author().split(", ")) {
            Author.Builder ab = Author.newBuilder();
            ab.setName(author);
            ab.setKey("1");
            bmB.addAuthor(ab.build());
        }
        bmB.setJournal(matchableEntity.source());
        TextWithLanguage.Builder twl = TextWithLanguage.newBuilder();
        twl.setText(matchableEntity.title());
        bmB.addTitle(twl);
        bmB.setPages(matchableEntity.pages());
        bmB.setIssue(matchableEntity.issue());
        bmB.setVolume(matchableEntity.volume());
        bmB.setYear(matchableEntity.year());
        
        docMetadataB.setBasicMetadata(bmB.build());
        
        DocumentWrapper.Builder docWrapperB = DocumentWrapper.newBuilder();
        docWrapperB.setDocumentMetadata(docMetadataB.build());
        docWrapperB.setRowId(matchableIdToDocId(matchableEntity.id()));
        
        return docWrapperB.build();
        
    }


    
    
}

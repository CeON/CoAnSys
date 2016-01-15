package pl.edu.icm.coansys.citations;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import pl.edu.icm.coansys.citations.data.MarkedText;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import scala.Tuple2;

/**
 * Spark job responsible for replacing document id for document MatchableEntity in
 * (citation_id, document_id) pair
 * 
 * @author madryk
 */
public class DocAttacherJob {

    private static DocumentAttacher documentAttacher = new DocumentAttacher();


    public static void main(String[] args) throws IOException {

        DocumentAttacherJobParameters params = new DocumentAttacherJobParameters();
        JCommander jcommander = new JCommander(params);
        jcommander.parse(args);


        SparkConf conf = new SparkConf();
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");


        try (JavaSparkContext sc = new JavaSparkContext(conf)) {

            JavaPairRDD<Text, Text> matchedCitations = sc.sequenceFile(params.inputMatchedPath, Text.class, Text.class);

            JavaPairRDD<Text, BytesWritable> documents = sc.sequenceFile(params.inputDocsPath, Text.class, BytesWritable.class);



            JavaPairRDD<Text, TextWithBytesWritable> matchedWithDocsAttached = documentAttacher.attachDocuments(matchedCitations, documents);


            JavaPairRDD<MarkedText, TextWithBytesWritable> markedMatchedWithDocsAttached = 
                    matchedWithDocsAttached.mapToPair(pair -> new Tuple2<MarkedText, TextWithBytesWritable>(
                            new MarkedText(pair._1.toString(), false),
                            pair._2));
            markedMatchedWithDocsAttached.saveAsHadoopFile(params.outputPath, MarkedText.class, TextWithBytesWritable.class, SequenceFileOutputFormat.class);

        }

    }

    @Parameters(separators = "=")
    private static class DocumentAttacherJobParameters {

        @Parameter(names = "-inputMatchedPath", required = true)
        private String inputMatchedPath;

        @Parameter(names = "-inputDocsPath", required = true)
        private String inputDocsPath;

        @Parameter(names = "-outputPath", required = true)
        private String outputPath;

    }
}

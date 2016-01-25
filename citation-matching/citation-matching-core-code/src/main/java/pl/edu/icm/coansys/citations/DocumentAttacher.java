package pl.edu.icm.coansys.citations;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;

import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import scala.Tuple2;

/**
 * Attacher of documents in place of document id
 * in matched citations
 * 
 * @author madryk
 */
public class DocumentAttacher {

	public JavaPairRDD<Text, TextWithBytesWritable> attachDocuments(JavaPairRDD<Text, Text> matchedCitations, JavaPairRDD<Text, BytesWritable> documents) {
		
		return matchedCitations
				.mapToPair(x -> x.swap())
				.join(documents)
				.mapToPair(x -> new Tuple2<Text, TextWithBytesWritable>(x._2._1, new TextWithBytesWritable(x._1, x._2._2)));
		
	}
}

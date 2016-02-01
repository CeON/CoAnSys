package pl.edu.icm.coansys.citations;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.api.java.JavaPairRDD;

import pl.edu.icm.coansys.citations.data.IdWithSimilarity;
import pl.edu.icm.coansys.citations.data.MatchableEntity;
import pl.edu.icm.coansys.citations.data.TextWithBytesWritable;
import scala.Tuple2;

/**
 * Default writer of output matched citations
 * 
 * @author madryk
 */
public class DefaultOutputWriter implements OutputWriter<MatchableEntity, IdWithSimilarity> {
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void writeMatchedCitations(JavaPairRDD<MatchableEntity, IdWithSimilarity> matchedCitations, String path) {
        
        matchedCitations
            .mapToPair(x -> new Tuple2<TextWithBytesWritable, Text>(
                    new TextWithBytesWritable(x._1.id(), x._1.data().toByteArray()),
                    new Text(x._2.getSimilarity() + ":" + x._2.getId())))
            .saveAsNewAPIHadoopFile(path, TextWithBytesWritable.class, Text.class, SequenceFileOutputFormat.class);
        
    }


}

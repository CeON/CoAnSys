package pl.edu.icm.coansys.citations.coansys.output;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.api.java.JavaPairRDD;

import com.google.common.base.Preconditions;

import pl.edu.icm.coansys.citations.OutputWriter;
import pl.edu.icm.coansys.models.PICProtos.PicOut;
import scala.Tuple2;

/**
 * 
 * Coansys implementation of {@link OutputWriter} 
 * 
 * @author Łukasz Dumiszewski
*/

public class CoansysOutputWriter implements OutputWriter<String, PicOut> {

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Writes the given pairs of (srcDocumentId, {@link PicOut}) to files at the given location as ({@link Text}, {@link BytesWritable})
     */
    @Override
    public void writeMatchedCitations(JavaPairRDD<String, PicOut> srcDocIdPicOuts, String path) {
        
        Preconditions.checkNotNull(srcDocIdPicOuts);
        Preconditions.checkNotNull(path);
        
        srcDocIdPicOuts
                .mapToPair(srcDocIdPicOut -> new Tuple2<Text, BytesWritable>(new Text(srcDocIdPicOut._1()), new BytesWritable(srcDocIdPicOut._2().toByteArray())))
                .saveAsNewAPIHadoopFile(path, Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
    }

}

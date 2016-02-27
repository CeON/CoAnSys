package pl.edu.icm.coansys.citations.coansys.output;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.models.PICProtos.PicOut;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class CoansysOutputWriterTest {

    
    private CoansysOutputWriter coansysOutputWriter = new CoansysOutputWriter();
    
    @Mock
    private JavaPairRDD<String, PicOut> srcDocIdPicOutRdd;
    
    @Mock
    private JavaPairRDD<Text, BytesWritable> srcDocIdPicOutWritableRdd;
    
    @Captor
    private ArgumentCaptor<PairFunction<Tuple2<String, PicOut>, Text, BytesWritable>> convertToWritableFunction;
    
    @Mock
    private Tuple2<String, PicOut> srcDocIdPicOutTuple2;
    
    
    @BeforeTest
    public void beforeTest() {
        
        MockitoAnnotations.initMocks(this);
        
    }
    
    //------------------------ TESTS --------------------------
    
    @Test(expectedExceptions=NullPointerException.class)
    public void writeMatchedCitations_NULL_path() {
        
        // execute
        
        coansysOutputWriter.writeMatchedCitations(srcDocIdPicOutRdd, null);
       
    }
    
    @Test(expectedExceptions=NullPointerException.class)
    public void writeMatchedCitations_NULL_srcDocIdPicOuts() {
        
        // execute
        
        coansysOutputWriter.writeMatchedCitations(null, "/path");
        
    }

    @Test
    public void writeMatchedCitations() throws Exception {
        
        // given
        
        String path = "/path";
        
        doReturn(srcDocIdPicOutWritableRdd).when(srcDocIdPicOutRdd).mapToPair(Mockito.any());
        
        // execute
        
        coansysOutputWriter.writeMatchedCitations(srcDocIdPicOutRdd, path);
       
        // verify
        
        verify(srcDocIdPicOutWritableRdd).saveAsNewAPIHadoopFile(path, Text.class, BytesWritable.class, SequenceFileOutputFormat.class);
        verify(srcDocIdPicOutRdd).mapToPair(convertToWritableFunction.capture());
        assertConvertToWritableFunction(convertToWritableFunction.getValue());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertConvertToWritableFunction(PairFunction<Tuple2<String, PicOut>, Text, BytesWritable> function) throws Exception {
        
        // given
        
        when(srcDocIdPicOutTuple2._1()).thenReturn("XYZ");
        PicOut picOut = PicOut.newBuilder().setDocId("ssss").build();
        when(srcDocIdPicOutTuple2._2()).thenReturn(picOut);
        
        // execute
        
        Tuple2<Text, BytesWritable> writableTuple2 = function.call(srcDocIdPicOutTuple2);
        
        // assert
        assertEquals(writableTuple2._1().copyBytes(), new Text("XYZ").copyBytes());
        assertEquals(writableTuple2._2().copyBytes(), picOut.toByteArray());
    }
}

package pl.edu.icm.coansys.citations.coansys.input;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.apache.hadoop.io.BytesWritable;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class BytesWritableConverterTest {

    
    private BytesWritableConverter bytesWritableConverter = new BytesWritableConverter();
    
    private DocumentWrapper docWrapper;
    
    
    @BeforeTest
    public void beforeTest() {
        BasicMetadata basicMetadata = BasicMetadata.newBuilder().build();
        DocumentMetadata docMetadata = DocumentMetadata.newBuilder().setBasicMetadata(basicMetadata).setKey("KEY").build();
        docWrapper = DocumentWrapper.newBuilder().setRowId("ROW_ID").setDocumentMetadata(docMetadata).build();
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToDocumentWrapper_NULL() {
        
        // execute
        
        bytesWritableConverter.convertToDocumentWrapper(null);
        
    }

    
    @Test
    public void convertToDocumentWrapper() {
        
        // given
        
        BytesWritable bytesWritable = new BytesWritable(docWrapper.toByteArray());
        
        // execute
        
        DocumentWrapper retDocWrapper = bytesWritableConverter.convertToDocumentWrapper(bytesWritable);
        
        // assert
        
        assertFalse(docWrapper == retDocWrapper);
        assertEquals(docWrapper.getDocumentMetadata().getKey(), retDocWrapper.getDocumentMetadata().getKey());
        
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void convertToDocumentWrapper_Wrong_Arg() {
        
        // given
        
        BytesWritable bytesWritable = new BytesWritable(new byte[] {9, 126, 12});
        
        // execute
        
        bytesWritableConverter.convertToDocumentWrapper(bytesWritable);
        
    }
    
    @Test
    public void convertToTuple2() {
        
        // given
        
        BytesWritable bytesWritable = new BytesWritable(docWrapper.toByteArray());
        
        // execute
        
        Tuple2<String, DocumentWrapper> retDocWrapperTuple2 = bytesWritableConverter.convertToDocumentWrapperTuple2(bytesWritable);
        
        // assert
        
        DocumentWrapper retDocWrapper = retDocWrapperTuple2._2();
        assertFalse(docWrapper == retDocWrapper);
        assertEquals(retDocWrapper.getDocumentMetadata().getKey(), docWrapper.getDocumentMetadata().getKey());
        assertEquals(retDocWrapperTuple2._1(), docWrapper.getDocumentMetadata().getKey());
    }
    
    
}

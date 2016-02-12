package pl.edu.icm.coansys.citations.coansys;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.coansys.models.PICProtos.Reference;

/**
 * @author Łukasz Dumiszewski
 * @author madryk
 */
public class PicOutAssert {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    private PicOutAssert() { }
    
    
    //------------------------ LOGIC --------------------------
    
    public static void assertDocIdPicOutsEquals(List<Pair<Text, BytesWritable>> expectedOutputDocIdPicOuts, List<Pair<Text, BytesWritable>> actualOutputDocIdPicOuts) throws Exception {
        
        assertEquals(actualOutputDocIdPicOuts.size(), expectedOutputDocIdPicOuts.size());
        
        for (Pair<Text, BytesWritable> actualDocIdPicOut : actualOutputDocIdPicOuts) {
            assertTrue(isInExcpectedDocIdPicOuts(expectedOutputDocIdPicOuts, actualDocIdPicOut));
        }
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private static boolean isInExcpectedDocIdPicOuts(List<Pair<Text, BytesWritable>> expectedOutputDocIdPicOuts, Pair<Text, BytesWritable> actualDocIdPicOut) throws Exception {
        
        for (Pair<Text, BytesWritable> expectedDocIdPicOut : expectedOutputDocIdPicOuts) {
            
            if (expectedDocIdPicOut.getKey().toString().equals(actualDocIdPicOut.getKey().toString())) {
                PicOut expectedPicOut = PicOut.parseFrom(expectedDocIdPicOut.getValue().copyBytes());
                PicOut actualPicOut = PicOut.parseFrom(actualDocIdPicOut.getValue().copyBytes());
                assertPicOuts(expectedPicOut, actualPicOut);
                return true;
            }
        }
        
        return false;
    
    }
    
    
    private static void assertPicOuts(PicOut expectedPicOut, PicOut actualPicOut) {
        assertEquals(actualPicOut.getDocId(), expectedPicOut.getDocId());
        assertEquals(actualPicOut.getRefsCount(), expectedPicOut.getRefsCount());
        for (Reference actualRef : actualPicOut.getRefsList()) {
            assertTrue(isInExpectedRefs(expectedPicOut.getRefsList(), actualRef));
        }
        
    }


    private static boolean isInExpectedRefs(List<Reference> expectedRefs, Reference actualRef) {
        for (Reference expectedRef : expectedRefs) {
            if (refsEqual(expectedRef, actualRef)) {
                return true;
            }
        }
        
        return false;
        
    }

    private static boolean refsEqual(Reference ref1, Reference ref2) {
        return ref1.getDocId().equals(ref2.getDocId()) &&
               (ref1.getRefNum() == ref2.getRefNum()) &&
               ref1.getRawText().equals(ref2.getRawText()); 
    }
    
}

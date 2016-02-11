
package pl.edu.icm.coansys.citations.coansys.output;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.coansys.models.PICProtos.Reference;
import scala.Tuple2;

/**
* @author Łukasz Dumiszewski
*/

public class ReferenceToPicOutConverterTest {

    private ReferenceToPicOutConverter referenceToPicOutConverter = new ReferenceToPicOutConverter();
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToPicOut_Null_srcDocIdReferences() {
        
        // execute
        
        referenceToPicOutConverter.convertToPicOut(null);
    }
    
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToPicOut_Null_srcDocId() {
    
        // given
        
        Reference ref1 = createReference(1, "X");
        List<Reference> refs = Lists.newArrayList(ref1);
        Tuple2<String, Iterable<Reference>> srcDocIdReferences = new Tuple2<>(null, refs);
        
        
        // execute
        
        referenceToPicOutConverter.convertToPicOut(srcDocIdReferences);
    }
    
    
    @Test(expectedExceptions=NullPointerException.class)
    public void convertToPicOut_Null_refs() {
    
        // given
        
        Tuple2<String, Iterable<Reference>> srcDocIdReferences = new Tuple2<>("XYZ", null);
        
        
        // execute
        
        referenceToPicOutConverter.convertToPicOut(srcDocIdReferences);
    }
    
    
    @Test
    public void convertToPicOut() {
    
        // given
        
        Reference ref1 = createReference(1, "X");
        Reference ref2 = createReference(2, "C");
        Reference ref3 = createReference(3, "Z");
        List<Reference> refs = Lists.newArrayList(ref1, ref2, ref3);
        Tuple2<String, Iterable<Reference>> srcDocIdReferences = new Tuple2<>("XYZ", refs);
        
        
        // execute
        
        Tuple2<String, PicOut> srcDocIdPicOut = referenceToPicOutConverter.convertToPicOut(srcDocIdReferences);
        
        
        // assert
        
        assertEquals(srcDocIdPicOut._1(), "XYZ");
        assertEquals(srcDocIdPicOut._2().getDocId(), "XYZ");
        assertEquals(srcDocIdPicOut._2().getRefsCount(), refs.size());
        assertTrue(srcDocIdPicOut._2().getRefs(0) == ref1);
        assertTrue(srcDocIdPicOut._2().getRefs(1) == ref2);
        assertTrue(srcDocIdPicOut._2().getRefs(2) == ref3);
        
    }

    //------------------------ PRIVATE --------------------------
    
    private Reference createReference(int refNum, String docId) {
        return Reference.newBuilder().setRefNum(refNum).setDocId(docId).build();
    }
}

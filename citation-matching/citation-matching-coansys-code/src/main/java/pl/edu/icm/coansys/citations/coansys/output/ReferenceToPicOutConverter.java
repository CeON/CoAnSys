package pl.edu.icm.coansys.citations.coansys.output;

import com.google.common.base.Preconditions;

import pl.edu.icm.coansys.models.PICProtos.PicOut;
import pl.edu.icm.coansys.models.PICProtos.Reference;
import scala.Tuple2;

/**
 * 
 * Converter of pairs of (sourceDocumentId, Iterable&lt;Reference&gt;) to pairs of (sourceDocumentId, {@link PicOut})
 * 
 * @author Łukasz Dumiszewski
*/

class ReferenceToPicOutConverter {
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given pair of (sourceDocumentId, Iterable&lt;Reference&gt;) to the pair of (sourceDocumentId, {@link PicOut})
     */
    public Tuple2<String, PicOut> convertToPicOut(Tuple2<String, Iterable<Reference>> srcDocIdReferences) {
        
        Preconditions.checkNotNull(srcDocIdReferences);
        Preconditions.checkNotNull(srcDocIdReferences._1());
        Preconditions.checkNotNull(srcDocIdReferences._2());
        
        
        String srcDocId = srcDocIdReferences._1();
        
        PicOut.Builder picOutB = PicOut.newBuilder();
        picOutB.setDocId(srcDocId);
        
        for (Reference reference : srcDocIdReferences._2()) {
            picOutB.addRefs(reference);
        }
        
        return new Tuple2<>(srcDocId, picOutB.build());
        
    }

}

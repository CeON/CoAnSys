package pl.edu.icm.coansys.disambiguation.work;

import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import pl.edu.icm.coansys.importers.models.DocumentProtos;
import pl.edu.icm.coansys.importers.models.DocumentProtos.Author;
import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;

/** 
 * Contains various utility methods related to the {@link DocumentWrapper} class
 * @author lukdumi
 * 
 * */
public abstract class DocumentWrapperUtils {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(DocumentWrapperUtils.class);
    
    
    private DocumentWrapperUtils() {
        throw new IllegalStateException("a helper class, not to instantiate");
    }
    
    
    /** 
     * documentWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText() 
     * 
     * */
    public static String getMainTitle(DocumentWrapper documentWrapper) {
        return documentWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText();
    }
    
    
    
    /**
     * Returns the author of the publication that is on the given authorPosition. 
     * Returns null if there is no author on the authorPosition.  
     */
    public static Author getAuthor(DocumentWrapper documentWrapper, int authorPosition) {
        List<Author> authors = documentWrapper.getDocumentMetadata().getBasicMetadata().getAuthorList();
        if (CollectionUtils.isEmpty(authors)) {
            return null;
        }
        for (Author author : authors) {
            if (author.getPositionNumber()==authorPosition) {
                return author;
            }
        }
        return null;
    }
    
    
    
    /** 
     * documentWrapper.getDocumentMetadata().getBasicMetadata().getYear()
     */
    public static String getPublicationYear(DocumentWrapper documentWrapper) {
        return documentWrapper.getDocumentMetadata().getBasicMetadata().getYear();
    }
    
    
    
    /** 
     * Returns list of {@link DocumentWrapper}s parsed from values
     */
    public static List<DocumentWrapper> extractDocumentWrappers(Text key, Iterable<BytesWritable> values) throws InvalidProtocolBufferException {
        List<DocumentWrapper> documents = Lists.newArrayList();
        
        for (BytesWritable value : values) {
            DocumentWrapper docWrapper = DocumentProtos.DocumentWrapper.parseFrom(value.copyBytes());
            documents.add(docWrapper);
        }
        
        return documents;
    }
    
}

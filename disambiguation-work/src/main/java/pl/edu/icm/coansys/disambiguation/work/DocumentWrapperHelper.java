package pl.edu.icm.coansys.disambiguation.work;

import pl.edu.icm.coansys.importers.models.DocumentProtos.DocumentWrapper;

public abstract class DocumentWrapperHelper {

    
    private DocumentWrapperHelper() {
        throw new IllegalStateException("a helper class, not to instantiate");
    }
    
    
    public static String getMainTitle(DocumentWrapper documentWrapper) {
        return documentWrapper.getDocumentMetadata().getBasicMetadata().getTitle(0).getText();
    }
    
}

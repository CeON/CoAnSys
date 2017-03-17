package pl.edu.icm.coansys.document.deduplication.merge;

import java.util.List;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;

/**
 *
 * @author acz
 */
public interface DuplicatesMerger {
    public void setup(String mergerConfiguration);
    public DocumentWrapper merge(List<DocumentWrapper> duplicates);
}

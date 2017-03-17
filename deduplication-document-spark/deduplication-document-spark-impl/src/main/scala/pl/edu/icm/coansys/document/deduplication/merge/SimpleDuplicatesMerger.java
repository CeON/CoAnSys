package pl.edu.icm.coansys.document.deduplication.merge;

//import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.BasicMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentWrapper;
import pl.edu.icm.coansys.models.DocumentProtos.KeyValue;
//import pl.edu.icm.coansys.1output.merge.MergeConstants;

/**
 * Chooses first DocumentWrapper, updates keys in DocumentWrapper, DocumentMetadata and authors, gets extIds from all
 * DocumentWrappers
 *
 * @author acz
 */
public class SimpleDuplicatesMerger implements DuplicatesMerger {

    @Override
    public DocumentWrapper merge(List<DocumentWrapper> duplicates) {

        if (duplicates == null || duplicates.isEmpty()) {
            throw new RuntimeException("Nothing to merge");
        } else if (duplicates.size() == 1) {
            return duplicates.get(0);
        }

        // Collect information for final result
        List<String> identifiers = new ArrayList<String>(duplicates.size());
        Map<String, Set<String>> extIds = new HashMap<String, Set<String>>();
        SortedSet<String> sortedCollections = new TreeSet<String>();

        for (DocumentWrapper dw : duplicates) {
            DocumentMetadata dm = dw.getDocumentMetadata();
            identifiers.add(dw.getRowId());
            for (String collection : dm.getCollectionList()) {
                sortedCollections.add(collection);
            }
            for (KeyValue id : dm.getExtIdList()) {
                String idSource = id.getKey();
                String idValue = id.getValue();
                if (!extIds.containsKey(idSource)) {
                    extIds.put(idSource, new HashSet<String>());
                }
                extIds.get(idSource).add(idValue);
            }
        }
        Collections.sort(identifiers);
        String joinedIds = StringUtils.join(identifiers, "???");//MergeConstants.MERGED_ID_SEPARATOR);
        String newIdentifier = UUID.nameUUIDFromBytes(joinedIds.getBytes()).toString();

        // Create new DocumentWrapper.Builder
        DocumentWrapper.Builder resultBuilder = DocumentWrapper.newBuilder(duplicates.get(0));

        // Modify fields of DocumentWrapper.Builder
        resultBuilder.setRowId(newIdentifier);

        DocumentMetadata.Builder documentMetadataBuilder = resultBuilder.getDocumentMetadataBuilder();

        BasicMetadata.Builder basicMetadataBuilder = documentMetadataBuilder.getBasicMetadataBuilder();
        documentMetadataBuilder.setKey(newIdentifier);
        documentMetadataBuilder.addAllCollection(sortedCollections);

        for (Author.Builder authorBuilder : basicMetadataBuilder.getAuthorBuilderList()) {
            String positionSuffix = authorBuilder.getKey().replaceAll(".*(#c\\d+)", "$1");
            authorBuilder.setDocId(newIdentifier);
            authorBuilder.setKey(newIdentifier + positionSuffix);
        }

        documentMetadataBuilder.clearExtId();
        for (String eKey : extIds.keySet()) {
            for (String eValue : extIds.get(eKey)) {
                KeyValue.Builder ei = KeyValue.newBuilder();
                ei.setKey(eKey);
                ei.setValue(eValue);
                documentMetadataBuilder.addExtId(ei);
            }
        }

        // Build and return DocumentWrapper
        return resultBuilder.build();
    }

    @Override
    public void setup(String mergerConfiguration) {
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.ArrayList;
import java.util.List;


import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.AuthorBased;
import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.models.DocumentProtos.Author;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 * The {@link Extractor} from {@link DocumentProtos.DocumentMetadata} (commit
 * 33ad120f11eb430d450) to a feature value list. Please read the description of
 * the {@link AuthorBased} interface.
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DocumentProto2EmailExtractor implements Extractor<DocumentMetadata>, AuthorBased {

    @Override
    public List<String> extract(DocumentMetadata input, String... auxil) {
        String authId = auxil[0];
        
        DocumentMetadata dm = (DocumentMetadata) input;
        List<String> ret = new ArrayList<String>();
        for (Author a : dm.getBasicMetadata().getAuthorList()) {
            if (a.getKey().equals(authId)) {
                ret.add(a.getEmail());
                break;
            }
        }
        return ret;

    }
}

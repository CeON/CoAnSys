/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.author.features.extractors;

import java.util.ArrayList;
import java.util.List;


import pl.edu.icm.coansys.disambiguation.author.features.extractors.indicators.DocumentBased;
import pl.edu.icm.coansys.disambiguation.features.Extractor;
import pl.edu.icm.coansys.models.DocumentProtos.ClassifCode;
import pl.edu.icm.coansys.models.DocumentProtos.DocumentMetadata;

/**
 * The {@link Extractor} from {@link DocumentProtos.DocumentMetadata} (commit
 * 33ad120f11eb430d450) to a feature value list. Please read the description of
 * the {@link DocumentBased} interface.
 *
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
public class DocumentProto2ClassifCodeExtractor implements Extractor<DocumentMetadata>, DocumentBased {

    @Override
    public List<String> extract(DocumentMetadata input, String... auxil) {
        DocumentMetadata dm = (DocumentMetadata) input;
        List<String> ret = new ArrayList<String>();
        for (ClassifCode cc : dm.getBasicMetadata().getClassifCodeList()) {
            ret.addAll(cc.getValueList());
        }
        return ret;
    }
}

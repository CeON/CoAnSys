/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.transformers;

import pl.edu.icm.coansys.importers.models.DocumentDTO;

/**
 * @author pdendek
 */
public class DocumentDTO2WrapperLine {

    public static String translate(DocumentDTO docDTO) {
        return DocumentDTO2DocumentWrapper.translate(docDTO).toByteString().toStringUtf8();
    }
}

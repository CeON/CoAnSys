/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.transformers;

import com.google.protobuf.ByteString;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.models.DocumentProtosWrapper.DocumentWrapper;

/**
 * @author pdendek
 */
public class DocumentDTO2WrapperLine {

    private DocumentDTO2WrapperLine() {
    }

    public static String translate(DocumentDTO docDTO) {

        DocumentWrapper.Builder dw = DocumentWrapper.newBuilder();

        dw.setRowId(ByteString.copyFrom(RowComposer.composeRow(docDTO).getBytes()));
        dw.setMproto(docDTO.getDocumentMetadata().toByteString());
        dw.setCproto(docDTO.getMediaConteiner().toByteString());

        return dw.build().toByteString().toStringUtf8();
    }
}

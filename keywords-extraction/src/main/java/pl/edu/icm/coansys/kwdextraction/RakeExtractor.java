/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.InputStream;
import java.text.BreakIterator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import pl.edu.icm.cermine.DocumentTextExtractor;
import pl.edu.icm.cermine.PdfRawTextExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.coansys.importers.constants.ProtoConstants;
import pl.edu.icm.coansys.importers.models.DocumentProtos;

/**
 *
 * @author acz
 */
public class RakeExtractor {

    private List<String> keywords;

    public RakeExtractor(String docContent) {
        extractKeywords(docContent);
    }

    public RakeExtractor(InputStream pdfStream) throws AnalysisException {
        DocumentTextExtractor<String> extr = new PdfRawTextExtractor();
        String content = extr.extractText(pdfStream);
        extractKeywords(content);
    }
    
    public RakeExtractor(DocumentProtos.DocumentWrapper docWrapper) {
        StringBuilder sb = new StringBuilder();
        for (DocumentProtos.Media media : docWrapper.getMediaContainer().getMediaList()) {
            if (media.getMediaType().equals(ProtoConstants.mediaTypePdf)) {
                try {
                    DocumentTextExtractor<String> extr = new PdfRawTextExtractor();
                    sb.append(extr.extractText(media.getContent().newInput()));
                } catch (AnalysisException ex) {
                    // TODO zrobić ładne logi
                }
            } else if (media.getMediaType().equals(ProtoConstants.mediaTypeTxt)) {
                sb.append(media.getContent().toStringUtf8());
            }
        }
        extractKeywords(sb.toString());
    }

    private void extractKeywords(String content) {
        
        BreakIterator bi = BreakIterator.getWordInstance();
        // TODO...
        keywords = Arrays.asList("keyword1", "keyword2");
    }

    public List<String> getKeywords() {
        return keywords;
    }
}

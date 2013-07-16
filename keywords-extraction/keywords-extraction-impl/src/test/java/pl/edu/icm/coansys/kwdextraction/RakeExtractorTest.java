/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import static org.testng.Assert.assertEquals;
import pl.edu.icm.cermine.exception.AnalysisException;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class RakeExtractorTest {

    private static final String TXTFILE = "bson-cc0.txt";
    private static final String PDFFILE = "09752900.pdf";
    private static final String LANGUAGE = "en";
    private static final List<String> EXPECTED_KEYWORDS_FROM_TXT = Arrays.asList(
            "binary interchange formats",
            "binary json",
            "field names",
            "space efficiency");
    private static final List<String> EXPECTED_KEYWORDS_FROM_PDF = Arrays.asList(
            "inducing cellular organic functional disturbances",
            "normal red blood cell formation",
            "inadequate bone marrow responses compared",
            "erythroid colony forming units",
            "decreased total iron binding capacity",
            "diseases including inflammatory disorders",
            "senescent red blood cells",
            "iron deficiency body iron homeostasis");

    @org.testng.annotations.Test(groups = {"fast"})
    public void rakeTxtTest() throws IOException {
        String txtPath = this.getClass().getClassLoader().getResource(TXTFILE).getPath();
        String fileContent = FileUtils.readFileToString(new File(txtPath));
        RakeExtractor rake = new RakeExtractor(fileContent, LANGUAGE);

        List<String> extractedKeywords = rake.getKeywords(EXPECTED_KEYWORDS_FROM_TXT.size());

        assertEquals(extractedKeywords, EXPECTED_KEYWORDS_FROM_TXT);
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void rakePdfTest() throws IOException, AnalysisException {
        InputStream pdfStream = this.getClass().getClassLoader().getResourceAsStream(PDFFILE);
        byte[] pdfContent = IOUtils.toByteArray(pdfStream);
        RakeExtractor rake = new RakeExtractor(pdfContent, LANGUAGE);

        List<String> extractedKeywords = rake.getKeywords(EXPECTED_KEYWORDS_FROM_PDF.size());

        assertEquals(extractedKeywords, EXPECTED_KEYWORDS_FROM_PDF);
    }
}

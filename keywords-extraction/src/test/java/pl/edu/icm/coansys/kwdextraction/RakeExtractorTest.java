/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.kwdextraction;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import static org.testng.Assert.assertEquals;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class RakeExtractorTest {

    private static final String TXTFILE = "bson-cc0.txt";
    private static final String LANGUAGE = "en";
    private static final List<String> EXPECTED_KEYWORDS = Arrays.asList(
            "binary interchange formats",
            "binary json",
            "field names",
            "space efficiency");

    @org.testng.annotations.Test(groups = {"fast"})
    public void rakeTxtTest() throws IOException {
        String txtPath = this.getClass().getClassLoader().getResource(TXTFILE).getPath();
        String fileContent = FileUtils.readFileToString(new File(txtPath));
        RakeExtractor rake = new RakeExtractor(fileContent, LANGUAGE);

        List<String> extractedKeywords = rake.getKeywords(EXPECTED_KEYWORDS.size());

        assertEquals(extractedKeywords, EXPECTED_KEYWORDS);
    }
}

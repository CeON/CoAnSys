/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.kwdextraction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import static org.testng.Assert.assertTrue;
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

        assertTrue(listsAreSimilar(extractedKeywords, EXPECTED_KEYWORDS_FROM_TXT, EXPECTED_KEYWORDS_FROM_TXT.size() - 2));
    }

    @org.testng.annotations.Test(groups = {"fast"})
    public void rakePdfTest() throws IOException, AnalysisException {
        InputStream pdfStream = this.getClass().getClassLoader().getResourceAsStream(PDFFILE);
        byte[] pdfContent = IOUtils.toByteArray(pdfStream);
        RakeExtractor rake = new RakeExtractor(pdfContent, LANGUAGE);

        List<String> extractedKeywords = rake.getKeywords(EXPECTED_KEYWORDS_FROM_PDF.size());

        assertTrue(listsAreSimilar(extractedKeywords, EXPECTED_KEYWORDS_FROM_PDF, EXPECTED_KEYWORDS_FROM_PDF.size() - 2));
    }
    
    private boolean listsAreSimilar(List<String> first, List<String> second, int requiredCommon) {
        int common = 0;
        for (String item : first) {
            if (second.contains(item)) {
                common += 1;
            }
        }
        
        return common >= requiredCommon;
    }
}

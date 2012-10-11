/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.experimental.categories.Category;
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.test.SmallTest;
import pl.edu.icm.coansys.importers.transformers.DocumentDTO2TSVLine;

/**
 * @author pdendek
 */
@Category(SmallTest.class)
public class ZipDirToTSVFileTest {
	
    @org.testng.annotations.Test(groups = {"fast"})
    public void readZipDirTest() throws IOException {
        String zipDirPath = this.getClass().getClassLoader().getResource("zipdir").getPath();
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(zipDirPath, "TESTCOLLECTION");
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("zip2tsvTEST.ignore.tsv")));
        
        for (DocumentDTO doc : zdtp) {
        	bw.write(DocumentDTO2TSVLine.translate(doc));
        }
        bw.flush();
        bw.close();
    }
}

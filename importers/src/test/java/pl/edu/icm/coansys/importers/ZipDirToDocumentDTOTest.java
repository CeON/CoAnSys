/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */


package pl.edu.icm.coansys.importers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.test.SmallTest;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 * @author pdendek
 */
@Category(SmallTest.class)
public class ZipDirToDocumentDTOTest {
    	
//    @Test
    public void readZipDirTest() {
        String zipDirPath = this.getClass().getClassLoader().getResource("zipdir").getPath();
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(zipDirPath, "TEST_COLLECTION");
        long start = System.nanoTime();
        int counter = 0;
        for (DocumentDTO doc : zdtp) {
            System.out.println("counter: " + counter++);
            System.out.println("doc: " + doc.toString());
            assertEquals(doc.getDocumentMetadata().getCollection(), "TEST_COLLECTION");
        }
        System.out.println(((System.nanoTime()-start)/1000000000)+"sec.");
    }
}

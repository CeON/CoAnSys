package pl.edu.icm.coansys.importers;

import org.junit.Test;
import pl.edu.icm.coansys.importers.DocumentProtos.Document;
import static org.junit.Assert.*;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 */
public class ZipDirToProtosTest {
    
    //@Test
    public void readZipDirTest() {
        String zipDirPath = this.getClass().getClassLoader().getResource("zipdir").getPath();
        ZipDirToProtos zdtp = new ZipDirToProtos(zipDirPath, "TEST_COLLECTION");
        
        int counter = 0;
        for (Document doc : zdtp) {
            System.out.println("counter: " + counter++);
            System.out.println("doc: " + doc.toString());
            assertEquals(doc.getMetadata().getCollection(), "TEST_COLLECTION");
        }
    }
}

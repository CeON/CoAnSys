/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers;

import java.util.ArrayList;
import org.apache.hadoop.hbase.KeyValue;
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.transformers.DocumentDto2KeyValue;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 * @author pdendek
 */
public class ZipDirToHFileTest {

//    @Test
    public void readZipDirTest() {
        String zipDirPath = this.getClass().getClassLoader().getResource("zipdir").getPath();
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(zipDirPath, "TEST_COLLECTION");
//        long start = System.nanoTime();
//        int counter = 0;

        ArrayList<KeyValue> kvs = new ArrayList<KeyValue>();

        for (DocumentDTO doc : zdtp) {
            kvs.addAll(DocumentDto2KeyValue.translate(doc));
        }

//        HFile.Writer hfw = new HFile.Writer(Fs);
    }
}

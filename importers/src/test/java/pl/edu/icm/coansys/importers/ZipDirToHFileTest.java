/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.models.DocumentDTO;
import pl.edu.icm.coansys.importers.transformers.DocumentDTO2KeyValue;

/**
 *
 * @author Artur Czeczko a.czeczko@icm.edu.pl
 * @author pdendek
 */
public class ZipDirToHFileTest {

    public void readZipDirTest() {
        String zipDirPath = this.getClass().getClassLoader().getResource("zipdir").getPath();
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(zipDirPath, "TEST_COLLECTION");
        List<KeyValue> kvs = new ArrayList<KeyValue>();
        for (DocumentDTO doc : zdtp) {
            kvs.addAll(DocumentDTO2KeyValue.translate(doc));
        }
    }
}

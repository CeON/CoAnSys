/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */

package pl.edu.icm.coansys.importers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.hbase.KeyValue;
import org.junit.Test;

import pl.edu.icm.coansys.importers.iterators.ZipDirToDocumentDTOIterator;
import pl.edu.icm.coansys.importers.model.DocumentDTO;
import pl.edu.icm.coansys.importers.transformer.DocumentDTO2TSVLine;
import pl.edu.icm.coansys.importers.transformer.DocumentDto2KeyValue;

/**
 * @author pdendek
 */
public class ZipDirToTSVFileTest {

    @Test
    public void emptyTest() {
    }	
	
    @Test
    public void readZipDirTest() throws IOException {
        String zipDirPath = this.getClass().getClassLoader().getResource("zipdir").getPath();
        ZipDirToDocumentDTOIterator zdtp = new ZipDirToDocumentDTOIterator(zipDirPath, "TEST_COLLECTION");
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("zip2tsvTEST.ignore.tsv")));
        
        ArrayList<KeyValue> kvs = new ArrayList<KeyValue>();
        
        for (DocumentDTO doc : zdtp) {
        	bw.write(DocumentDTO2TSVLine.translate(doc));
        }
    }
}

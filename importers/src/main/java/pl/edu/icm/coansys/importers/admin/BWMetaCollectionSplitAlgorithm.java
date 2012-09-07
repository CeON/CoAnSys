/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.icm.coansys.importers.admin;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;

/**
 *
 * @author akawa
 */
/**
 * Example:
 * export HBASE_CLASSPATH=importers-sf/workflow/lib/importers-1.0-SNAPSHOT-jar-with-dependencies.jar 
 * akawa@hadoop:/mnt/tmp/workflows/oozie$ hbase org.apache.hadoop.hbase.util.RegionSplitter -D split.algorithm=pl.edu.icm.coansys.importers.admin.BWMetaCollectionSplitAlgorithm -c 2 -f m:c bwndataSplited
 */
public class BWMetaCollectionSplitAlgorithm implements SplitAlgorithm {

    private final String[] bigCollectionsPrefixes = {
        "medline_", "medline_PDF_", "springer_", "springer_PDF_"
    };
    
    private final int STEP = 10;

    @Override
    public byte[] split(byte[] bytes, byte[] bytes1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[][] split(int numberOfSplits) {
        List<byte[]> regions = new ArrayList<byte[]>();
        for (String prefix : bigCollectionsPrefixes) {
            for (char ch = '0'; ch <= '9'; ch += STEP) {
                regions.add(Bytes.toBytes(prefix + ch));
            }
            for (char ch = 'a'; ch <= 'z'; ch += STEP) {
                regions.add(Bytes.toBytes(prefix + ch));
            }
        }
        return regions.toArray(new byte[0][]);
    }

    @Override
    public byte[] firstRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] lastRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] strToRow(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String rowToStr(byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String separator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}



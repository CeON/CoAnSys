/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.commons.hbase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akawa
 */
public class SequenceFileSplitAlgorithm implements SplitAlgorithm {

    private static final String SPLIT_KEY_FILENAME_PROPERTY_NAME = "split.region.keys.file.name";
    public static final String SPLIT_KEY_FILE_DV = "keys";
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SequenceFileSplitAlgorithm.class);

    @Override
    public byte[] split(byte[] bytes, byte[] bytes1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[][] split(int numRegions) {

        String splitKeysFile = System.getProperty(SPLIT_KEY_FILENAME_PROPERTY_NAME, SPLIT_KEY_FILE_DV);
        List<byte[]> regions = new ArrayList<byte[]>();

        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(splitKeysFile));
            String line;
            while ((line = input.readLine()) != null) {
                regions.add(Bytes.toBytes(line));
            }
        } catch (Exception ex) {
            logger.error("Exception occured while reading file " + splitKeysFile, ex);
            return null;
        } finally {
            IOUtils.closeStream(input);
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

    @Override
    public void setFirstRow(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLastRow(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.auxil;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;

/**
 *
 * @author akawa
 */
    public class TextArrayWritable extends ArrayWritable {

        public TextArrayWritable() {
            super(Text.class);
        }

        public TextArrayWritable(Text[] array) {
            super(Text.class, array);
        }

        public List<String> toStringList() {
            return Arrays.asList(this.toStrings());
        }
    }
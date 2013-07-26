/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.auxil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 *
 * @author akawa
 * @author pdendek
 * @version 1.0
 * @since 2012-08-07
 */
    public class TextArrayWritable extends ArrayWritable  implements WritableComparable<TextArrayWritable> {

        public TextArrayWritable() {
            super(Text.class);
        }

        public TextArrayWritable(Text[] array) {
            super(Text.class, array);
        }

        public List<String> toStringList() {
            return Arrays.asList(this.toStrings());
        }

		@Override
		/**
		 * @author pdendek
		 * @since 2012-08-22
		 */
		public int compareTo(TextArrayWritable o) {
			List<String> osl = o.toStringList();
			List<String> tsl = this.toStringList();
			int val = tsl.size() - osl.size();
			if(val == 0){
				if(osl.containsAll(tsl)) {
                                    return 0;
                                }
				else{
					Iterator<String> io = osl.iterator();
					Iterator<String> it = tsl.iterator();
					for(;it.hasNext();){
						val = it.next().compareTo(io.next());
						if(val != 0) {
                                                    return val;
                                                }
					}
				}
			}else{
				return val;
			}
			//impossible
			return 0;
		}
    }
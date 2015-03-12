/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
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
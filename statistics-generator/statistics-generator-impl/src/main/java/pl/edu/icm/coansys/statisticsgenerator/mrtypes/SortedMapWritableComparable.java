/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2013 ICM-UW
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

package pl.edu.icm.coansys.statisticsgenerator.mrtypes;

import java.util.Iterator;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class SortedMapWritableComparable extends SortedMapWritable implements WritableComparable<SortedMapWritableComparable> {

    @Override
    public int compareTo(SortedMapWritableComparable otherMap) {
        
        // map with lower entries count is lower
        if (this.size() < otherMap.size()) {
            return -1;
        } else if (this.size() > otherMap.size()) {
            return 1;
        }
        
        // the same size - comparing of all entries up to first different
        Iterator<Entry<WritableComparable, Writable>> firstIterator = this.entrySet().iterator();
        Iterator<Entry<WritableComparable, Writable>> secondIterator = otherMap.entrySet().iterator();
        
        while (firstIterator.hasNext()) {
            Entry<WritableComparable, Writable> first = firstIterator.next();
            Entry<WritableComparable, Writable> second = secondIterator.next();
            
            // comparing keys
            int keysCompare = first.getKey().compareTo(second.getKey());
            
            if (keysCompare != 0) {
                return keysCompare;
            }
            
            // comparing values
            Writable firstValue = first.getValue();
            Writable secondValue = second.getValue();
            if (firstValue == secondValue || firstValue.equals(secondValue)) {
                continue;
            } // here the NullPointerException can be thrown -- it's ok.

            Comparable firstValueComparable = (Comparable) firstValue;
            Comparable secondValueComparable = (Comparable) secondValue;
            // if values are not comparable, we cannot compare it - so an exception will be thrown
            
            int valuesCompare = firstValueComparable.compareTo(secondValueComparable);
            if (valuesCompare != 0) {
                return valuesCompare;
            }
        }
        
        return 0;
    }

}

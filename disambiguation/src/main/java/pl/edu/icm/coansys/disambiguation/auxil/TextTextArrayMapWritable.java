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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 *
 * @author akawa
 * @version 1.0
 * @since 2012-08-07
 */
public class TextTextArrayMapWritable implements Writable {

    private MapWritable map = new MapWritable();
    private Text key = new Text();
    
    public Integer getSize() {
        return map.entrySet().size();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        map.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        map.readFields(in);
    }

    public Text getText(String skey) {
        key.set(skey);
        TextArrayWritable value = (TextArrayWritable) map.get(key);
        return (value != null ? (Text) value.get()[0] : null);
    }

    public String getString(String skey) {
        Text value = getText(skey);
        return (value != null ? value.toString() : null);
    }

    public void put(String skey, String svalue) {
        Text tkey = new Text(skey);
        Text tvalue = new Text(svalue);
        put(tkey, tvalue);
    }

    public void put(Text key, Text value) {
        Text[] tarray = {value};
        put(key, tarray);
    }

    public void put(Text key, Text[] tarray) {
        TextArrayWritable taw = new TextArrayWritable();
        taw.set(tarray);
        map.put(key, taw);
    }

    public void put(String skey, List<String> list) {
        if (list != null) {
            Text[] textArray = new Text[list.size()];
            int i = 0;
            for (String item : list) {
                textArray[i++] = new Text(item);
            }
            Text tkey = new Text(skey);
            put(tkey, textArray);
        }
    }

    public void clear() {
        map.clear();
    }

    public void putAll(TextTextArrayMapWritable other) {
        map.putAll(other.map);
    }

    public TextTextArrayMapWritable copy() {
        TextTextArrayMapWritable mapCopy = new TextTextArrayMapWritable();
        mapCopy.map = new MapWritable(map);
        return mapCopy;
    }

    public List<String> getStringList(String skey) {
        return getStringList(new Text(skey));
    }

    public List<String> getStringList(Text key) {
        TextArrayWritable list = (TextArrayWritable) map.get(key);
        return (list == null ? null : list.toStringList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(map.size());
        for (Writable wkey : map.keySet()) {
            sb.append(wkey.toString());
            List<String> values = getStringList(new Text(wkey.toString()));
            sb.append("\t").append(values).append("\n");
        }
        return sb.toString();
    }
}
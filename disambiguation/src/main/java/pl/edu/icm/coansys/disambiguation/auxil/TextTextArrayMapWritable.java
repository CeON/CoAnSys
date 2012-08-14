/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.auxil;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
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
public class TextTextArrayMapWritable implements Writable, Serializable {

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
        String s = "";
        for (Writable wkey : map.keySet()) {
            s += wkey.toString();
            List<String> values = getStringList(new Text(wkey.toString()));
            s += "\t" + values;
            s += "\n";
        }
        return s;
    }
}
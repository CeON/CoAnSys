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
package pl.edu.icm.coansys.commons.pig.udf;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class SequenceFileRecordWriter<K extends Writable, V extends Writable>
        extends RecordWriter<K, V> {

    private Writer writer;

    @SuppressWarnings("deprecation")
    public SequenceFileRecordWriter(Configuration conf, Class<?> keyClass, Class<?> valueClass)
            throws IOException, ClassNotFoundException {
        Path location = new Path(conf.get("location") + "/_tmp" + (new Date()).getTime());
        
        writer = SequenceFile.createWriter(location.getFileSystem(conf), conf, location, keyClass, valueClass);
    }

    @Override
    public void write(K key, V value) throws IOException {
        writer.append(key, value);
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        writer.close();
    }
}

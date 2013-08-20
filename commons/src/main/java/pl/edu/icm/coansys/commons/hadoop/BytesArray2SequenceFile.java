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

package pl.edu.icm.coansys.commons.hadoop;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public final class BytesArray2SequenceFile {

    private BytesArray2SequenceFile() {
    }

    private static Configuration createConf() {
        Configuration conf = new Configuration();
        String[] resources = {"/etc/hadoop/conf/core-site.xml", "/etc/hadoop/conf/hdfs-site.xml"};
        for (String resource : resources) {
            conf.addResource(resource);
        }
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        return conf;
    }

    public static void write(Iterable<byte[]> byteArrays, String uri) throws IOException {
        Configuration conf = createConf();
        Path path = new Path(uri);

        Writable key = NullWritable.get();
        SequenceFile.Writer writer = null;

        try {
            writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path), SequenceFile.Writer.keyClass(key.getClass()),
                    SequenceFile.Writer.valueClass(BytesWritable.class));

            for (byte[] byteArray : byteArrays) {
                writer.append(key, new BytesWritable(byteArray));
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    public static Iterable<byte[]> read(String uri) throws IOException {
        Configuration conf = createConf();
        Path path = new Path(uri);
        SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));

        return new BWIterable(reader, conf);
    }

    private static class BWIterable implements Iterable<byte[]> {

        private static final Logger logger = LoggerFactory.getLogger(BWIterable.class);
        private SequenceFile.Reader reader;
        private Configuration conf;

        public BWIterable(SequenceFile.Reader reader, Configuration conf) {
            this.reader = reader;
            this.conf = conf;
        }

        private static class BWIterator implements Iterator<byte[]> {

            private SequenceFile.Reader reader;
            private Configuration conf;
            private boolean nextAvailable;
            private byte[] nextBytes = null;

            public BWIterator(SequenceFile.Reader reader, Configuration conf) {
                this.reader = reader;
                this.conf = conf;
                nextAvailable = true;
                moveItem();
            }

            @Override
            public boolean hasNext() {
                return nextAvailable;
            }

            @Override
            public byte[] next() {
                if (nextAvailable) {
                    byte[] retBytes = nextBytes;
                    moveItem();
                    return retBytes;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Operation remove() is not supported");
            }

            private void moveItem() {
                if (nextAvailable) {
                    Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
                    BytesWritable value = new BytesWritable();
                    try {
                        nextAvailable = reader.next(key, value);
                    } catch (IOException ex) {
                        logger.error("moveItem: " + ex);
                        nextAvailable = false;
                    }
                    if (nextAvailable) {
                        nextBytes = value.copyBytes();
                    } else {
                        IOUtils.closeStream(reader);
                    }
                }
            }
        }

        @Override
        public Iterator<byte[]> iterator() {
            return new BWIterator(reader, conf);
        }
    }
}
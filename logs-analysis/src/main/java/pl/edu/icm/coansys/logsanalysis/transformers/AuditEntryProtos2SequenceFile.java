/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.transformers;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos.LogMessage;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntryProtos2SequenceFile {

    private static Configuration createConf() {
        Configuration conf = new Configuration();
        String[] resources = {"/etc/hadoop/conf/core-site.xml", "/etc/hadoop/conf/hdfs-site.xml"};
        for (String resource : resources) {
            conf.addResource(resource);
        }
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        return conf;
    }

    public static void writeLogsToSequenceFile(Iterable<AuditEntryProtos.LogMessage> messages, String uri) throws IOException {
        Configuration conf = createConf();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        System.out.println("URI: " + fs.getUri());
        Path path = new Path(uri);

        Text key = new Text();
        byte[] valueBytes;
        BytesWritable value = new BytesWritable();
        SequenceFile.Writer writer = null;

        try {
            //This method is deprecated. What should I use instead?...
            writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());

            for (AuditEntryProtos.LogMessage message : messages) {
                key.set(message.getEventId());
                valueBytes = message.toByteArray();
                value.set(valueBytes, 0, valueBytes.length);
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    public static Iterable<AuditEntryProtos.LogMessage> readLogsFromSequenceFile(String uri) throws IOException {
        Configuration conf = createConf();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(uri);

        List<AuditEntryProtos.LogMessage> result = new ArrayList<AuditEntryProtos.LogMessage>();

        Text key = new Text();
        BytesWritable value = new BytesWritable();
        SequenceFile.Reader reader = null;

        try {
            reader = new SequenceFile.Reader(fs, path, conf);

            while (reader.next(key, value)) {
                LogMessage parseFrom = AuditEntryProtos.LogMessage.parseFrom(value.copyBytes());
                result.add(parseFrom);
            }
        } finally {
            IOUtils.closeStream(reader);
        }
        return result;
    }
}

/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers;

import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;
import com.twitter.elephantbird.util.TypeRef;
import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import pl.edu.icm.coansys.importers.model.AuditEntryProtos;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class ImportLogs {

    private static void writeLogsToSequenceFile(Iterable<AuditEntryProtos.Entry> messages, String uri, String filePath) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(uri);

        Text key = new Text();
        ProtobufWritable value = new ProtobufWritable(new TypeRef<AuditEntryProtos.Entry>() {
        });
        SequenceFile.Writer writer = null;

        try {
            //This method is deprecated. What should I use instead?...
            writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());

            for (AuditEntryProtos.Entry message : messages) {
                key.set(message.getEventId());
                value.set(message);
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }
}
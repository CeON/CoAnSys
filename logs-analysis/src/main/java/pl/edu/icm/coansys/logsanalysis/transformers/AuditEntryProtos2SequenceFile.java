/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.transformers;

import com.twitter.elephantbird.mapreduce.io.ProtobufWritable;
import com.twitter.elephantbird.util.TypeRef;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import pl.edu.icm.coansys.logsanalysis.logsacquisition.GenerateDummyLogs;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntryProtos2SequenceFile {

    public static void writeLogsToSequenceFile(Iterable<AuditEntryProtos.LogMessage> messages, String uri) throws IOException {
        Configuration conf = new Configuration();
        conf.addResource("/etc/hadoop/conf/core-site.xml");
        conf.addResource("/etc/hadoop/conf/hdfs-site.xml");
        //FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(uri);

        Text key = new Text();
        ProtobufWritable value = new ProtobufWritable(new TypeRef<AuditEntryProtos.LogMessage>() {
        });
        SequenceFile.Writer writer = null;

        try {
            //This method is deprecated. What should I use instead?...
            writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());

            for (AuditEntryProtos.LogMessage message : messages) {
                key.set(message.getEventId());
                value.set(message);
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }
    
    public static Iterable<AuditEntryProtos.LogMessage> readLogsFromSequenceFile(String uri) throws IOException {
        Configuration conf = new Configuration();
        conf.addResource("/etc/hadoop/conf/core-site.xml");
        conf.addResource("/etc/hadoop/conf/hdfs-site.xml");
        //FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path(uri);

        List<AuditEntryProtos.LogMessage> result = new ArrayList<AuditEntryProtos.LogMessage>();
        
        Text key = new Text();
        ProtobufWritable value = new ProtobufWritable(new TypeRef<AuditEntryProtos.LogMessage>() {
        });
        SequenceFile.Reader reader = null;

        try {
            reader = new SequenceFile.Reader(fs, path, conf);
            
            while (reader.next(key, value)) {
                result.add((AuditEntryProtos.LogMessage) value.get());
            }
        } finally {
            IOUtils.closeStream(reader);
        }
        return result;
    }
    
   
    public static void main(String[] argv) throws ParseException, MalformedURLException, IOException {
        List<AuditEntry> entries = GenerateDummyLogs.generateLogs(10);
        List<AuditEntryProtos.LogMessage> protobufMessages = new ArrayList<AuditEntryProtos.LogMessage>();
        
        for (AuditEntry entry : entries) {
            protobufMessages.add(AuditEntry2Protos.serialize(entry));
        }
        
        writeLogsToSequenceFile(protobufMessages, "hdfs://localhost/tmp/testlogs.seqfile");
    }
}

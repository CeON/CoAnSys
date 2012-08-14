/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.transformers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import pl.edu.icm.coansys.logsanalysis.logsacquisition.GenerateDummyLogs;
import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntryTransformersTest {

    @Test
    public void serializeDeserializeTest() throws IOException, ParseException {

        // go through: 
        // AuditEntry -> AuditEntryProtos.LogMessage -> SequenceFile -> AuditEntryProtos.LogMessage -> AuditEntry

        List<AuditEntry> auditEntries;
        List<AuditEntryProtos.LogMessage> protosList = new ArrayList<AuditEntryProtos.LogMessage>();
        Iterable<AuditEntryProtos.LogMessage> readProtosList;
        List<AuditEntry> deserializedAuditEntries = new ArrayList<AuditEntry>();
        File tempFile = File.createTempFile("auditEntriesTransformations", ".seqfile");
        String tempFilePath = tempFile.getAbsolutePath();

        auditEntries = GenerateDummyLogs.generateLogs(20);
        for (AuditEntry entry : auditEntries) {
            protosList.add(AuditEntry2Protos.serialize(entry));
        }
        try {
            AuditEntryProtos2SequenceFile.writeLogsToSequenceFile(protosList, tempFilePath);
            readProtosList = AuditEntryProtos2SequenceFile.readLogsFromSequenceFile(tempFilePath);
        } finally {
            tempFile.delete();
        }
        for (AuditEntryProtos.LogMessage protoEntry : readProtosList) {
            deserializedAuditEntries.add(AuditEntry2Protos.deserialize(protoEntry));
        }

        // verifications: 

        assertEquals(auditEntries.size(), deserializedAuditEntries.size());

        if (auditEntries.size() == deserializedAuditEntries.size()) {
            AuditEntry expected;
            AuditEntry actual;
            for (int i = 0; i < auditEntries.size(); i++) {
                expected = auditEntries.get(i);
                actual = deserializedAuditEntries.get(i);

                assertEquals(expected.getEventId(), actual.getEventId());
                assertEquals(expected.getLevel(), actual.getLevel());
                assertEquals(expected.getServiceId(), actual.getServiceId());
                assertEquals(expected.getEventType(), actual.getEventType());
                assertEquals(expected.getTimestamp(), actual.getTimestamp());
                assertEquals(expected.getModule(), actual.getModule());
                assertArrayEquals(expected.getArgs(), actual.getArgs());
            }
        }
    }
}

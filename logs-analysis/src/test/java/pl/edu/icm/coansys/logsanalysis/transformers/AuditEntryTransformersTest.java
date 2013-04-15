/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.logsanalysis.transformers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static org.testng.AssertJUnit.*;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;
import pl.edu.icm.coansys.importers.models.LogsProtos.LogsMessage;
import pl.edu.icm.coansys.logsanalysis.logsacquisition.GenerateDummyLogs;
//import pl.edu.icm.coansys.logsanalysis.models.AuditEntryProtos;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntryTransformersTest {

    @org.testng.annotations.Test(groups = {"fast"})
    public void serializeDeserializeTest() throws IOException, ParseException {

        // go through: 
        // AuditEntry -> AuditEntryProtos.LogMessage as bytes array -> SequenceFile -> AuditEntryProtos.LogMessage -> AuditEntry

        List<AuditEntry> auditEntries;
        List<byte[]> protosList = new ArrayList<byte[]>();
        Iterable<byte[]> readProtosList;
        List<AuditEntry> deserializedAuditEntries = new ArrayList<AuditEntry>();
        File tempFile = File.createTempFile("auditEntriesTransformations", ".seqfile");
        String tempFilePath = tempFile.toURI().toString();

        auditEntries = GenerateDummyLogs.generateLogs(20);
        for (AuditEntry entry : auditEntries) {
            LogsMessage protoMessage = AuditEntry2Protos.serialize(entry);
            protosList.add(protoMessage.toByteArray());
        }
        try {
            BytesArray2SequenceFile.write(protosList, tempFilePath);
            readProtosList = BytesArray2SequenceFile.read(tempFilePath);
        } finally {
            tempFile.delete();
        }
        for (byte[] bytes : readProtosList) {
            LogsMessage protoMessage = LogsMessage.parseFrom(bytes);
            deserializedAuditEntries.add(AuditEntry2Protos.deserialize(protoMessage));
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

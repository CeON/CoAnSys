/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers.transformers;

import java.util.Arrays;
import java.util.Date;

import pl.edu.icm.coansys.importers.models.AuditEntryProtos;
import pl.edu.icm.coansys.importers.models.AuditEntryProtos.Entry.Builder;
import pl.edu.icm.synat.api.services.audit.model.AuditEntry;

/**
 *
 * @author Artur Czeczko <a.czeczko@icm.edu.pl>
 */
public class AuditEntry2Protos {

    public static AuditEntryProtos.Entry serialize(AuditEntry entry) {
        Builder builder = AuditEntryProtos.Entry.newBuilder();

        builder.setEventId(entry.getEventId()).setServiceId(entry.getServiceId()).
                setEventType(entry.getEventType()).setTimestamp(entry.getTimestamp().getTime());

        builder.setLevel(AuditEntryProtos.Level.valueOf(entry.getLevel().name()));

        builder.addAllArg(Arrays.asList(entry.getArgs()));

        return builder.build();
    }

    public static AuditEntry deserialize(AuditEntryProtos.Entry proto) {
        String[] args = new String[proto.getArgCount()];
        
        for (int i = 0; i < proto.getArgCount(); i++) {
            args[i] = proto.getArg(i);
        }
        
        return new AuditEntry(proto.getEventId(), AuditEntry.Level.valueOf(proto.getLevel().name()),
                new Date(proto.getTimestamp()), proto.getServiceId(), proto.getEventType(), args);
    }
}
